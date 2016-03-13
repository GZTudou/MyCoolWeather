package com.mycoolweather.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mycoolweather.app.R;
import com.mycoolweather.app.util.HttpCallbackListener;
import com.mycoolweather.app.util.HttpUtil;
import com.mycoolweather.app.util.LogUtil;
import com.mycoolweather.app.util.Utility;

public class WeatherActivity extends Activity implements OnClickListener{

	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;
	private TextView publishText;
	private TextView weatherDespText;
	private TextView temp1Text;
	private TextView temp2Text;
	private TextView currentDataText;
	
	private Button switchCity;
	private Button refreshWeather;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		//初始化各种空间
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDataText = (TextView) findViewById(R.id.current_date);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		
		String countyCode = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			//有县级代号的去查询天气
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			LogUtil.d(LogUtil.TAG, "countyCode is "+countyCode);
			queryWeatherCode(countyCode);
		}else{
			showWeather();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.switch_city:
			Intent intent = new Intent(this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_actity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中...");
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = pref.getString("weather_code", "");
			if(!TextUtils.isEmpty(weatherCode)){
				queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		}
	}

	private void queryWeatherCode(String countyCode) {
		// TODO Auto-generated method stub
		
		String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
	}
	private void queryWeatherInfo(String weatherCode) {
		// TODO Auto-generated method stub
		LogUtil.d(LogUtil.TAG, "queryWeatherInfo executed "+weatherCode);
		String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromServer(address,"weatherCode");
		LogUtil.d(LogUtil.TAG, "queryWeatherInfo executed "+address);
	}
	
	private void queryFromServer(final String address, final String type) {
		// TODO Auto-generated method stub
	
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				// TODO Auto-generated method stub
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						String[] array = response.split("\\|");
						if(array != null && array.length == 2){
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					LogUtil.d(LogUtil.TAG, "queryFromServer executed ");
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishText.setText("同步失败");
					}
				});
			}
		});
	}

	
	private void showWeather() {
		// TODO Auto-generated method stub
	
		LogUtil.d(LogUtil.TAG, "showWeathre() executed ");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city-name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText(prefs.getString("publish_time", "")+"发布");
		currentDataText.setText(prefs.getString("current_data", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}



	
}
































