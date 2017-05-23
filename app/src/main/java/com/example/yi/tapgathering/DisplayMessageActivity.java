package com.example.yi.tapgathering;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DisplayMessageActivity extends AppCompatActivity{

    gridAdapter adapter;

    String filename;

    DataHelper dh;

    // data to populate the RecyclerView with
    final static String[] data = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};

    int [] count = {0,0,0,0,0,0,0,0,0};

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dh = DataHelper.getInstance();
        filename = getIntent().getStringExtra("filename");
        setContentView(R.layout.activity_display_message);
      // set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvNumbers);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new gridAdapter(this, data,count);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position, float x, float y, long etime) {
                count[position] += 1;
                adapter.notifyDataSetChanged();
                try{
                    dh.write_to_file((System.currentTimeMillis()/100)+" " +x+" "+y+" "+ (position+1) + "\n");
                    Log.i("TAG", "X" + x + " Y" + y);

                }catch (Exception e){
                    e.printStackTrace();
                }
                //Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
                //Log.i("TAG", "X" + x + " Y" + y);

            }

           // @Override
           // public void onLongClick(View view, int position, float x, float y) {
                //Toast.makeText(adapter.getItem(position).getTitle() + " is long pressed!", Toast.LENGTH_SHORT).show();

           // }
        }));

    }


   // @Override
   public void onItemClick(View view, int position) {
       // Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
        //Log.d("TAG", "Screen X: " +positionX);
        //count[position] += 1;
        //adapter.notifyDataSetChanged();
    }


/* @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
          //  Log.i("Touch coordinates : ", String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
        }
        return false;
    }*/
}
