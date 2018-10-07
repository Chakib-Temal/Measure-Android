package com.chakibtemal.fr.androidproject;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class ComplexSensor  {
    private Sensor sensor = null;
    private double frequency = SensorManager.SENSOR_DELAY_NORMAL;
    private boolean isSelected = false;
    private boolean isAvailable = false;
    private boolean isSelectedInListView = false;




    ComplexSensor(SensorManager sensorManager, int type){
        this.sensor = sensorManager.getDefaultSensor(type);
    }
    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }
    public boolean isSelectedInListView() {
        return isSelectedInListView;
    }

    public void setSelectedInListView(boolean selectedInListView) {
        isSelectedInListView = selectedInListView;
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
