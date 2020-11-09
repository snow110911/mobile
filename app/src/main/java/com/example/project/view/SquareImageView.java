package com.example.project.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

public class SquareImageView extends androidx.appcompat.widget.AppCompatImageView {

    public SquareImageView(@NonNull Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
