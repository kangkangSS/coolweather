package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {
	
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
		
		if(!TextUtils.isEmpty(response)){
			String[] allProvince=response.split(",");
			if(allProvince!=null&&allProvince.length>0){
				for(String p:allProvince){
					String[] array=p.split("\\|");
					Province province=new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherDB.saveProvice(province);
				}
				return true;
			}
		}
		return false;
		
	}
	
	public static boolean handleCitiesReponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			
			Log.d("ut12", "OK1");
			String[] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for(String c:allCities){
					String[] array=c.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
		
	}
	
	public static boolean handleCountiesReponse(CoolWeatherDB coolWeatherDB,String response,int countyId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for(String c:allCities){
					String[] array=c.split("\\|");
					County county=new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(countyId);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
		
	}
	
	public void handleWeatherResponse(Context context,String response){
		Log.d("ut12", "OK");
		
			JSONObject jsonObject;
			JSONObject weatherInfo = null;
			String cityName=null;
			String weatherCode=null;
			String temp1=null;
			String temp2=null;
			String weatherDesp=null;
			String publishTime=null;
			try {
				jsonObject = new JSONObject(response);
				weatherInfo=jsonObject.getJSONObject("weatherinfo");
				

			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				Log.d("ut12", "异常");
			}
			try {
				cityName=weatherInfo.getString("city");
				weatherCode=weatherInfo.getString("cityid");
				temp1=weatherInfo.getString("temp1");
				temp2=weatherInfo.getString("temp2");
				weatherDesp=weatherInfo.getString("weather");
				publishTime=weatherInfo.getString("ptime");	
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				Log.d("ut12", "异常2");
			}
			
			saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);		
			
			
		
		
	}

	private static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		// TODO 自动生成的方法存根
		Log.d("ut12", "OK2");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences .Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
		
	}

}
