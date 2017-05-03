package com.example.stone.weather.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stone.weather.R;
import com.example.stone.weather.utils.HttpUtils;

import java.io.IOException;
import java.util.Set;

/**
 * Created by czy on 17-4-27.
 */

public class WeatherFragment extends Fragment {
    private TextView nowCity;
    private TextView weather;
    private TextView nowTemp;
    private TextView tempHighLow;
    private ImageView selectCity;
    private Set<String> cities;
    private String city = null;
    private Context mContext;

    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                nowTemp.setText(msg.getData().getString("tempNow"));
                weather.setText(msg.getData().getString("weather"));
                tempHighLow.setText(msg.getData().getString("tempHighLow"));
            }
        }
    };

    public WeatherFragment(Context context, String city) {
        this.city = city;
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.weather_fragment, container, false);
        Log.d("zwj","fragment create");
        nowCity = (TextView) view.findViewById(R.id.city);
        nowTemp = (TextView) view.findViewById(R.id.now_temp);
        weather = (TextView) view.findViewById(R.id.weather);
        tempHighLow = (TextView) view.findViewById(R.id.temp_high_low);
        selectCity = (ImageView) view.findViewById(R.id.select_city);

        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectIntent = new Intent(mContext, CityActivity.class);
                startActivity(selectIntent);
            }
        });
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("zwj","fragmentcity=="+city);
        if(city != null){
            nowCity.setText(city);
            String url = "https://free-api.heweather.com/v5/weather?city="+city+"&key=a2b3fffe17c24c45905b78bbd8c85df5";
            try {
                HttpUtils.run(url,handler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
