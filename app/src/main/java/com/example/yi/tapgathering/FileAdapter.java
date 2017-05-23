package com.example.yi.tapgathering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yi on 3/31/17.
 */
public class FileAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mDataSource;

    private OnClickListener clickListener;

    public FileAdapter(Context context, ArrayList<String> items) {
        mContext = context;
        mDataSource = items;
        this.clickListener = clickListener;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item, parent, false);

        // Get delelte element
        TextView delete_file =
                (TextView) rowView.findViewById(R.id.delete_file);

        // Get filename element
        TextView filename =
                (TextView) rowView.findViewById(R.id.filename);

        // Get send element
        TextView send_file =
                (TextView) rowView.findViewById(R.id.send_file);

        filename.setText((String) getItem(position));

        delete_file.setTag(position);
        delete_file.setOnClickListener(Delete_file);
        send_file.setTag(position);
        send_file.setOnClickListener(Send_file);

        return rowView;
    }


    // allows clicks events to be caught
    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface OnClickListener {
        // void onItemClick(View view, int position);
        void Delete_event(int index);

        void Send_event(int index);
    }

    public View.OnClickListener Delete_file = new View.OnClickListener() {
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            clickListener.Delete_event(position);
        }
    };

    public View.OnClickListener Send_file = new View.OnClickListener() {
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            clickListener.Send_event(position);
        }
    };


}
