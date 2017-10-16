package com.example.leet.weatherdemo02;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thinkpage.lib.api.TPCity;
import com.thinkpage.lib.api.TPListeners;
import com.thinkpage.lib.api.TPWeatherDaily;
import com.thinkpage.lib.api.TPWeatherManager;
import com.thinkpage.lib.api.TPWeatherNow;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.lang.Math.log;

public class MainActivity extends Activity {
    @BindView(R.id.weather)
    TextView weather;
    @BindView(R.id.searchcity)
    EditText searchcity;
    @BindView(R.id.search)
    Button search;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.temp)
    TextView temp;
    @BindView(R.id.maxtemp)
    TextView maxtemp;
    @BindView(R.id.mintemp)
    TextView mintemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //search.setBackgroundColor(Color.TRANSPARENT);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityname=searchcity.getText().toString();
                if(cityname!=null&&cityname.length()>0) {
                    TPWeatherManager tpWeatherManager = TPWeatherManager.sharedWeatherManager();
                    tpWeatherManager.initWithKeyAndUserId("tlzphatfvpcmwmtc", "U3D61163F7");
                    tpWeatherManager.getWeatherNow(new TPCity(cityname)
                            , TPWeatherManager.TPWeatherReportLanguage.kSimplifiedChinese
                            , TPWeatherManager.TPTemperatureUnit.kCelsius
                            , new TPListeners.TPWeatherNowListener() {
                                @Override
                                public void onTPWeatherNowAvailable(TPWeatherNow weatherNow, String errorInfo) {
                                    if (weatherNow != null) {
                                        weather.setText(weatherNow.text);
                                        city.setText(searchcity.getText().toString());
                                        temp.setText(String.valueOf(weatherNow.temperature));
//                                    Log.d(weatherNow.text,"TAG");
                                    }
                                }
                            });
                    tpWeatherManager.getWeatherDailyArray(new TPCity(cityname)
                            , TPWeatherManager.TPWeatherReportLanguage.kSimplifiedChinese
                            , TPWeatherManager.TPTemperatureUnit.kCelsius
                            , new Date(), 1, new TPListeners.TPWeatherDailyListener() {
                                @Override
                                public void onTPWeatherDailyAvailable(TPWeatherDaily[] tpWeatherDailies, String s) {
                                    String temmax = String.valueOf(tpWeatherDailies[0].highTemperature);
                                    String temmin = String.valueOf(tpWeatherDailies[0].lowTemperature);
                                    maxtemp.setText(temmax);
                                    mintemp.setText(temmin);
                                }
                            });
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }else{
                    new AlertDialog.Builder(MainActivity.this).setTitle("天气预报").setMessage("城市名不能为空！")
                            .setPositiveButton("确定",null).show();
                }
            }
        });



    }
}
