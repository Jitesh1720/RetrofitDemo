package com.example.retrofitdemo;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "";
    public static final String LOGIN_TOKEN = "your_token";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitClient(Context context) {

        if (retrofit == null) {

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // For add Header
                    /*.addInterceptor(
                            new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request().newBuilder()
                                            .addHeader("login_token", "your_token").build();
                                    return chain.proceed(request);
                                }
                            })*/
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
