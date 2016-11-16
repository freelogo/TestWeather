package com.zhitech.test.testweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by cdg on 2016/11/14.
 */

import com.zhitech.test.testweather.model.*;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class TestWeatherDb {
    //数据库名
    public static final String DB_NAME = "test_weather";
    //db version
    public static final int DB_VERSION = 1;

    private static TestWeatherDb testWeatherDb;
    private SQLiteDatabase db;

    private TestWeatherDb(Context context){
        TestWeatherOpenHelpler testWeatherOpenHelpler
                = new TestWeatherOpenHelpler(context, DB_NAME, null, DB_VERSION);

        db = testWeatherOpenHelpler.getWritableDatabase();
    }

    //获取TestWeatherDb单例
    public synchronized static TestWeatherDb getInstance(Context context){
        if(null == testWeatherDb)
            testWeatherDb = new TestWeatherDb(context);

        return  testWeatherDb;
    }

    //put the province to db
    public void savePovice(Province province){
        if(null != province){
            ContentValues contentValues = new ContentValues();
            contentValues.put("province_name", province.getProvinceName());
            contentValues.put("province_code", province.getProvinceCode());
            db.insert("Province", null, contentValues);
            Log.d("savePovice()-->", "province_code:"+province.getProvinceCode());
        }
    }

    //get province list from db
    public List<Province> loadProvinces(){
        List<Province> listProvince = new ArrayList<Province>();
        Cursor cursor = db.query("province", null, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                listProvince.add(province);
            }while (cursor.moveToNext());
        }

        return  listProvince;
    }

    //save city to db
    public void saveCity(City city){
        if(null != city){
            ContentValues contentValues = new ContentValues();
            contentValues.put("city_name", city.getCityName());
            contentValues.put("city_code", city.getCityCode());
            contentValues.put("province_id", city.getProvinceId());
            db.insert("City", null, contentValues);
            Log.d("saveCity()-->", "city_code:"+city.getCityCode());
        }
    }

    //get city list from db
    public List<City> loadCities(int provinceId) {
        List<City> listCity = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id=?",
                new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                listCity.add(city);
            } while (cursor.moveToNext());
        }

        return listCity;
    }

    //save county to db
    public void saveCounty(County county){
         if(null != county){
             ContentValues contentValues = new ContentValues();
             contentValues.put("county_name", county.getCountyName());
             contentValues.put("county_code", county.getCountyCode());
             contentValues.put("city_id", county.getCityId());
             db.insert("County", null, contentValues);
        }
    }

    //get county list from db
    public List<County> loadCounties(int cityId){
        List<County> listCounty = new ArrayList<County>();
        Cursor cursor = db.query("County", null, "city_id = ?",
                new String[]{String.valueOf(cityId)}, null, null, null);
        if(cursor.moveToFirst()){
            do{
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                listCounty.add(county);
            }while (cursor.moveToNext());
        }

        return listCounty;
    }



}
