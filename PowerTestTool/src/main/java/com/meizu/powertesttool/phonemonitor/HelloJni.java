package com.meizu.powertesttool.phonemonitor;

import android.util.Log;

/**
 * Created by wangwen1 on 16-7-7.
 */
public class HelloJni {
    private native String helloJni(String inputStr);

    public HelloJni (){
        Log.v("ww", helloJni("I am HelloJni"));

    }



    static
    {
        //加载库文件
        System.loadLibrary("HelloWorldJni");
     }



}
