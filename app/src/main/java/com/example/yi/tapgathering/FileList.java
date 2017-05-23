package com.example.yi.tapgathering;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by yi on 3/31/17.
 */
public class FileList extends AppCompatActivity implements FileAdapter.OnClickListener {

    private ListView mListView;
    ArrayList<String> filelist;
    FileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
        filelist = retreivefiles();
        adapter = new FileAdapter(this,filelist);
        adapter.setClickListener(this);
        setContentView(R.layout.file_list);
        mListView = (ListView) findViewById(R.id.file_list_view);
        mListView.setAdapter(adapter);
        final Context context = this;
    }


    //get filenames from the sdcard
    public ArrayList<String> retreivefiles(){
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(root.toString(),"/sensordata/");
        File list[] = file.listFiles();
        ArrayList<String> filenames = new ArrayList<String>();
        for(File f:list)
        {
            filenames.add(file.toString()+"/"+f.getName());//add new files name in the list
        }
        return filenames;

    }


    @Override
    public void Delete_event(int index) {
        String selectedfile = filelist.get(index);

        File file = new File(selectedfile);
        if( file.delete()) {
            filelist.remove(index);
            adapter.notifyDataSetChanged();
        };
    }

    @Override
    public void Send_event(int index) {
        String selectedfile=filelist.get(index);
        File filelocation = new File(selectedfile);
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");
        String to[] = {"toy041126@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Training_Data");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }
}
