package com.msp.retrofitdemo;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitInterface {

    @Multipart
    //@Headers("login_token: " + RetrofitClient.LOGIN_TOKEN)
    @POST("Users/updateProfilePic")
    Call<Object> uploadImage(@Header("login_token") String token, @Part MultipartBody.Part image);

    @Multipart
    @POST("Users/updateProfilePic")
    Call<ApiResponse> uploadImageResult(@Header("login_token") String token, @Part MultipartBody.Part image);

    @POST("Users/login")
    @FormUrlEncoded
    Call<Object> loginApi(@Field("username") String username,
                          @Field("password") String password,
                          @Field("device_type") String device_type,
                          @Field("device_id") String device_id,
                          @Field("device_token") String device_token);

}
