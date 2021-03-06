package com.zhitech.test.testweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.zhitech.test.testweather.db.TestWeatherDb;
import com.zhitech.test.testweather.model.City;
import com.zhitech.test.testweather.model.County;
import com.zhitech.test.testweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;


/**
 * Created by cdg on 2016/11/15.
 */

public class Utility {
    public synchronized static boolean handleProvinceResponse(TestWeatherDb testWeatherDb,
                                                              String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if((allProvinces != null) && (allProvinces.length > 0)){
                for(String p : allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    testWeatherDb.savePovice(province);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCitiesResponse(TestWeatherDb testWeatherDb,
                                               String response,
                                               int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if((null != allCities) && (allCities.length > 0)){
                for(String c : allCities){
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    testWeatherDb.saveCity(city);
                }
                return true;
            }

        }
        return  false;
    }

    public static boolean handleCountiesResponse(TestWeatherDb testWeatherDb,
                                                 String response,
                                                 int cityId){
        if(TextUtils.isEmpty(response)){
            return false;
        }

        String[] allCounties = response.split(",");
        if((null == allCounties) || (allCounties.length <= 0)){
            return false;
        }

        for(String c : allCounties){
            String[] array = c.split("\\|");
            County county = new County();
            county.setCountyCode(array[0]);
            county.setCountyName(array[1]);
            county.setCityId(cityId);
            testWeatherDb.saveCounty(county);
        }
        return true;
    }

    public static void handleWeatherResponse(Context context, String response){
        try{
            Log.d("handleWeatherResponse->", "response="+response);
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            Log.d("handleWeatherResponse->", "cityName="+cityName);
            Log.d("handleWeatherResponse->", "weatherCode="+weatherCode);
            Log.d("handleWeatherResponse->", "temp1="+temp1);
            Log.d("handleWeatherResponse->", "temp2="+temp2);
            Log.d("handleWeatherResponse->", "weatherDesp="+weatherDesp);
            Log.d("handleWeatherResponse->", "publishTime="+publishTime);

            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,
                    weatherDesp, publishTime);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static void saveWeatherInfo(Context context, String cityName,
                                       String weatherCode, String temp1,
                                       String temp2, String weatherDesp,
                                       String publishTime){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(context).edit();
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
