package com.example.stone.weather.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.stone.weather.bean.Weather;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


/**
 * Created by zwj on 2017/4/13.
 */
public class HttpUtils {

    public static void run(String url, final Handler handler) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            String mJson;
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                mJson = response.body().string();
                Weather weather;
                Gson gson = new Gson();
                weather = gson.fromJson(mJson, Weather.class);
                Log.d("zwj","json=="+mJson);
                Message message = new Message();
                message.what = 1;
                Bundle b = new Bundle();
                b.putString("tempNow",weather.getHeWeather5().get(0).getNow().getTmp()+"°");
                b.putString("weather",weather.getHeWeather5().get(0).getNow().getCond().getTxt());
                b.putString("tempHighLow", weather.getHeWeather5().get(0).getDaily_forecast().get(0).getTmp().getMax() + "° / " + weather.getHeWeather5().get(0).getDaily_forecast().get(0).getTmp().getMin()+"°");
                Log.d("zwj", "temp==" + weather.getHeWeather5().get(0).getNow().getTmp() + " ,weather==" + weather.getHeWeather5().get(0).getNow().getCond().getTxt() +
                        " ,high==" + weather.getHeWeather5().get(0).getDaily_forecast().get(0).getTmp().getMax()
                        + " ,min==" + weather.getHeWeather5().get(0).getDaily_forecast().get(0).getTmp().getMin());
                message.setData(b);
                handler.sendMessage(message);
            }

        });
    }
}
