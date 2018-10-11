package com.chakibtemal.fr.modele.calibrationSensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

import com.chakibtemal.fr.androidproject.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class CalibrateTime {

    private List<float[]> tableValuesForCalibration = new ArrayList<float[]>();
    private int sampleNumber = 10;
    private long startTime ;
    private SharedDatas sharedData = new SharedDatas();;
    private SensorManager sensorManager;
    private Sensor sensor;

    public class SharedDatas{
        public boolean data = false;

    }

    public void calibrateTimeForSensor(final Context context){
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(acceleroEvenetListner, sensor , SensorManager.SENSOR_DELAY_NORMAL);
        this.sharedData.data = false;


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            // Actions to do after 0 seconds
            public void run() {
                this.delayNormal();
                sensorManager.unregisterListener(acceleroEvenetListner, sensor);
            }

            public void delayNormal(){
                tableValuesForCalibration.clear();
                startTime = System.nanoTime();
                while (!sharedData.data){
                    //
                }
                long timeForOneShoot = (System.nanoTime() - startTime) / sampleNumber;
                MainActivity.timeForOneShoot = timeForOneShoot;
            }
        }, 0);
    }

    final SensorEventListener acceleroEvenetListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            tableValuesForCalibration.add(sensorEvent.values);
            if (tableValuesForCalibration.size() < sampleNumber){
                sharedData.data = true;
            }
        }
    };
}
