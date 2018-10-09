package com.chakibtemal.fr.modele.validator;

import com.chakibtemal.fr.modele.sharedResources.ComplexSensor;

import java.util.List;

public class ValidatorSensor {

    public static void returnResults(ComplexSensor accelerometer, ComplexSensor gyroscope, ComplexSensor aproximity , List<ComplexSensor> mySensors) {
        if (accelerometer.getSensor() != null){
            accelerometer.setAvailable(true);
            mySensors.add(accelerometer);
        }
        if (gyroscope.getSensor() != null) {
            gyroscope.setAvailable(true);
            mySensors.add(gyroscope);
        }
        if (aproximity.getSensor() != null){
            aproximity.setAvailable(true);
            mySensors.add(aproximity);
        }
    }
}
