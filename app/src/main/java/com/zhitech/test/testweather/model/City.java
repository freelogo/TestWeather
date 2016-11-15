package com.zhitech.test.testweather.model;

/**
 * Created by chendingguo1 on 2016/11/14.
 */

public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;

    public int getId(){
        return id;
    }

    public void setId(int iId){
        this.id = iId;
    }

    public String getCityName(){
        return cityName;
    }

    public void setCityName(String strCityName){
        this.cityName = strCityName;
    }

    public String getCityCode(){
        return cityCode;
    }

    public void setCityCode(String strCityCode){
        this.cityCode = strCityCode;
    }

    public int getProvinceId(){
        return provinceId;
    }

    public void setProvinceId(int iProvinceId){
        this.provinceId = iProvinceId;
    }
}
