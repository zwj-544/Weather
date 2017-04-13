package com.example.stone.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stone.weather.R;
import com.example.stone.weather.utils.HttpUtils;

import java.io.IOException;

/**
 * Created by zwj on 2017/4/13.
 */
public class WeatherActivity extends Activity{

    private TextView nowCity;
    private TextView weather;
    private TextView nowTemp;
    private TextView tempHighLow;
    private ImageView selectCity;

    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                nowTemp.setText(msg.getData().getString("tempNow"));
                weather.setText(msg.getData().getString("weather"));
                tempHighLow.setText(msg.getData().getString("tempHighLow"));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);

        nowCity = (TextView) findViewById(R.id.city);
        nowTemp = (TextView) findViewById(R.id.now_temp);
        weather = (TextView) findViewById(R.id.weather);
        tempHighLow = (TextView) findViewById(R.id.temp_high_low);
        selectCity = (ImageView) findViewById(R.id.select_city);

        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String url = "https://free-api.heweather.com/v5/weather?city="+city+"&key=a2b3fffe17c24c45905b78bbd8c85df5";
        nowCity.setText(city);
        selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectIntent = new Intent(getApplicationContext(), CityActivity.class);
                startActivity(selectIntent);
            }
        });

        try {
            HttpUtils.run(url,handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
