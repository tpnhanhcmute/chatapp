package com.example.chatapp.service;

import com.example.chatapp.common.RetrofitClient;

public interface APIService {

    final static String BASE_URL ="";

    public static APIService getAPIService(){
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
