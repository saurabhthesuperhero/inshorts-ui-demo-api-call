package com.fourrooms.inshortsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class FullNewsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imgB;
    TextView tv_title;
    CollapsingToolbarLayout collapsing_toolbar;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_news);
        String url= getIntent().getStringExtra("imgurl");
        String ttile=getIntent().getStringExtra("title");
        toolbar= findViewById(R.id.toolbar);
        collapsing_toolbar= findViewById(R.id.collapsing_toolbar);
        imgB= findViewById(R.id.imgB);
        collapsing_toolbar.setTitle(ttile);
        collapsing_toolbar.setMaxLines(3);
        Glide.with(this)
                .load(url)
//                        .load("https://images.pexels.com/photos/2246478/pexels-photo-2246478.jpeg")
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgB);
//        toolbar.setTitle(ttile);
        setSupportActionBar(toolbar);
//        toolbar.set
    }
}