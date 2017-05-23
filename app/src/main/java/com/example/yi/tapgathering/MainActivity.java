package com.example.yi.tapgathering;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    //this is test

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    public final static String EXTRA_MESSAGE = "com.yi.example.TapGathering.MESSAGE";
    private static final int REQUEST_WRITE_STORAGE = 1;

    DataHelper dh = DataHelper.getInstance();


    String filename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isExternalMediaAvailable()) {
            Log.d("sensorvalue", "avalaible");
            //Required by api 23 or above, check the dangerous permission, write and read data from sd card.
            boolean hasPermission =
                    (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission (this,  Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

            // if we do not have the permission, we need to request it.
            if(!hasPermission) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE },
                        REQUEST_WRITE_STORAGE);
            }}
        else Log.d("sensorvalue", "unavalaible");
    }

    public void start(View view){// called when user click on the start button
            // create intent to start sensor service, and pass it the filename.
            String CurrentTime =sdf.format(new Date());
            Intent intent = new Intent(this, SensorService.class);
            EditText editText = (EditText) findViewById(R.id.filename);
            filename = editText.getText().toString();
            intent.putExtra("filename",filename+CurrentTime);


        RadioButton tapapp = (RadioButton) findViewById(R.id.tapapp);

        RadioButton taplocation = (RadioButton) findViewById(R.id.taplocation);

           if(!check()){
               Intent to_grid_intent = new Intent(this, DisplayMessageActivity.class);
               to_grid_intent.putExtra("filename",filename+CurrentTime+"_real.txt");
               to_grid_intent.putExtra("tapapp", tapapp.isChecked());
               dh.create_file(filename+CurrentTime+"_real");
               startService(intent);
               if(taplocation.isChecked()) startActivity(to_grid_intent);
               else if(tapapp.isChecked()) moveTaskToBack(true);
           } else Toast.makeText(this, "service alreading running",Toast.LENGTH_LONG).show();


        //create intent to go to another activity, and pass it the filename

    }


    //handle the permisson request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] ==PackageManager.PERMISSION_GRANTED   )
                {
                    //create folder that we need to store the file
                    File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File dir = new File(root.toString(),"/sensordata/");
                    dir.mkdirs();
                    Toast.makeText(this, "The app was allowed to write to your storage. ", Toast.LENGTH_LONG).show();
                    //reload my activity with permission granted or use the features what required the permission
                } else
                {
                    Toast.makeText(this, "The app was not allowed to write to your storage. ", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    //stop the service
    public void stop(View view) {


        stopService(new Intent(this, SensorService.class));
        dh.close_file();
        Intent intent = new Intent(this,FileList.class);
        startActivity(intent);
    }

    // check if the external media available
    private boolean isExternalMediaAvailable(){
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    public boolean check(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if ("com.example.yi.tapgathering.SensorService".equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }
}
