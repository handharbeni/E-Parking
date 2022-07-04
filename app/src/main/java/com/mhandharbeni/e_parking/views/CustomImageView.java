package com.mhandharbeni.e_parking.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

@SuppressLint("AppCompatCustomView")
public class CustomImageView extends AppCompatImageView {

    public CustomImageView(@NonNull Context context) {
        super(context);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        Drawable drawable = getDrawable();
        if (drawable != null)
        {
            //get imageview width
            int width =  MeasureSpec.getSize(widthMeasureSpec);


            int diw = drawable.getIntrinsicWidth();
            int dih = drawable.getIntrinsicHeight();
            float ratio = (float)diw/dih; //get image aspect ratio

            int height = (int) (width * ratio);

            //don't let height exceed width
            if (height > width){
                height = width;
            }


            setMeasuredDimension(width, height);
        }
        else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
