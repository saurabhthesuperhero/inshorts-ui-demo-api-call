package com.fourrooms.inshortsapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.fourrooms.inshortsapp.FullNewsActivity;
import com.fourrooms.inshortsapp.R;
import com.fourrooms.inshortsapp.model.LocalTechModelRoom;

import java.util.List;

public class ViewPagerAdapterRoom extends PagerAdapter {

    List<LocalTechModelRoom> sliderItems;
    LayoutInflater mLayoutInflater;
    Context context;

    public ViewPagerAdapterRoom(Context context, List<LocalTechModelRoom> sliderItems) {
        this.context = context;
        this.sliderItems = sliderItems;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sliderItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        String viewType= sliderItems.get(position).getCategory();
        if (viewType.equals("data")){
            View itemView = mLayoutInflater.inflate(R.layout.list_row_main, container, false);
            ImageView imageView = itemView.findViewById(R.id.imageView);
            TextView tv = itemView.findViewById(R.id.name);
            tv.setText(sliderItems.get(position).getTitle());
            CircularProgressDrawable drawable = new CircularProgressDrawable(context);
            drawable.setColorSchemeColors(R.color.black, R.color.black, R.color.black);
            drawable.setCenterRadius(30f);
            drawable.setStrokeWidth(5f);
            // set all other properties as you would see fit and start it
            drawable.start();
            Log.e("checkme", "instantiateItem: "+sliderItems.get(position).getImgUrl()+" "+sliderItems.get(position).getThumn() );
            Glide.with(context)
                    .load(sliderItems.get(position).getImgUrl())
//                        .load("https://images.pexels.com/photos/2246478/pexels-photo-2246478.jpeg")
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .thumbnail(Glide.with(context)
                            .load(sliderItems.get(position).getThumn())
                            .centerCrop())
                    .error(context.getDrawable(R.drawable.ic_launcher_background))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
            container.addView(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context,FullNewsActivity.class);
                    intent.putExtra("imgurl",sliderItems.get(position).getImgUrl());
                    intent.putExtra("title",sliderItems.get(position).getTitle());
                    context.startActivity(intent);
                }
            });

            return itemView;
        }
        else if (viewType.equals("fullscreenad")){
            View itemView = mLayoutInflater.inflate(R.layout.list_row_ad, container, false);
            ImageView imageView = itemView.findViewById(R.id.imageView);
            Glide.with(context)
                    .load(sliderItems.get(position).getImgUrl())
//                        .load("https://images.pexels.com/photos/2246478/pexels-photo-2246478.jpeg")
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .thumbnail(Glide.with(context)
                            .load(sliderItems.get(position).getThumn())
                            .centerCrop())
                    .error(context.getDrawable(R.drawable.ic_launcher_background))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
            container.addView(itemView);
            return itemView;
        }
     return null;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}