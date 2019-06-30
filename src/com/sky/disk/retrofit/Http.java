package com.sky.disk.retrofit;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Http {
    private static Retrofit retrofit;
	private final static String BASE_URL="http://2z282r5149.51mypc.cn/";
    /**
     * 返回Retrofit
     */
    public static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
        	
        	ClearableCookieJar cookieJar =  new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
    		
    		OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cookieJar(cookieJar)
                    .build();
        	        	
            Retrofit.Builder builder = new Retrofit.Builder();//创建Retrfit构建器
            retrofit = builder
            		.client(okHttpClient)
                    .baseUrl(BASE_URL) //指定网络请求的baseUrl
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}