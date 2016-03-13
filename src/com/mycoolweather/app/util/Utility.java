package com.mycoolweather.app.util;

import android.text.TextUtils;

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
	
}







