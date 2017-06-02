package com.example.stone.weather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.stone.weather.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by zwj on 2017/4/13.
 */
public class WeatherActivity extends FragmentActivity {


    private SharedPreferences sharedPreferences;
    private String city = null;
    private ViewPager vPager;
    private List<String> mCities = new ArrayList<String>();
    private List<Fragment> fragmentList ;
    private MyPagerAdapter adapter;
    private int cityNum;
    //private SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);

        /*swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_srl);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("zwj","refresh=="+vPager.getCurrentItem());
                //new LoadDataThread().start();
                fragmentList.get(vPager.getCurrentItem()).refresh();
            }
        });*/

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        cityNum = sharedPreferences.getInt("num",0);
        fragmentList = new ArrayList<Fragment>();
        if(cityNum != 0){
            for(int i = 1; i <= cityNum; i++){
                city = sharedPreferences.getString(i+"",null);
                mCities.add(city);
                fragmentList.add(new WeatherFragment(this,city));
            }
        }
        vPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);

        Intent intent = getIntent();
        /*if(intent != null){
            Log.d("zwj","intent1="+intent.getStringExtra("city"));
            if(intent.getStringExtra("city") != null){
                city = intent.getStringExtra("city");
                Log.d("zwj","intent");
            }
        }*/

        vPager.setAdapter(adapter);
        vPager.setCurrentItem(0);
        /*vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d("zwj","scroll=="+position+" ,offset=="+positionOffset+" ,pixels=="+positionOffsetPixels);
                //if(positionOffset>0.5){
                //    position++;
                //}
                vPager.setCurrentItem(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        if(city == null){
            intent = new Intent(WeatherActivity.this,CityActivity.class);
            startActivity(intent);
        }


    }

    @Override
    public void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if(intent != null && intent.getStringExtra("city") != null) {
            if(cityNum != sharedPreferences.getInt("num",0)) {
                cityNum = sharedPreferences.getInt("num", 0);
                fragmentList = new ArrayList<Fragment>();
                if (cityNum != 0) {
                    for (int i = 1; i <= cityNum; i++) {
                        mCities.add(city);
                        city = sharedPreferences.getString(i + "", null);
                        fragmentList.add(new WeatherFragment(this, city));
                    }
                }

                adapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList);
                vPager.setAdapter(adapter);
            }
            for (int i = 0; i<mCities.size(); i++){
                if(intent.getStringExtra("city").equals(mCities.get(i))){
                    vPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            // TODO Auto-generated constructor stub
            mFragments=fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mFragments.size();
        }

    }

}
