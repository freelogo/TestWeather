package com.zhitech.test.testweather.model;

/**
 * Created by chendingguo1 on 2016/11/14.
 */

public class County {
    private int id;
    private String countyName;
    private String countyCode;
    private int cityId;

    public int getId(){
        return id;
    }

    public void setId(int iId){
        this.id = iId;
    }

    public String getCountyName(){
        return countyName;
    }

    public void setCountyName(String strCountyName){
        this.countyName = strCountyName;
    }

    public String getCountyCode(){
        return countyCode;
    }

    public void setCountyCode(String strCountyCode){
        this.countyCode = strCountyCode;
    }

    public int getCityId(){
        return cityId;
    }

    public void setCityId(int iCityId){
        this.cityId = iCityId;
    }
}
