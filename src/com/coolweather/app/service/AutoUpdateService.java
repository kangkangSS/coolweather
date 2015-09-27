package com.coolweather.app.service;

import com.coolweather.app.receiver.UpdataWeatherBroadcastReceiver;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自动生成的方法存根
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO 自动生成的方法存根
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				UpdateWeather();
			}

			
			
		}).start();
		AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
		long tipTime=System.currentTimeMillis()+1000*60*60*8;
		Intent UpdateIntent=new Intent(AutoUpdateService.this,UpdataWeatherBroadcastReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(this,0,UpdateIntent,0);
		alarmManager.set(alarmManager.RTC_WAKEUP, tipTime, pi);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
      private void UpdateWeather() {
				// TODO 自动生成的方法存根
    	  SharedPreferences pre=PreferenceManager.getDefaultSharedPreferences(this);
    	  String weatherCode=pre.getString("weather_code", "");
    	  String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
    	  HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO 自动生成的方法存根
				Utility utility=new Utility();
				utility.handleWeatherResponse(AutoUpdateService.this, response);
			}
			
			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				
			}
		});
				
		      	}
}
