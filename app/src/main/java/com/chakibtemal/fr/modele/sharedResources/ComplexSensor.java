package com.chakibtemal.fr.androidproject;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class ComplexSensor  {
    private Sensor sensor = null;
    private boolean isSelected = false;
    private boolean isAvailable = false;
    private DataForNextActivity dataOfSensor;



    ComplexSensor(SensorManager sensorManager, int type){
        this.sensor = sensorManager.getDefaultSensor(type);
        this.dataOfSensor = new DataForNextActivity();
        this.dataOfSensor.setName(this.sensor.getName());
        this.dataOfSensor.setType(this.sensor.getType());

    }

    public DataForNextActivity getDataOfSensor() {
        return dataOfSensor;
    }

    public void setDataOfSensor(DataForNextActivity dataOfSensor) {
        this.dataOfSensor = dataOfSensor;
    }


    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }


    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
