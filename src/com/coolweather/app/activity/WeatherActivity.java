package com.coolweather.app.activity;


import com.coolweather.app.R;
import com.coolweather.app.service.AutoUpdateService;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.SearchManager.OnCancelListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends ActionBarActivity implements android.view.View.OnClickListener{
	
	private LinearLayout weatherInfolayout;
	private TextView cityNameText;
	private TextView publishText;
	private TextView weatherDespText;
	private TextView temp1Text;
	private TextView temp2Text;
	private TextView currentDateText;
	private Button BtnChangeCity;
	private Button BtnRefreshWeather;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_layout);
		weatherInfolayout=(LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText=(TextView) findViewById(R.id.city_name);
		publishText=(TextView) findViewById(R.id.publish_text);
		weatherDespText=(TextView) findViewById(R.id.weather_desp);
		temp1Text=(TextView) findViewById(R.id.temp1);
		temp2Text=(TextView) findViewById(R.id.temp2);
		currentDateText=(TextView) findViewById(R.id.current_date);
		BtnChangeCity=(Button) findViewById(R.id.BtnChangeCity);
		BtnRefreshWeather=(Button) findViewById(R.id.BtnRefreshWeather);
		String countyCode=getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			publishText.setText("同步中...");
			weatherInfolayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
			Log.d("WeatherActivity", "RUN");
		}else{
			showWeather();
		}
		BtnChangeCity.setOnClickListener(this);
		BtnRefreshWeather.setOnClickListener(this);
		
	}
	private void queryWeatherCode(String countyCode) {
		// TODO 自动生成的方法存根
		String address="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
	}
	
	private void queryWeatherInfo(String WeatherCode) {
		// TODO 自动生成的方法存根
		String address="http://www.weather.com.cn/data/cityinfo/"+WeatherCode+".html";
		queryFromServer(address,"weatherCode");
	}
	
	private void queryFromServer(String address, final String type) {
		// TODO 自动生成的方法存根
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO 自动生成的方法存根
				Log.d("WeatherActivity", "RUN1");
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						String[] array=response.split("\\|");
						if(array!=null&&array.length==2){
							String weatherCode=array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					Utility utility=new Utility();
					utility.handleWeatherResponse(WeatherActivity.this, response);
					Log.d("WeatherActivity", "RUN2");
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						publishText.setText("同步失败");
					}
				});
			}
		});
	}
	
	private void showWeather(){
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天"+prefs.getString("publish_time", "")+"发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfolayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		Intent intent=new Intent(this,AutoUpdateService.class);
		startService(intent);
		
	}
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.BtnChangeCity:
			Intent intent=new Intent(WeatherActivity.this,ChooseAreaActivity.class);
			intent.putExtra("fromWeatherActivity", true);
			startActivity(intent);
			finish();
			
			break;
		case R.id.BtnRefreshWeather:
			SharedPreferences pre=PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode=pre.getString("weather_code", "");
			if(!TextUtils.isEmpty(weatherCode)){
				queryWeatherCode(weatherCode);
			}
			break;

		default:
			break;
		}
	}

	
}
