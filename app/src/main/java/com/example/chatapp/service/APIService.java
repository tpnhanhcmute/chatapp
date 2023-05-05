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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {

    final static String BASE_URL ="https://sleepy-gorge-18900.herokuapp.com/";

    public static APIService getAPIService(){
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    @POST("user/login")
    Call<ResponseModel> login(@Body LoginRequest request);

    @POST("user/logout")
    Call<ResponseModel>  logout(@Body LogoutRequest request);

    @POST("user/register")
    Call<ResponseModel>  register(@Body RegisterRequest request);

    @POST("user/update")
    Call<ResponseModel>  updateInfo(@Body UpdateInfoRequest request);

    @POST("common/resendOtp")
    Call<ResponseModel>  resendOTP(@Body ResendOTPRequest request);

    @POST("common/authenticateOtp")
    Call<ResponseModel>  authenticate(@Body UserRequest request);

    @POST("message/sendMessage")
    Call<ResponseModel>  sendMessage(@Body SendMessageRequest request);

    @POST("message/recallMessage")
    Call<ResponseModel>  recallMessage(@Body RecallMessageRequest request);

    @POST("contact/getContacts")
    Call<ResponseModel> getContacts(@Body UserRequest request);

}
