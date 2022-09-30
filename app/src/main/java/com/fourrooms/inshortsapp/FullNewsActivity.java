package com.fourrooms.inshortsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
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
        collapsing_toolbar.setMaxLines(5);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);
        Glide.with(this)
                .load(url)
//                        .load("https://images.pexels.com/photos/2246478/pexels-photo-2246478.jpeg")
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgB);
//        toolbar.setTitle(ttile);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        toolbar.set

        RunNotification();
    }


    private void RunNotification() {

        NotificationManager  notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.item_noti);
//        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        Intent switchIntent = new Intent(this, NewsStack.class);
        PendingIntent pendingSwitchIntent = PendingIntent.getActivity(this, 1020, switchIntent, PendingIntent.FLAG_IMMUTABLE);
        contentView.setOnClickPendingIntent(R.id.ooooo, pendingSwitchIntent);

        mBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        mBuilder.setContent(contentView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Notification notification = mBuilder.build();
        notificationManager.notify(1, notification);
    }
}