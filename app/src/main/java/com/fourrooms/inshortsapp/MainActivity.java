package com.fourrooms.inshortsapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.fourrooms.inshortsapp.adapter.ViewPagerAdapter;
import com.fourrooms.inshortsapp.databinding.ActivityMainBinding;
import com.fourrooms.inshortsapp.model.LocalTechModel;
import com.fourrooms.inshortsapp.retrofit.ApiClient;
import com.fourrooms.inshortsapp.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    ArrayList<LocalTechModel> dataArrayList = new ArrayList<>();
    public ViewPagerAdapter adapter;
    ApiInterface apiInterface;
    int limit = 10;
    boolean notLoading = true;
    public static ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        adapter = new ViewPagerAdapter(this, dataArrayList);
        binding.viewPagerMain.setOffscreenPageLimit(4);
        binding.viewPagerMain.setAdapter(adapter);

        getData(1, limit);
        getMoreData(limit);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getData(int page, int limit) {

        {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getImageList(page, limit);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dataArrayList.clear();
                    ArrayList<LocalTechModel> localTechModel = new ArrayList<>();
                    if (response.body() == null) return;
                    try {
                        String responses = response.body().string();


                        JSONArray jsonArray = new JSONArray(responses);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            LocalTechModel temp = new LocalTechModel("data");
                            temp.setTitle(jsonArray.getJSONObject(i).getJSONObject("title").getString("rendered"));
                            temp.setImgUrl(jsonArray.getJSONObject(i).getJSONObject("parselyMeta").getString("parsely-image-url"));
//                            Log.e(TAG, "doInBackground: url" + jsonArray.getJSONObject(i).getString("jetpack_featured_media_url"));

                            localTechModel.add(temp);
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    dataArrayList.addAll(localTechModel);
                    adapter.notifyDataSetChanged();


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());

                }
            });
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getMoreData(int limit) {
        binding.viewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (notLoading) {
                    if (position == dataArrayList.size() - 1) {
                        dataArrayList.add(new LocalTechModel("progress"));
                        adapter.notifyDataSetChanged();
                    }
                    notLoading = false;

                    apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<ResponseBody> call = apiInterface.getImageList(dataArrayList.size() - 1, limit);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                dataArrayList.remove(dataArrayList.size() - 1);
                                adapter.notifyDataSetChanged();

                                String responses = null;
                                try {
                                    responses = response.body().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                JSONArray jsonArray = null;
                                try {
                                    jsonArray = new JSONArray(responses);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (jsonArray.length() > 0) {

                                    ArrayList<LocalTechModel> localTechModel = new ArrayList<>();
                                    try {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            LocalTechModel temp = new LocalTechModel("data");
                                            temp.setTitle(jsonArray.getJSONObject(i).getJSONObject("title").getString("rendered"));
                                            temp.setImgUrl(jsonArray.getJSONObject(i).getString("jetpack_featured_media_url"));
                                            localTechModel.add(temp);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    dataArrayList.addAll(localTechModel);
                                    adapter.notifyDataSetChanged();
                                    notLoading = true;

                                } else {
                                    Toast.makeText(MainActivity.this, "End of data reached", Toast.LENGTH_LONG).show();
                                }


                            }
                        }


                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e(TAG, "onFailure():");

                        }
                    });

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

}