package com.example.yi.tapgathering;

import android.app.Service;
import android.content.Intent;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SensorService extends Service implements SensorEventListener{

    final static String MY_ACTION = "MY_ACTION";

    //default filename
    public String filename;

    // three sensors that we used
    Sensor acc= null;
    Sensor gro = null;
    Sensor rot = null;

    // SensorManager to register sensors
    SensorManager SM = null;

    // data stream process tool
    FileOutputStream myFileOut;
    OutputStreamWriter myOutWriter;
    BufferedWriter myBufferedWriter;
    PrintWriter myPrintWriter;


    //parameters we need when store data.
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private long currentTime;

    // intermediate data structure needed to transfer rotData to angData
    private float [] rmatrix = new float[9];
    private float [] tempmatrix = new float[9];

    float[] accData = new float[3];
    float[] groData = new float[3];
    float[] rotData = new float[3];
    float[] angData = new float[3];

    // temporay data
    float[] temp = new float[3];

    //construct function
    public SensorService() {

    }

    // what to do when we created
    @Override
    public void onCreate(){
        Toast.makeText(this, "service Created",Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onSensorChanged(SensorEvent event){

        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            accData[0] = event.values[0];
            accData[1] = event.values[1];
            accData[2] = event.values[2];
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            groData[0] = event.values[0];
            groData[1] = event.values[1];
            groData[2] = event.values[2];
        } else if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){

            temp[0] = event.values[0];
            temp[1] = event.values[1];
            temp[2] = event.values[2];
            // calculate the pitch and roll angle
            SM.getRotationMatrixFromVector(tempmatrix,temp);
            SM.remapCoordinateSystem(tempmatrix,SM.AXIS_X,SM.AXIS_Y,rmatrix);
            SM.getOrientation(rmatrix,rotData);

            angData[0] = (float)Math.toDegrees(rotData[0]);
            angData[1] = (float)Math.toDegrees(rotData[1]);
            angData[2] = (float)Math.toDegrees(rotData[2]);
        }

        //currentTime = event.timestamp;

        myPrintWriter.write((System.currentTimeMillis()/100)+" "+accData[0]+" "+accData[1]+" "+accData[2]+" "+groData[0]+" "+groData[1]+" "+groData[2]+" "+angData[0]+" "+angData[1]+" "+angData[2]+"\n");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    // start the service and create the file
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){

        filename = intent.getExtras().getString("filename");
            try {

                File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File dir =  new File(root.toString(),"/sensordata/");
                if(!dir.exists()) dir.mkdir();
                File file = new File(dir.toString()+"/"+filename+".txt");

                myFileOut = new FileOutputStream(file);
                myOutWriter = new OutputStreamWriter(myFileOut);
                myBufferedWriter = new BufferedWriter(myOutWriter);
                myPrintWriter = new PrintWriter(myBufferedWriter);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        SM = (SensorManager)getSystemService(SENSOR_SERVICE);

        //register sensors
        acc = SM.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        gro = SM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        rot = SM.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        SM.registerListener(this,acc,SensorManager.SENSOR_DELAY_FASTEST);
        SM.registerListener(this,gro,SensorManager.SENSOR_DELAY_FASTEST);
        SM.registerListener(this,rot,SensorManager.SENSOR_DELAY_FASTEST );
        return Service.START_STICKY;
    }

    //unregister sensors and stop service
    @Override
    public void onDestroy(){
        Toast.makeText(this, "service Stop",Toast.LENGTH_LONG).show();
        try{
            myPrintWriter.close();
            myBufferedWriter.close();
            myOutWriter.close();
            myFileOut.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        SM.unregisterListener(this);
        stopSelf();
    }



}
