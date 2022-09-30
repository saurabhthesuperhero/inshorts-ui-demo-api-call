package com.fourrooms.inshortsapp;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.fourrooms.inshortsapp.adapter.MainAdapter;
import com.fourrooms.inshortsapp.databinding.StaggerdMainBinding;
import com.fourrooms.inshortsapp.model.LocalTechModelRoom;
import com.fourrooms.inshortsapp.retrofit.ApiClient;
import com.fourrooms.inshortsapp.retrofit.ApiInterface;
import com.fourrooms.inshortsapp.room.DatabaseClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaggeredMainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static boolean isnetworkOn2ndTime = true;
    ArrayList<LocalTechModelRoom> dataArrayList = new ArrayList<>();
    public MainAdapter adapter;
    ApiInterface apiInterface;
    int limit = 10;
    int totalItemCount;
    int visibleItemCount;
    int pastVisibleItems;
    boolean notLoading = true;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    public static StaggerdMainBinding binding;
    private BroadcastReceiver MyReceiver = null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.staggerd_main);
        adapter = new MainAdapter(dataArrayList, this, new MainAdapter.OnClickListener() {
            @Override
            public void onRowClick(int position) {
//                Intent intent = new Intent(getApplicationContext(), ImageActivity.class);
//                Bundle args = new Bundle();
//                args.putSerializable("ARRAYLIST",(Serializable)dataArrayList);
//                intent.putExtra("BUNDLE",args);
//                intent.putExtra("imageLoc",position);
//                startActivity(intent);

            }
        });
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerview.setLayoutManager(staggeredGridLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration(24);
        binding.recyclerview.addItemDecoration(decoration);
        binding.recyclerview.setAdapter(adapter);

        getData(1, limit);
        getMoreData(limit);
        broadcastIntent();
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int mSpace;

        public SpacesItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = mSpace;
        }
    }

    public static void rvScrolltoPosition(int pos) {
        binding.recyclerview.scrollToPosition(pos);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getData(int page, int limit) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.getImageList(page, limit);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dataArrayList.clear();
                ArrayList<LocalTechModelRoom> localTechModel = new ArrayList<>();
                if (response.body() == null) return;
                try {
                    String responses = response.body().string();


                    JSONArray jsonArray = new JSONArray(responses);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        LocalTechModelRoom temp = new LocalTechModelRoom("data");
                        temp.setTitle(jsonArray.getJSONObject(i).getJSONObject("title").getString("rendered"));
                        temp.setImgUrl(jsonArray.getJSONObject(i).getString("jetpack_featured_media_url"));
                        Log.e(TAG, "doInBackground: url" + jsonArray.getJSONObject(i).getString("jetpack_featured_media_url"));

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


    public void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getMoreData(int limit) {
        binding.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (notLoading) {
                     visibleItemCount = staggeredGridLayoutManager.getChildCount();
                     totalItemCount = staggeredGridLayoutManager.getItemCount();

                    int[] firstVisibleItems = null;
                    firstVisibleItems = staggeredGridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                    if(firstVisibleItems != null && firstVisibleItems.length > 0) {
                        pastVisibleItems = firstVisibleItems[0];
                    }
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {

                            dataArrayList.add(new LocalTechModelRoom("progress"));
                            recyclerView.post(new Runnable() {
                                public void run() {
                                    // There is no need to use notifyDataSetChanged()
                                    adapter.notifyItemInserted(dataArrayList.size() - 1);
                                }
                            });

                            notLoading = false;

                            apiInterface = ApiClient.getClient().create(ApiInterface.class);
                            Call<ResponseBody> call = apiInterface.getImageList(dataArrayList.size() - 1, limit);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        dataArrayList.remove(dataArrayList.size() - 1);
                                        adapter.notifyItemRemoved(dataArrayList.size());

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

                                            ArrayList<LocalTechModelRoom> localTechModel = new ArrayList<>();
                                            try {
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    LocalTechModelRoom temp = new LocalTechModelRoom("data");
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
            }
        });


    }


}