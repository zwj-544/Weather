package com.example.stone.weather.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.stone.weather.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by zwj on 2017/4/13.
 */
public class WeatherActivity extends Activity {


    private SharedPreferences sharedPreferences;
    private Set<String> cities;
    private String city = null;
    private ViewPager vPager;
    private List<String> mCities = new ArrayList<String>();
    private List<WeatherFragment> fragmentList ;
    private MyPagerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
        vPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new MyPagerAdapter(getFragmentManager());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        cities = sharedPreferences.getStringSet("cities",null);
        fragmentList = new ArrayList<WeatherFragment>();
        if(cities != null){
            Iterator<String> i = cities.iterator();
            mCities.clear();
            while (i.hasNext()){
                city = i.next();
                Log.d("zwj","new fragment=="+city);
                fragmentList.add(new WeatherFragment(this,city));
                mCities.add(city);
            }
        }

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
        vPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.d("zwj","scroll=="+position+" ,offset=="+positionOffset+" ,pixels=="+positionOffsetPixels);
                if(positionOffset>0.5){
                    position++;
                }
                vPager.setCurrentItem(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if(city == null){
            intent = new Intent(WeatherActivity.this,CityActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        cities = sharedPreferences.getStringSet("cities",null);
        fragmentList = new ArrayList<WeatherFragment>();
        if(cities != null){
            Iterator<String> i = cities.iterator();
            mCities.clear();
            while (i.hasNext()){
                city = i.next();
                Log.d("zwj","new fragment=="+city);
                fragmentList.add(new WeatherFragment(this,city));
                mCities.add(city);
            }
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public class MyPagerAdapter extends PagerAdapter
    {
        private FragmentTransaction mCurTransaction;
        private FragmentManager mFragmentManager;
        private Fragment mCurrentPrimaryItem = null;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            mFragmentManager=fragmentManager;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String name=mCities.get(position);

            mCurTransaction=mFragmentManager.beginTransaction();
            Fragment fragment = mFragmentManager.findFragmentByTag(name);
            if (fragment != null) {
                Log.d("zwj","attach");
                mCurTransaction.attach(fragment);
            } else {
                Log.d("zwj","add");
                fragment = fragmentList.get(position);
                mCurTransaction.add(container.getId(), fragment, name);
                mCurTransaction.commitAllowingStateLoss();
            }
            return fragment;

        }

        @Override
        public void finishUpdate(View container) {
            if (mCurTransaction != null) {
                //mCurTransaction.commitAllowingStateLoss();
                mCurTransaction = null;
                mFragmentManager.executePendingTransactions();
            }
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {

            Fragment fragment = (Fragment)object;
            if (fragment != mCurrentPrimaryItem) {
                if (mCurrentPrimaryItem != null) {
                    FragmentCompat.setMenuVisibility(mCurrentPrimaryItem, false);
                }
                if (fragment != null) {
                    FragmentCompat.setMenuVisibility(fragment, true);
                }
                mCurrentPrimaryItem = fragment;
            }
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            if (mCurTransaction == null) {
                mCurTransaction = mFragmentManager.beginTransaction();
            }
            /*Log.v("test", "Detaching item #" + position + ": f=" + object
                    + " v=" + ((Fragment)object).getView());*/
            mCurTransaction.detach((Fragment)object);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return ((Fragment)object).getView() == view;
        }
    }
}
