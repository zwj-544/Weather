package com.example.stone.weather;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import bean.city;

/**
 * Created by czy on 17-4-11.
 */

public class CityActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        readCityFromAssets();
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
            Log.d("zwj","name ="+cities.get(i).getCityZh());
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
}
