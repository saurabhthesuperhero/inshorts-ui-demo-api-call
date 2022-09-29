package com.fourrooms.inshortsapp.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fourrooms.inshortsapp.R;
import com.fourrooms.inshortsapp.model.LocalTechModelRoom;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_DATA = 0;
    public static final int TYPE_PROGRESS = 1;
    private ArrayList<LocalTechModelRoom> dataArrayList;
    private Activity activity;
    private OnClickListener onClickListener;

    public MainAdapter(ArrayList<LocalTechModelRoom> dataArrayList, Activity activity, OnClickListener onClickListener) {
        this.dataArrayList = dataArrayList;
        this.activity = activity;
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onRowClick(int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //creatio of inflater separately
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // based on viewtype viewholder is used
        if (viewType == TYPE_DATA) {
            // data view holder for items
            return new DataViewHolder(inflater.inflate(R.layout.list_row_main, parent, false));
        } else {
            //progress view holder for progress
            return new ProgressViewHolder(inflater.inflate(R.layout.progressbar, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof DataViewHolder) {

            try {
                CircularProgressDrawable drawable = new CircularProgressDrawable(activity);
                drawable.setColorSchemeColors(R.color.black, R.color.black, R.color.black);
                drawable.setCenterRadius(30f);
                drawable.setStrokeWidth(5f);
                // set all other properties as you would see fit and start it
                drawable.start();

                Log.e("checkme", "onBindViewHolder: " + dataArrayList.get(position).getImgUrl());
                Glide.with(activity)
                        .load(dataArrayList.get(position).getImgUrl())
//                        .load("https://picsum.photos/id/237/200/300")
                        .centerCrop()
                        .error(activity.getDrawable(R.drawable.ic_launcher_background))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(drawable)
                        .into(((DataViewHolder) holder).imageView);

                ((DataViewHolder) holder).name.setText(dataArrayList.get(position).getTitle());
            } catch (Exception e) {
                Log.e("checkme", "onBindViewHolder: " + e.getMessage());
            }

//            holder.itemView.setOnClickListener(v -> {
//
//
////                Intent intent = new Intent(this.activity, ImageActivity.class);
////                Bundle args = new Bundle();
////                args.putSerializable("ARRAYLIST",(Serializable)dataArrayList);
////                intent.putExtra("BUNDLE",args);
////                intent.putExtra("imageLoc",position);
////                activity.startActivity(intent);
//
//            });
        }
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // overriden item view type and checking if category is data
        // so basically this logic will return type of data i.e to show progress bar or item
        if (dataArrayList.get(position) != null && dataArrayList.get(position).getCategory() != null) {
            if (dataArrayList.get(position).getCategory().equals("data")) {
                return TYPE_DATA;
            } else {
                return TYPE_PROGRESS;
            }
        }
        return TYPE_DATA;
    }

    public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView name;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    onClickListener.onRowClick(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {
            onClickListener.onRowClick(getAdapterPosition());
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
