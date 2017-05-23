package com.example.yi.tapgathering;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by yi on 4/11/17.
 */
public class DataHelper {
    // data stream process tool
    private  FileOutputStream myFileOut;
    private  OutputStreamWriter myOutWriter;
    private  BufferedWriter myBufferedWriter;
    private  PrintWriter myPrintWriter;

    private DataHelper(){
        if (SingletonHolder.INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    private static class SingletonHolder{
        private static final DataHelper INSTANCE = new DataHelper();
    }

    public static DataHelper getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public  boolean create_file(String filename){
        try {
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File dir =  new File(root.toString(),"/sensordata/");

            if(!dir.exists()) dir.mkdir();

            File file = new File(dir.toString()+"/"+filename+".txt");

            myFileOut = new FileOutputStream(file);
            myOutWriter = new OutputStreamWriter(myFileOut);
            myBufferedWriter = new BufferedWriter(myOutWriter);
            myPrintWriter = new PrintWriter(myBufferedWriter);

            return  true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public  void write_to_file(String data){
        Log.i("sdd",data);

        try{
         myPrintWriter.write(data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close_file(){
        try{
            myPrintWriter.close();
            myBufferedWriter.close();
            myOutWriter.close();
            myFileOut.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
