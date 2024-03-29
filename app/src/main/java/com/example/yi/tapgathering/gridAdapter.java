package com.example.yi.tapgathering;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class gridAdapter extends RecyclerView.Adapter<gridAdapter.ViewHolder>{

    private String[] mData = new String[0];
    private  int [] count = new int[0];
    private LayoutInflater mInflater;
    
   // private ItemClickListener mClickListener;

    // data is passed into the constructor
    public gridAdapter(Context context, String[] data,int[] count ) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.count = count;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData[position];
        holder.myTextView.setText(animal);

        String tapCount =  String.valueOf(count[position]);
        holder.count.setText(tapCount);

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }

    // stores and recycles views as they are scrolled off screen
   // public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public class ViewHolder extends RecyclerView.ViewHolder{
    public TextView myTextView;
        public  TextView count;

        public ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.info_text);
            count = (TextView) itemView.findViewById(R.id.count);
            //itemView.setOnClickListener(this);
        }

       // @Override
       // public void onClick(View view) {
         //  if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
       // }

    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
   // public void setClickListener(ItemClickListener itemClickListener) {
    //    this.mClickListener = itemClickListener;
  //  }


    // parent activity will implement this method to respond to click events
   // public interface ItemClickListener {
   //     void onItemClick(View view, int position);
   // }
}