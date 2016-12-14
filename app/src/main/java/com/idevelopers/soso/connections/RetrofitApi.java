package com.idevelopers.soso.connections;

import com.idevelopers.soso.models.SendClas;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Gio on 11/14/2016.
 */

public interface RetrofitApi {

    @POST("http://soso.ge/sos/")
    Call<SendClas> send(@Body SendClas goObject);


}