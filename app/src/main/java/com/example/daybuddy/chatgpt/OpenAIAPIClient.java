package com.example.daybuddy.chatgpt;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class OpenAIAPIClient {
    private static final String BASE_URL = "https://api.openai.com/v1/";

    // Define the API service interface
    public interface OpenAIAPIService {
        @Headers("Authorization: Bearer My--API--Key")
        @POST("chat/completions")
        Call<OpenAIResponseModel> getCompletion(@Body OpenAIRequestModel requestModel);
    }

    // Create the Retrofit instance and API service
    public static OpenAIAPIService create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(OpenAIAPIService.class);
    }
}