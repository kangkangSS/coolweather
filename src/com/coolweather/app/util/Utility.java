package com.coolweather.app.util;

import android.text.TextUtils;

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

}
