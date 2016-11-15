package com.zhitech.test.testweather.model;

/**
 * Created by chendingguo1 on 2016/11/14.
 */

public class Province {
    private int id;
    private String provinceName;
    private String provinceCode;

    public int getId(){
        return id;
    }

    public void setId(int iId){
        this.id = iId;
    }

    public String getProvinceName(){
        return provinceName;
    }

    public void setProvinceName(String strProvinceName){
        this.provinceName = strProvinceName;
    }

    public String getProvinceCode(){
        return provinceCode;
    }

    public void setProvinceCode(String strProvinceCode){
        this.provinceCode = strProvinceCode;
    }

}
