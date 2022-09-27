package com.fourrooms.inshortsapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fourrooms.inshortsapp.R;
import com.fourrooms.inshortsapp.model.LocalTechModel;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    List<LocalTechModel> sliderItems;
    LayoutInflater mLayoutInflater;
    Context context;

    public ViewPagerAdapter(Context context, List<LocalTechModel> sliderItems) {
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
        View itemView = mLayoutInflater.inflate(R.layout.list_row_main, container, false);
        ImageView imageView = itemView.findViewById(R.id.imageView);
        TextView tv = itemView.findViewById(R.id.name);
        tv.setText(sliderItems.get(position).getTitle());
        Glide.with(context)
                .load(sliderItems.get(position).getImgUrl())
//                        .load("https://picsum.photos/id/237/200/300")
                .centerCrop()
                .apply(new RequestOptions().format(DecodeFormat.PREFER_RGB_565))
                .error(context.getDrawable(R.drawable.ic_launcher_background))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}