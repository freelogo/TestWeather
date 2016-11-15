package com.zhitech.test.testweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhitech.test.testweather.R;
import com.zhitech.test.testweather.db.TestWeatherDb;
import com.zhitech.test.testweather.model.City;
import com.zhitech.test.testweather.model.County;
import com.zhitech.test.testweather.model.Province;
import com.zhitech.test.testweather.R.layout.*;
import com.zhitech.test.testweather.util.HttpCallbackListener;
import com.zhitech.test.testweather.util.HttpUtil;
import com.zhitech.test.testweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chendingguo1 on 2016/11/15.
 */

public class ChooseAreaActivity extends Activity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progessDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private TestWeatherDb testWeatherDb;
    private List<String> dataList = new ArrayList<String>();

    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView)findViewById(R.id.list_view);
        titleText = (TextView)findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                dataList);
        listView.setAdapter(adapter);
        testWeatherDb = TestWeatherDb.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(index);
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(index);
                    queryCounties();

                }
            }
        });
        queryProvinces();
    }

    private void queryProvinces(){
        provinceList = testWeatherDb.loadProvinces();
        if(provinceList.size() > 0){
            dataList.clear();
            for(Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else{
            queryFromServer(null, "province");
        }
    }

    private void queryCities(){
        cityList = testWeatherDb.loadCities(selectedProvince.getId());
        if(cityList.size() > 0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }else{
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }

    private void queryCounties(){
        countyList = testWeatherDb.loadCounties(selectedCity.getId());
        if(countyList.size() > 0){
            dataList.clear();
            for(County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        }else {
            queryFromServer(selectedCity.getCityCode(), "county");
        }
    }

    private void queryFromServer(final String code, final String type){
        String address;
        if(!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";

        }else{
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(testWeatherDb, response);
                }else if("city".equals(type)){
                    result = Utility.handleCitiesResponse(testWeatherDb, response,
                            selectedProvince.getId());
                }else if("county".equals(type)){
                    result = Utility.handleCountiesResponse(testWeatherDb, response,
                            selectedCity.getId());
                }
                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,
                                "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void showProgressDialog(){
        if(progessDialog == null){
            progessDialog = new ProgressDialog(this);
            progessDialog.setMessage("正在加载...");
            progessDialog.setCanceledOnTouchOutside(false);
        }
        progessDialog.show();
    }

    private void closeProgressDialog(){
        if(null != progessDialog){
            progessDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if(currentLevel == LEVEL_COUNTY){
           queryCities();
        }else if(currentLevel == LEVEL_CITY){
            queryProvinces();
        }else {
            finish();
        }
    }
}




