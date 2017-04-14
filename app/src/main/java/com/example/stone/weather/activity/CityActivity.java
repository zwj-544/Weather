package com.example.stone.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.stone.weather.R;
import com.example.stone.weather.bean.City;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by czy on 17-4-11.
 */

public class CityActivity extends Activity implements TextWatcher, View.OnClickListener, AMapLocationListener, AdapterView.OnItemClickListener{

    EditText searchCity;
    ListView searchCityList;
    List<String> cityList = new ArrayList<String>();
    TextView locate;
    private AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mlocationClient = null;
    private GridView hotCity;
    private TextView quickSelect;
    SharedPreferences sharedPreferences;
    Set<String> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        readCityFromAssets();
        quickSelect = (TextView) findViewById(R.id.quick_select_city);
        searchCity = (EditText) findViewById(R.id.search_city);
        searchCity.addTextChangedListener(this);
        searchCityList = (ListView) findViewById(R.id.search_city_list);
        searchCityList.setOnItemClickListener(this);
        //locate = (TextView) findViewById(R.id.location);
        //locate.setOnClickListener(this);
        hotCity = (GridView) findViewById(R.id.hot_city);
        hotCity.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.hot_city)));
        hotCity.setOnItemClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        cities = sharedPreferences.getStringSet("cities",null);
        if(cities == null){
            cities = new HashSet<String >();
        }
    }
    
    private void readCityFromAssets(){
        try {
            InputStream is = getAssets().open("chinacity.txt");
            String text = readText(is);
            handleCities(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCities(String text) {
        List<City> cities;
        Gson gson = new Gson();
        cities = gson.fromJson(text,new TypeToken<List<City>>(){}.getType());
        for(int i = 0; i<cities.size(); i++){
            cityList.add(cities.get(i).getCityZh());
        }
    }

    private String readText(InputStream is) {
        StringBuffer buffer = new StringBuffer("");
        try {
            InputStreamReader reader = new InputStreamReader(is,"utf-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String str;
            while ((str = bufferedReader.readLine()) != null){
                buffer.append(str);
                buffer.append("\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(s.length() != 0){
            List<String> list = new ArrayList<String>();
            for(int i=0; i<cityList.size(); i++){
                if(cityList.get(i).contains(s)){
                    list.add(cityList.get(i));
                }
            }
            searchCityList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list));
            searchCityList.setVisibility(View.VISIBLE);
            hotCity.setVisibility(View.GONE);
            quickSelect.setVisibility(View.GONE);
        }else {
            searchCityList.setVisibility(View.GONE);
            hotCity.setVisibility(View.VISIBLE);
            quickSelect.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.location:
                mlocationClient = new AMapLocationClient(this);//初始化定位参数
                mLocationOption = new AMapLocationClientOption();//设置定位监听
                mlocationClient.setLocationListener(this);//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//设置定位间隔,单位毫秒,默认为2000ms
                mLocationOption.setInterval(2000);//设置定位参数
                mlocationClient.setLocationOption(mLocationOption);
                mlocationClient.startLocation();
                break;*/
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getCity();
                String name=amapLocation.getCity().substring(0,amapLocation.getCity().length()-1);
                cities.add(name);
                sharedPreferences.edit().putStringSet("cities",cities).apply();
                Log.d("zwj:", "city===" + name);
                mlocationClient.stopLocation();
                Intent intent = new Intent(this, WeatherActivity.class);
                intent.putExtra("city",name);
                startActivity(intent);
                finish();
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.hot_city){
            if(position == 0){
                mlocationClient = new AMapLocationClient(this);//初始化定位参数
                mLocationOption = new AMapLocationClientOption();//设置定位监听
                mlocationClient.setLocationListener(this);//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//设置定位间隔,单位毫秒,默认为2000ms
                mLocationOption.setInterval(2000);//设置定位参数
                mlocationClient.setLocationOption(mLocationOption);
                mlocationClient.startLocation();
            }else {
                //searchCity.setText(getResources().getStringArray(R.array.hot_city)[position]);
                cities.add(getResources().getStringArray(R.array.hot_city)[position]);
                sharedPreferences.edit().putStringSet("cities",cities).apply();
                Intent intent = new Intent(this, WeatherActivity.class);
                intent.putExtra("city", getResources().getStringArray(R.array.hot_city)[position]);
                Log.d("zwj","put =="+getResources().getStringArray(R.array.hot_city)[position]);
                startActivity(intent);
                finish();
            }
        }
        if(parent.getId() == R.id.search_city_list){
            String city = (String) ((TextView) view).getText();
            cities.add(city);
            sharedPreferences.edit().putStringSet("cities",cities).apply();
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("city", city);
            startActivity(intent);
            finish();
        }
    }
}
