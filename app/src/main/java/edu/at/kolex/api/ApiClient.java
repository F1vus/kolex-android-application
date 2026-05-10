package edu.at.kolex.api;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;

import edu.at.kolex.api.adapter.LocalDateTimeAdapter;
import edu.at.kolex.api.interceptor.AuthInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://172.20.10.4:8080/";
    private static Retrofit retrofit;

    public static void init(Context context) {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(
                            LocalDateTime.class,
                            new LocalDateTimeAdapter()
                    )
                    .create();


            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context.getApplicationContext()))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            throw new IllegalStateException("ApiClient not initialized");
        }
        return retrofit;
    }
}
