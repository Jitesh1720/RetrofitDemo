package com.example.retrofitdemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int INTENT_REQUEST_CODE = 100;

    Context context;

    private Button mBtImageSelect;
    private Button btn_api_multipart;
    private Button btn_api_normal;
    ImageView ivImage;
    private ProgressBar mProgressBar;
    private String mediaPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        initViews();

    }

    private void initViews() {

        mBtImageSelect = findViewById(R.id.btn_select_image);
        btn_api_multipart = findViewById(R.id.btn_api_multipart);
        btn_api_normal = findViewById(R.id.btn_api_normal);
        ivImage = findViewById(R.id.ivImage);
        mProgressBar = findViewById(R.id.progress);

        mBtImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, INTENT_REQUEST_CODE);

            }
        });

        btn_api_multipart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    // returns Object class
                    //callMultipartApi();

                    // returns Model class
                    callMultipartApiResult();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_api_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callNormalApi();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                //str1.setText(mediaPath);
                // Set the Image in ImageView for Previewing the Media
                ivImage.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();

            }
        }
    }

    private void callMultipartApi() {

        try {

            Retrofit retrofit = RetrofitClient.getRetrofitClient(context);
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

            mProgressBar.setVisibility(View.VISIBLE);
            // Map is used to multipart the file using okhttp3.RequestBody
            File file = new File(mediaPath);

            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profile_image", file.getName(), requestBody);

            Call<Object> call = retrofitInterface.uploadImage(RetrofitClient.LOGIN_TOKEN, fileToUpload);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {

                    mProgressBar.setVisibility(View.GONE);

                    if (response.isSuccessful()) {

                        String res = "";
                        try {
                            res = new Gson().toJson(response.body());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Snackbar.make(findViewById(R.id.content), res, Snackbar.LENGTH_SHORT).show();

                    } else {

                        ResponseBody errorBody = response.errorBody();

                        Gson gson = new Gson();

                        try {
                            mProgressBar.setVisibility(View.GONE);
                            //Response errorResponse = gson.fromJson(errorBody.string(), Response.class);
                            Snackbar.make(findViewById(R.id.content), errorBody.string(), Snackbar.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    mProgressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    Snackbar.make(findViewById(R.id.content), t.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(findViewById(R.id.content), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void callMultipartApiResult() {

        try {

            Retrofit retrofit = RetrofitClient.getRetrofitClient(context);
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

            mProgressBar.setVisibility(View.VISIBLE);
            // Map is used to multipart the file using okhttp3.RequestBody
            File file = new File(mediaPath);

            // Parsing any Media type file
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("profile_image", file.getName(), requestBody);

            Call<ApiResponse> call = retrofitInterface.uploadImageResult(RetrofitClient.LOGIN_TOKEN, fileToUpload);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {

                    mProgressBar.setVisibility(View.GONE);

                    if (response.isSuccessful()) {

                        ApiResponse responseBody = response.body();
                        Snackbar.make(findViewById(R.id.content), responseBody.getMessage(), Snackbar.LENGTH_SHORT).show();

                    } else {

                        ResponseBody errorBody = response.errorBody();

                        Gson gson = new Gson();

                        try {
                            mProgressBar.setVisibility(View.GONE);
                            //Response errorResponse = gson.fromJson(errorBody.string(), Response.class);
                            Snackbar.make(findViewById(R.id.content), errorBody.string(), Snackbar.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                    mProgressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    Snackbar.make(findViewById(R.id.content), t.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(findViewById(R.id.content), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void callNormalApi() {

        try {

            Retrofit retrofit = RetrofitClient.getRetrofitClient(context);
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

            mProgressBar.setVisibility(View.VISIBLE);

            Call<Object> call = retrofitInterface.loginApi("", "",
                    "", "", "");
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {

                    mProgressBar.setVisibility(View.GONE);

                    if (response.isSuccessful()) {

                        String res = "";
                        try {
                            // TreeLinkedMap to string convert
                            res = new Gson().toJson(response.body());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "onSuccessfull: " + res);
                        Snackbar.make(findViewById(R.id.content), res, Snackbar.LENGTH_SHORT).show();

                    } else {

                        ResponseBody errorBody = response.errorBody();

                        Gson gson = new Gson();

                        try {
                            mProgressBar.setVisibility(View.GONE);
                            Log.d(TAG, "onSuccessfull: " + errorBody.string());
                            //Response errorResponse = gson.fromJson(errorBody.string(), Response.class);
                            Snackbar.make(findViewById(R.id.content), errorBody.string(), Snackbar.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                    mProgressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    Snackbar.make(findViewById(R.id.content), t.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(findViewById(R.id.content), e.getLocalizedMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

}
