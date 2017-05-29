package com.jeff.alphamao;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by jeff on 17-5-29.
 */

public class MyImageView extends AppCompatImageView{
    private int index;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setIndex(int idx) {
        index = idx;
    }

    public int getIndex(){
        return index;
    }

}
