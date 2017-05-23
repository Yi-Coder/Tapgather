package com.example.yi.tapgathering;

import android.view.View;

/**
 * Created by yi on 4/11/17.
 */
public interface RecyclerViewClickListener {
    void onClick(View view, int position, float x, float y, long etime);
    //void onLongClick(View view, int position,float x, float y);
}
