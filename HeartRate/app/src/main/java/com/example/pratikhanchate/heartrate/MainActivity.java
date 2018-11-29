package com.example.pratikhanchate.heartrate;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private TextView mTextView,mTextAcc;
    SensorManager mSensorManager;
    Sensor mHeartRateSensor,mAccelerometer,mGyro;

    SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mTextView = (TextView) findViewById(R.id.value_hr);
        mTextAcc=(TextView) findViewById(R.id.value_acc);

        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mAccelerometer=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyro=mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mGyro,SensorManager.SENSOR_DELAY_NORMAL);


        mSensorManager.registerListener(sensorEventListener, mHeartRateSensor, mSensorManager.SENSOR_DELAY_FASTEST);


        Log.d(TAG, "LISTENERS REGISTERED.");

      //  mTextView.setText("Test");



        // Enables Always-on
      //  setAmbientEnabled();
    }

    public void onResume(){
        super.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Log.d("DEBUGXX","Sensor changed");

        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            String msg = "" + (int)event.values[0];
            mTextView.setText(msg);
            Log.d(TAG, msg);
        }
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            Log.d(TAG, "ACcelerometer activated");

            mTextAcc.setText(
                    "x = " + Float.toString(event.values[0]) + "\n" +
                            "y = " + Float.toString(event.values[1]) + "\n" +
                            "z = " + Float.toString(event.values[2]) + "\n"
            );
        }

        if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
            Log.d(TAG,"Gyro Activated");
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged - accuracy: " + accuracy);
    }
}
