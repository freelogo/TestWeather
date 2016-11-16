package com.zhitech.test.testweather.util;

/**
 * Created by cdg on 2016/11/15.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
