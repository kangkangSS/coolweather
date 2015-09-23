package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;
import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends ActionBarActivity {
	
	public static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY=1;
	public static final int LEVEL_COUNTY=2;
	
	private ProgressDialog progressDialog;
	private TextView textTitle;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList=new ArrayList<String>();
	
	private List<Province> provinceList;
	private List<City> cityList;
	private List<County> countyList;
	
	private Province selectedProvince;
	private City selectedCity;
	
	private int currentLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_area);
		listView=(ListView) findViewById(R.id.list_View);
		textTitle=(TextView) findViewById(R.id.text_title);
		adapter=new ArrayAdapter<String>(ChooseAreaActivity.this,android.R.layout.simple_list_item_1,dataList);
		listView.setAdapter(adapter);
		coolWeatherDB=CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				if(currentLevel==LEVEL_PROVINCE){
					selectedProvince=provinceList.get(arg2);
					queryCities();
				}else if(currentLevel==LEVEL_CITY){
					selectedCity=cityList.get(arg2);
					queryCounties();
				}
			}

			
		});
		queryProvinces();
	}

	private void queryProvinces() {
		// TODO 自动生成的方法存根
		provinceList=coolWeatherDB.loadProvince();
		if(provinceList.size()>0){
			dataList.clear();
			for(Province province:provinceList){
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textTitle.setText("中国");
			currentLevel=LEVEL_PROVINCE;
		}else{
			querFromServer(null,"province");
		}
	}
	
	private void queryCities(){
		cityList=coolWeatherDB.loadCity(selectedProvince.getId());
		if(cityList.size()>0){
			dataList.clear();
			for(City city:cityList){
				dataList.add(city.getCityName());				
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textTitle.setText(selectedProvince.getProvinceName());
			currentLevel=LEVEL_CITY;
		}else{
			querFromServer(selectedProvince.getProvinceCode(),"city");
		}
	}
	
	private void queryCounties(){
		countyList=coolWeatherDB.loadCounty(selectedCity.getId());
		if(countyList.size()>0){
			dataList.clear();
			for(County county:countyList){
				dataList.add(county.getCountyName());				
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textTitle.setText(selectedCity.getCityName());
			currentLevel=LEVEL_COUNTY;
		}else{
			querFromServer(selectedCity.getCityCode(),"county");
		}
	}
	
	private void querFromServer(final String code,final String type) {
		// TODO 自动生成的方法存根
		String address;
		if(!TextUtils.isEmpty(code)){
			address="http://www.weather.com.cn/data/list3/city"+code+".xml";
		}else{
			address="http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO 自动生成的方法存根
				boolean result=false;
				if("province".equals(type)){
					result=Utility.handleProvincesResponse(coolWeatherDB, response);
				}else if("city".equals(type)){
					result=Utility.handleCitiesReponse(coolWeatherDB, response, selectedProvince.getId());
				}else if("county".equals(type)){
					result=Utility.handleCountiesReponse(coolWeatherDB, response, selectedCity.getId());
				}
				if(result){
					runOnUiThread(new Runnable() {
						public void run() {
							closeProgressDialog();
							if("province".equals(type)){
								queryProvinces();
							}else if("city".equals(type)){
								queryCities();
							}else if("county".equals(type)){
								queryCounties();
							}
						}

						
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private void showProgressDialog() {
		// TODO 自动生成的方法存根
		if(progressDialog==null){
			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("正在加载");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}
		
		
	}
	
	private void closeProgressDialog() {
		// TODO 自动生成的方法存根
		if(progressDialog!=null){
			progressDialog.dismiss();
		}
		
	}
	
	public void onBackPressed(){
		if(currentLevel==LEVEL_COUNTY){
			queryCities();
		}else if(currentLevel==LEVEL_CITY){
			queryProvinces();
		}else{
			finish();
		}
	}
 
}
