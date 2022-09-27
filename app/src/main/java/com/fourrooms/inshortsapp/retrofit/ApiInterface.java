package com.fourrooms.inshortsapp.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("v2/posts")
    Call<ResponseBody> getImageList(@Query("page") int page,
                                               @Query("per_page") int limit);
}
