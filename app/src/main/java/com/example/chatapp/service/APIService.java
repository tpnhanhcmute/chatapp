package com.example.chatapp.service;

import com.example.chatapp.common.RetrofitClient;
import com.example.chatapp.model.request.LoginRequest;
import com.example.chatapp.model.request.LogoutRequest;
import com.example.chatapp.model.request.RecallMessageRequest;
import com.example.chatapp.model.request.RegisterRequest;
import com.example.chatapp.model.request.ResendOTPRequest;
import com.example.chatapp.model.request.SendMessageRequest;
import com.example.chatapp.model.request.UpdateInfoRequest;
import com.example.chatapp.model.request.UserRequest;
import com.example.chatapp.model.response.ResponseModel;

import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {

    final static String BASE_URL ="https://sleepy-gorge-18900.herokuapp.com/";

    public static APIService getAPIService(){
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    @POST("user/login")
    Callback<ResponseModel> login(@Body LoginRequest request);

    @POST("user/logout")
    Callback<ResponseModel> logout(@Body LogoutRequest request);

    @POST("user/register")
    Callback<ResponseModel> register(@Body RegisterRequest request);

    @POST("user/update")
    Callback<ResponseModel> updateInfo(@Body UpdateInfoRequest request);

    @POST("common/resendOtp")
    Callback<ResponseModel> resendOTP(@Body ResendOTPRequest request);

    @POST("common/authenticateOtp")
    Callback<ResponseModel> authenticate(@Body UserRequest request);

    @POST("message/sendMessage")
    Callback<ResponseModel> sendMessage(@Body SendMessageRequest request);

    @POST("message/recallMessage")
    Callback<ResponseModel> recallMessage(@Body RecallMessageRequest request);


}
