package com.example.stone.weather;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import bean.city;

/**
 * Created by czy on 17-4-11.
 */

public class CityActivity extends Activity implements TextWatcher{

    EditText searchCity;
    ListView searchCityList;
    List<String> cityList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        readCityFromAssets();
        searchCity = (EditText) findViewById(R.id.search_city);
        searchCity.addTextChangedListener(this);
        searchCityList = (ListView) findViewById(R.id.search_city_list);
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
        List<city> cities;
        Gson gson = new Gson();
        cities = gson.fromJson(text,new TypeToken<List<city>>(){}.getType());
        Log.d("zwj","length = "+cities.size());
        for(int i = 0; i<cities.size(); i++){
            Log.d("zwj","name ="+cities.get(i).getCityZh()+" ,advance ="+cities.get(i).getProvinceZh());
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
        }else {
            searchCityList.setVisibility(View.GONE);
        }

    }
}
