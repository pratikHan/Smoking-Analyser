package com.example.pratikhanchate.heartrate;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private static final float ROTATION_THRESHOLD = 2.0f;
    private static final int ROTATION_WAIT_TIME_MS = 100;
    private static final float SHAKE_THRESHOLD = 1.1f;
    private static final int SHAKE_WAIT_TIME_MS = 250;

    private long mRotationTime = 0;
    private long mShakeTime = 0;

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

      //  Log.d("DEBUGXX","Sensor changed");

        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            String msg = "" + (int)event.values[0];
            mTextView.setText(msg);
            Log.d("Heart Rate", msg);
        }
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            Log.d("SENSOR", "ACcelerometer activated");

            mTextAcc.setText(
                    "x = " + Float.toString(event.values[0]) + "\n" +
                            "y = " + Float.toString(event.values[1]) + "\n" +
                            "z = " + Float.toString(event.values[2]) + "\n"
            );

            Log.d("VALUES","x = " + Float.toString(event.values[0]) + "\n" +
                    "y = " + Float.toString(event.values[1]) + "\n" +
                    "z = " + Float.toString(event.values[2]) + "\n");
        }

        if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
            Log.d("SENSOR","Gyro Activated");
            detectRotation(event);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "onAccuracyChanged - accuracy: " + accuracy);
    }

    private void detectRotation(SensorEvent event) {
        long now = System.currentTimeMillis();

        if((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
            mRotationTime = now;

            // Change background color if rate of rotation around any
            // axis and in any direction exceeds threshold;
            // otherwise, reset the color
            if(Math.abs(event.values[0]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[1]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[2]) > ROTATION_THRESHOLD) {
              //  mView.setBackgroundColor(Color.rgb(0, 100, 0));
                setActivityBackgroundColor(1);
                Log.d("DEBUG","Rotation made");
            }
            else {
               setActivityBackgroundColor(0);
               Log.d("DEBUG","Flat");
            }
        }
    }

    private void detectShake(SensorEvent event) {
        long now = System.currentTimeMillis();

        if((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
            mShakeTime = now;

            float gX = event.values[0] / SensorManager.GRAVITY_EARTH;
            float gY = event.values[1] / SensorManager.GRAVITY_EARTH;
            float gZ = event.values[2] / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement
            float gForce = (float) Math.sqrt(gX*gX + gY*gY + gZ*gZ);

            // Change background color if gForce exceeds threshold;
            // otherwise, reset the color
            if(gForce > SHAKE_THRESHOLD) {
               // mView.setBackgroundColor(Color.rgb(0, 100, 0));
            }
            else {
               // mView.setBackgroundColor(Color.BLACK);
            }
        }
    }

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }
}
