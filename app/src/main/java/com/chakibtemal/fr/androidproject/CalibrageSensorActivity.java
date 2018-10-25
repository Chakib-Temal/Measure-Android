package com.chakibtemal.fr.androidproject;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.chakibtemal.fr.modele.SharedPreferencesHelper.SharedPreferencesHelper;
import com.chakibtemal.fr.modele.notification.Alert;

public class CalibrageSensorActivity extends AppCompatActivity implements SensorEventListener {

    private int levelSpeedCompter = 0;
    private int [] inverseModeSpeed = {3,2,1,0};
    private long [] resultsOfCalibrage = {0,0,0,0};
    private int frequecyCompter = 0;
    private Sensor sensor1;
    private int sampleNumber = 10;
    private long startTime ;
    private SensorManager sensorManager = null;
    private SharedPreferencesHelper preference;
    private ConstraintLayout body;

    private Button startButton;
    private ProgressBar progressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrage_sensor);
        this.sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        this.preference = new SharedPreferencesHelper(this);
        this.body = (ConstraintLayout) findViewById(R.id.body);
        this.startButton = (Button) findViewById(R.id.starting);
        this.progressBar = (ProgressBar) findViewById(R.id.timeCalibration);
        this.body.removeView(progressBar);
    }

    public void calibrateTimeSensors(View view) {
        body.removeView(startButton);
        body.addView(progressBar);
        this.sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER );
        sensorManager.registerListener(this, sensor1 , SensorManager.SENSOR_DELAY_NORMAL, 100000000 );
        startTime = System.nanoTime();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        frequecyCompter++;
        if (frequecyCompter == sampleNumber){
            resultsOfCalibrage[levelSpeedCompter] = ((System.nanoTime() - startTime) / sampleNumber);
            sensorManager.unregisterListener(this, sensor1);
            frequecyCompter = 0;
            levelSpeedCompter++;
            if (levelSpeedCompter <= 3){
                sensorManager.registerListener(this, sensor1 , inverseModeSpeed[levelSpeedCompter], 100000000 );
                startTime = System.nanoTime();
            }
            if (levelSpeedCompter == 4){
                preference.editor.putBoolean("alreadyCalibred", true);
                preference.editor.putLong("normalMode", resultsOfCalibrage[0]);
                preference.editor.putLong("uiMode", resultsOfCalibrage[1]);
                preference.editor.putLong("gameMode", resultsOfCalibrage[2]);
                preference.editor.putLong("fastestMode", resultsOfCalibrage[3]);
                preference.editor.commit();
                levelSpeedCompter = 0;
                body.removeView(progressBar);
                Alert alert  = new Alert(this, R.string.alert, getResources().getString(R.string.AlertMessage));
                alert.showAlert();
                body.addView(startButton);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    /**
     * if we leave the application we have to stop the sensor
     */
    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this, sensor1);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        SharedPreferencesHelper preferences  = new SharedPreferencesHelper(this);
        if (!preferences.preferences.getBoolean("alreadyCalibred", false)){
            Alert alert  = new Alert(this, R.string.warning, getResources().getString(R.string.warningMessage));
            alert.showAlert();
            onResume();
        }else {
            Intent intent = new Intent(CalibrageSensorActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
