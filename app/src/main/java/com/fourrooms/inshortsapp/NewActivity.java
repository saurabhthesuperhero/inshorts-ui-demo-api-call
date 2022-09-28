package com.fourrooms.inshortsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.fourrooms.inshortsapp.adapter.ViewPagerAdapter;
import com.fourrooms.inshortsapp.databinding.ActivityMainBinding;
import com.fourrooms.inshortsapp.databinding.ActivityNewBinding;
import com.fourrooms.inshortsapp.model.LocalTechModel;

import java.util.ArrayList;

public class NewActivity extends AppCompatActivity {
    ArrayList<LocalTechModel> dataArrayList = new ArrayList<>();
    public ViewPagerAdapter adapter;
    public ActivityNewBinding binding;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new);
        adapter = new ViewPagerAdapter(this, dataArrayList);
        binding.viewPagerMain.setOffscreenPageLimit(1);
        binding.viewPagerMain.setAdapter(adapter);

        setDemoData();
        setBehaviour();
    }

    private void setBehaviour() {
        handler= new Handler();
        binding.viewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("checkmes", "position ,total " + position+" "+dataArrayList.size());
                handler.removeCallbacksAndMessages(null);
                if (!(position >=dataArrayList.size())) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.viewPagerMain.setCurrentItem(position + 1);

                        }
                    }, 3000);
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

    private void setDemoData() {
        dataArrayList.add(new LocalTechModel("data","https://sample-videos.com/img/Sample-jpg-image-50kb.jpg","hello hunny bunny 1","https://sample-videos.com/img/Sample-jpg-image-50kb.jpg",1));
        dataArrayList.add(new LocalTechModel("data","https://sample-videos.com/img/Sample-jpg-image-50kb.jpg","hello hunny bunny 2","https://sample-videos.com/img/Sample-jpg-image-50kb.jpg",1));
        dataArrayList.add(new LocalTechModel("fullscreenad","https://developers.google.com/static/admob/images/full-screen/image00.png","hello hunny bunny 2","https://developers.google.com/static/admob/images/full-screen/image00.png",1));
        dataArrayList.add(new LocalTechModel("data","https://sample-videos.com/img/Sample-jpg-image-50kb.jpg","hello hunny bunny 3","https://sample-videos.com/img/Sample-jpg-image-50kb.jpg",1));
        dataArrayList.add(new LocalTechModel("fullscreenad","https://tamilian.to/wp-content/uploads/2021/07/Haseen-Dillruba-poster.jpg","hello hunny bunny 4","https://tamilian.to/wp-content/uploads/2021/07/Haseen-Dillruba-poster.jpg",1));
        dataArrayList.add(new LocalTechModel("data","https://sample-videos.com/img/Sample-jpg-image-50kb.jpg","hello hunny bunny 5","https://sample-videos.com/img/Sample-jpg-image-50kb.jpg",1));
        adapter.notifyDataSetChanged();

    }
}