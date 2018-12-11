package com.chakibtemal.fr.modele.service;

import android.hardware.SensorManager;

import com.chakibtemal.fr.modele.sharedResources.ComplexSensor;
import com.chakibtemal.fr.modele.sharedResources.DataForNextActivity;

import java.util.List;

public class ServiceHelper {

    public void addSelectedSensors(List<DataForNextActivity> selectedSensors , SensorManager sensorManager, List<ComplexSensor> mySensors  ){
        for(DataForNextActivity actualSimplifiedSensor: selectedSensors){
            mySensors.add(new ComplexSensor(sensorManager, actualSimplifiedSensor.getType()));
            mySensors.get(mySensors.size() - 1).getDataOfSensor().setFrequency(actualSimplifiedSensor.getFrequency());
            //System.out.println("le capteur  " + actualSimplifiedSensor.getName() + " et ca frequence est   :" + actualSimplifiedSensor.getFrequency());
        }
    }

}
