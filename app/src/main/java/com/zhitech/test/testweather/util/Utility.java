package com.zhitech.test.testweather.util;

import android.text.TextUtils;

import com.zhitech.test.testweather.db.TestWeatherDb;
import com.zhitech.test.testweather.model.City;
import com.zhitech.test.testweather.model.County;
import com.zhitech.test.testweather.model.Province;

/**
 * Created by chendingguo1 on 2016/11/15.
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
            String[] allCities = response.split(".");
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



}
