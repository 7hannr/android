package com.example.ex08;

import androidx.contentpager.content.Query;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RemoteService {
    public static final String BASE_URL="http://192.168.0.98:5000";

    @GET("/wine/list.json")
    Call<HashMap<String,Object>> list(@Query("page") int page);
}