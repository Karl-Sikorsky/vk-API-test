package com.example.vkex;

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.httpClient.VKAbstractOperation;

/**
 * Created by ПОДАРУНКОВИЙ on 14.01.2017.
 */
public class Application extends android.app.Application {

@Override
public void onCreate(){
    super.onCreate();
    VKSdk.initialize(this);
}
}
