package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {

	public static final String DB_NAME="cool_weather";
	public static final int VERSION=1;
	public static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	
	private CoolWeatherDB(Context context) {
		// TODO 自动生成的构造函数存根
		CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db=dbHelper.getWritableDatabase();
	}
	
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB==null){
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
		
	}
	
	public void saveProvice(Province province){
		if(province!=null){
			ContentValues value=new ContentValues();
			value.put("province_name", province.getProvinceName());
			value.put("province_code",province.getProvinceCode());
			db.insert("Province", null, value);
		}
	}
	
	public List<Province> loadProvince(){
		List<Province> list=new ArrayList<Province>();
		Cursor cursor=db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province=new Province();
				province.setId(cursor.getColumnIndex("id"));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}

	public void saveCity(City city){
		if(city!=null){
			ContentValues value=new ContentValues();
			value.put("city_name", city.getCityName());
			value.put("city_code",city.getCityCode());
			db.insert("City", null, value);
		}
	}
	
	public List<City> loadCity(){
		List<City> list=new ArrayList<City>();
		Cursor cursor=db.query("City", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city=new City();
				city.setId(cursor.getColumnIndex("id"));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	public void saveCounty(County county){
		if(county!=null){
			ContentValues value=new ContentValues();
			value.put("county_name", county.getCountyName());
			value.put("county_code",county.getCountyCode());
			db.insert("County", null, value);
		}
	}
	
	public List<County> loadCounty(){
		List<County> list=new ArrayList<County>();
		Cursor cursor=db.query("County", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				County county=new County();
				county.setId(cursor.getColumnIndex("id"));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				list.add(county);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}

}
