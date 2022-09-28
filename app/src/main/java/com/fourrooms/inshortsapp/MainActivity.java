package com.fourrooms.inshortsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.fourrooms.inshortsapp.adapter.ViewPagerAdapter;
import com.fourrooms.inshortsapp.databinding.ActivityMainBinding;
import com.fourrooms.inshortsapp.model.LocalTechModel;
import com.fourrooms.inshortsapp.retrofit.ApiClient;
import com.fourrooms.inshortsapp.retrofit.ApiInterface;
import com.mindorks.core.ScreenshotBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
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
    SharedPref sharedPreferences;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences= new SharedPref(this);
        final Integer modeVal = sharedPreferences.loadNightModeState();
        if (modeVal == 0) {
            setMode(0);
        } else if (modeVal == 1) {
            setMode(1);
        } else if (modeVal == 2) {
            setMode(2);
        }
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        adapter = new ViewPagerAdapter(this, dataArrayList);
        binding.viewPagerMain.setOffscreenPageLimit(1);
        binding.viewPagerMain.setAdapter(adapter);

        getData(1, limit);
        getMoreData(limit);

        binding.btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bmp = new ScreenshotBuilder(MainActivity.this).setView(findViewById(R.id.ll_viewpager)).getScreenshot();
                shareImageUri(saveImage(bmp));
            }
        });

        binding.btMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogForMode();
            }
        });
    }

    private void showAlertDialogForMode() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Change Mode");
        String[] items = {"Night Mode", "Day Mode", "Automatic (Follow System)"};
        int checkedItem = sharedPreferences.loadNightModeState();
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        setMode(0);
                        dialog.dismiss();
                        break;
                    case 1:
                        setMode(1);
                        dialog.dismiss();
                        break;
                    case 2:
                        setMode(2);
                        dialog.dismiss();
                        break;

                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    private void setMode(int i) {
        switch (i) {
            case 0:
                sharedPreferences.setNightModeState(0);
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_YES);
                break;
            case 1:
                sharedPreferences.setNightModeState(1);

                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_NO);
                break;
            case 2:
                sharedPreferences.setNightModeState(2);
                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }

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
                            temp.setThumn(jsonArray.getJSONObject(i).getJSONObject("parselyMeta").getString("parsely-image-url"));
                            Log.e(TAG, "doInBackground: url" + jsonArray.getJSONObject(i).getString("jetpack_featured_media_url"));
                            Log.e(TAG, "doInBackground: url" + jsonArray.getJSONObject(i).getJSONObject("parselyMeta").getString("parsely-image-url"));

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
                        notLoading = false;
                        Log.e(TAG, "onPageScrolled: positon, dataarraylist.size : " + position + " " + dataArrayList.size());

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
                                                temp.setThumn(jsonArray.getJSONObject(i).getJSONObject("parselyMeta").getString("parsely-image-url"));
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


                } else {
//                    new Screenshot(this)
//                            .setView(/** the view **/)
//                            .setQuality(Quality.HIGH)
//                            .setFlip(Flip.HORIZONTALLY)
//                            .getAsBitmap();

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

    /**
     * Saves the image as PNG to the app's cache directory.
     *
     * @param image Bitmap to save.
     * @return Uri of the saved file or null
     */
    private Uri saveImage(Bitmap image) {
        //TODO - Should be processed in another thread
        File imagesFolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(this, "com.mydomain.fileprovider", file);

        } catch (IOException e) {
            Log.d(TAG, "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }

    private void shareImageUri(Uri uri) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/png");
        startActivity(intent);
        Log.e(TAG, "shareImageUri: " + uri);
    }

}