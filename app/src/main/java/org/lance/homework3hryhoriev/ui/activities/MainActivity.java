package org.lance.homework3hryhoriev.ui.activities;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.constraint.motion.MotionLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.lance.homework3hryhoriev.R;
import org.lance.homework3hryhoriev.ui.custom_views.CustomImageFilterView;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private MotionLayout mMainLayout;
    private CustomImageFilterView imgBall;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SensorEventListener mSensorListener;
    private float mMaxSensorRange;
    private float mCurrentSensorRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mMaxSensorRange = mSensor.getMaximumRange();
        mSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    mCurrentSensorRange = event.values[0];
                    Log.d(TAG, "onSensorChanged: Current range: " + mCurrentSensorRange);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        Log.d(TAG, "onCreate: sensorMaxRange: " + mMaxSensorRange);
        imgBall = findViewById(R.id.imgBall);
        mMainLayout = findViewById(R.id.mainLayout);
        imgBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "imgBall: onClick");
                if (mSensor == null) {
                    Toast.makeText(MainActivity.this, "No proximity sensor detected", Toast.LENGTH_SHORT).show();
                } else {
                    if (mMainLayout.getProgress() == 0.0) {
                        setResultBallImage(mCurrentSensorRange);
                        mMainLayout.transitionToEnd();
                    } else if (mMainLayout.getProgress() == 1.0) {
                        mMainLayout.transitionToStart();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mSensor != null){
            mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    private void setResultBallImage(float range) {
        if (range == 0) {
            imgBall.setAltDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.no_ball));
        } else {
            imgBall.setAltDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.yes_ball));
        }
    }
}
