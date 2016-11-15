package com.zhitech.test.testweather.util;

/**
 * Created by chendingguo1 on 2016/11/15.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
