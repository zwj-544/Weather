package com.example.stone.weather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.stone.weather.R;

import java.util.ArrayList;

/**
 * Created by czy on 17-6-1.
 */

public class CityManagerActivity extends Activity{

    private ListView listView;
    private ArrayList<String> cityList = new ArrayList<String>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_manager);

        listView = (ListView) findViewById(R.id.listview);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.getInt("num",0) > 0){
            for(int i = 1; i <= sharedPreferences.getInt("num",0); i++){
                cityList.add(sharedPreferences.getString(i+"",null));
            }
        }

        if(cityList.size() > 0){
            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,cityList));
        }
    }
}
