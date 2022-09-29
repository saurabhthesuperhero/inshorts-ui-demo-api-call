package com.fourrooms.inshortsapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.fourrooms.inshortsapp.adapter.ViewPagerAdapterRoom;
import com.fourrooms.inshortsapp.databinding.ActivityNewsStackBinding;
import com.fourrooms.inshortsapp.model.LocalTechModelRoom;
import com.fourrooms.inshortsapp.retrofit.ApiClient;
import com.fourrooms.inshortsapp.retrofit.ApiInterface;
import com.fourrooms.inshortsapp.room.DatabaseClient;
import com.saurabhthesuperhero.viewpageranimationslib.DepthPageTransformer;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsStack extends AppCompatActivity {
    private static final String TAG = "NewsStack";

    ArrayList<LocalTechModelRoom> dataArrayList = new ArrayList<>();
    //    public MainAdapter adapter;
    public ViewPagerAdapterRoom adapter;
    ApiInterface apiInterface;
    int limit = 100;
    SharedPref sharedPreferences;
    public ActivityNewsStackBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_stack);
//        adapter = new MainAdapter(dataArrayList, this,null);
        adapter = new ViewPagerAdapterRoom(this, dataArrayList);
        binding.viewPagerMain.setAdapter(adapter);
        binding.viewPagerMain.setOffscreenPageLimit(3);
        binding.viewPagerMain.setPageTransformer(false, new DepthPageTransformer());

//        binding.viewPagerMain.getChildDrawingOrder()
//        binding.viewPagerMain.setPageTransformer(new SliderTransformer(3));
        bindData();

//        getData(1, limit);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                if (adapter.getCount()<1) return;
                binding.viewPagerMain.beginFakeDrag();
                binding.viewPagerMain.fakeDragBy(-2.0f);
                binding.viewPagerMain.endFakeDrag();
            }
        };
        handler.postDelayed(r, 10);  // some small delay in ms
    }

    //    private static class ViewPagerStack implements ViewPager.PageTransformer {

//        @Override
//        public void transformPage(View page, float position) {
//            Log.e(TAG, "transformPage: "+position );
////            if (position >= 0) {
////                page.setScaleX(0.7f - 0.05f * position);
////                page.setScaleY(0.7f);
////                page.setTranslationX(-page.getWidth() * position);
////                page.setTranslationY(30 * position);
////            }
//            if (position >= 0) {
//                page.setScaleX(0.9f - 0.05f * position);
//                page.setScaleY(0.9f);
//                page.setAlpha(1f - 0.3f * position);
//                page.setTranslationX(-page.getWidth() * position);
//                page.setTranslationY(-30 * position);
//            } else {
//                page.setAlpha(1 + 0.3f * position);
//                page.setScaleX( 0.9f + 0.05f * position);
//                page.setScaleY(0.9f);
//                page.setTranslationX(page.getWidth() * position);
//                page.setTranslationY(30 * position);
//            }
//        }
//        }

    public static class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.5f;
        private static final float MAX_SCALE = 0.8f;
        private static final float MIN_FADE = 0.2f;

        public void transformPage(android.view.View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) {
                view.setAlpha(MIN_FADE);
            } else if (position < 0) {
                view.setAlpha(1 + position * (1 - MIN_FADE));
                view.setTranslationX(-pageWidth * MAX_SCALE * position);
                ViewCompat.setTranslationZ(view, position);
                float scaleFactor = MIN_SCALE
                        + (MAX_SCALE - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else if (position == 0) {
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(MAX_SCALE);
                ViewCompat.setTranslationZ(view, 0);
                view.setScaleY(MAX_SCALE);
            } else if (position <= 1) {
                ViewCompat.setTranslationZ(view, -position);
                view.setAlpha(1 - position * (1 - MIN_FADE));
                view.setTranslationX(pageWidth * MAX_SCALE * -position);

                float scaleFactor = MIN_SCALE
                        + (MAX_SCALE - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setAlpha(MIN_FADE);
            }
        }
    }

    private void bindData() {
        class GetData extends AsyncTask<Void, Void, List<LocalTechModelRoom>> {

            @Override
            protected List<LocalTechModelRoom> doInBackground(Void... voids) {
                List<LocalTechModelRoom> list = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .Dao()
                        .getAllData();
                return list;
            }

            @Override
            protected void onPostExecute(List<LocalTechModelRoom> datas) {
                super.onPostExecute(datas);
                dataArrayList.addAll(datas);
                adapter.notifyDataSetChanged();

            }
        }

        GetData gd = new GetData();
        gd.execute();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getData(int page, int limit) {

        {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getImageList(page, limit);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {


                        class SaveData extends AsyncTask<Void, Void, Void> {

                            @Override
                            protected Void doInBackground(Void... voids) {

                                ArrayList<LocalTechModelRoom> data = new ArrayList<>();
                                try {
                                    String responses = response.body().string();


                                    JSONArray jsonArray = new JSONArray(responses);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        LocalTechModelRoom temp = new LocalTechModelRoom("data");
                                        temp.setTitle(jsonArray.getJSONObject(i).getJSONObject("title").getString("rendered"));
                                        temp.setImgUrl(jsonArray.getJSONObject(i).getJSONObject("parselyMeta").getString("parsely-image-url"));
                                        temp.setThumn(jsonArray.getJSONObject(i).getJSONObject("parselyMeta").getString("parsely-image-url"));
                                        Log.e(TAG, "doInBackground: url" + jsonArray.getJSONObject(i).getString("jetpack_featured_media_url"));
                                        Log.e(TAG, "doInBackground: url" + jsonArray.getJSONObject(i).getJSONObject("parselyMeta").getString("parsely-image-url"));

                                        data.add(temp);
                                    }
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }

                                //adding to database
                                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                        .Dao()
                                        .insert(data);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                            }
                        }

                        SaveData st = new SaveData();
                        st.execute();
                    } catch (Exception e) {
                        Log.e(TAG, "onResponse: " + e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());

                }
            });
        }

    }
}