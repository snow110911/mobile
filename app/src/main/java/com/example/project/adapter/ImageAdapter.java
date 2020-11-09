package com.example.project.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.utils.SizeUtil;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Uri> mData;
    private final int mCountLimit = 9;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onTakePhotoClick();
        void onItemLongClick(View view, int position);
        void onAddButtonClick();
    }

    public ImageAdapter(Context context,List<Uri> data,OnItemClickListener onItemClickListener) {
        this.mData = data;
        this.mContext = context;
        this.mOnItemClickListener = onItemClickListener;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(SizeUtil.dip2px(parent.getContext(), 115), SizeUtil.dip2px(parent.getContext(), 115));
        params.setMargins(5, 5, 5, 5);
        params.gravity = Gravity.CENTER;
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ImageViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        if (position == getItemCount() - 1 && mData.size() < mCountLimit) {
            holder.imageView.setImageResource(R.mipmap.add_image);
            if(getItemCount()==1){
                holder.imageView.setVisibility(View.GONE);
            }
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onAddButtonClick();
                }
            });
        } else {
            holder.imageView.setImageURI(mData.get(position));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onTakePhotoClick();
                }
            });
            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(v, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null && mData.size() >= mCountLimit) {
            return mCountLimit;
        } else {
            return mData == null ? 1 : mData.size() + 1;
        }
    }
    static class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        public ImageViewHolder(@NonNull View View) {
            super(View);
            imageView = (ImageView) View;
        }
    }
}
