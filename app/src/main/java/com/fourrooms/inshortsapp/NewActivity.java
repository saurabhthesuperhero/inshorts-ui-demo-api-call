package com.fourrooms.inshortsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.fourrooms.inshortsapp.adapter.ViewPagerAdapter;
import com.fourrooms.inshortsapp.databinding.ActivityMainBinding;
import com.fourrooms.inshortsapp.databinding.ActivityNewBinding;
import com.fourrooms.inshortsapp.model.LocalTechModel;

import java.util.ArrayList;

public class NewActivity extends AppCompatActivity {
    ArrayList<LocalTechModel> dataArrayList = new ArrayList<>();
    public ViewPagerAdapter adapter;
    public ActivityNewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new);
        adapter = new ViewPagerAdapter(this, dataArrayList);
        binding.viewPagerMain.setOffscreenPageLimit(1);
        binding.viewPagerMain.setAdapter(adapter);

        setDemoData();
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