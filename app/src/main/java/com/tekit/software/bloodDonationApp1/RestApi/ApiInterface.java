package com.tekit.software.bloodDonationApp1.RestApi;

import com.tekit.software.bloodDonationApp1.Request.LoginRequest;
import com.tekit.software.bloodDonationApp1.Request.OTPRequest;
import com.tekit.software.bloodDonationApp1.Request.RegistrationRequest;
import com.tekit.software.bloodDonationApp1.Response.LoginResponse;
import com.tekit.software.bloodDonationApp1.Response.OTPResponse;
import com.tekit.software.bloodDonationApp1.Response.RegistrationResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("authentication/forget")
    Call<OTPResponse> authenticateUser(@HeaderMap Map<String, String> headers, @Body OTPRequest otpRequest);

    @POST("authentication/login")
    Call<LoginResponse> LoginUser(@HeaderMap Map<String, String> headers,@Body LoginRequest loginRequest);

    @POST("authentication/registration")
    Call<RegistrationResponse> registrationUser(@HeaderMap Map<String, String> headers,@Body RegistrationRequest registrationRequest);


}
