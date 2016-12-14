package com.idevelopers.soso.connections;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gio on 11/14/2016.
 */

public class RetrofitSingleTone {


    private static RetrofitSingleTone ourInstance = null;

    private  RetrofitApi api;

    private RetrofitSingleTone(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(RetrofitApi.class);
    }

    public static RetrofitSingleTone getInstance() {
        if(ourInstance == null){
            ourInstance = new RetrofitSingleTone();
        }
        return ourInstance;
    }

    public RetrofitApi getRetrofitApi(){
        return this.api;
    }
}
