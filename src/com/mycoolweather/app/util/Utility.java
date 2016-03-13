package com.mycoolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.mycoolweather.app.db.CoolWeatherDB;
import com.mycoolweather.app.model.City;
import com.mycoolweather.app.model.County;
import com.mycoolweather.app.model.Province;

public class Utility {

	public synchronized static boolean handleProvinceResponse(
			CoolWeatherDB coolweatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length > 0){
				for(String p:allProvinces){
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolweatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	public synchronized static boolean handleCitiesResponse(
			CoolWeatherDB coolweatherDB,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length > 0){
				for(String p:allCities){
					String[] array = p.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolweatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	public synchronized static boolean handleCountiesResponse(
			CoolWeatherDB coolweatherDB,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if(allCounties != null && allCounties.length > 0){
				for(String p:allCounties){
					String[] array = p.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					coolweatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
	public static void handleWeatherResponse(Context context,String response){
		LogUtil.d(LogUtil.TAG, "handleWeatherResponse executed ");
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weather");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityId");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
			
			LogUtil.d(LogUtil.TAG,"city_ame is:"+cityName);
			LogUtil.d(LogUtil.TAG,"weather_code is:"+weatherCode);
			LogUtil.d(LogUtil.TAG,"temp1 is:"+temp1);
			LogUtil.d(LogUtil.TAG,"temp2 is:"+temp2);
			LogUtil.d(LogUtil.TAG,"weather_desp is:"+weatherDesp);
			LogUtil.d(LogUtil.TAG,"publish_time is:"+publishTime);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		// TODO Auto-generated method stub
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyƒÍm‘¬d»’",Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name",cityName);
		editor.putString("weather_code",weatherCode);
		editor.putString("temp1",temp1);
		editor.putString("temp2",temp2);
		editor.putString("weather_desp",weatherDesp);
		editor.putString("publish_time",publishTime);
		editor.putString("current_time",sdf.format(new Date()));
		editor.commit();
		
		
		
	}
	
}

















