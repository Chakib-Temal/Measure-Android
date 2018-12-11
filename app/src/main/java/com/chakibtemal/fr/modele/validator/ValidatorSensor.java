package com.chakibtemal.fr.modele.validator;

import com.chakibtemal.fr.modele.sharedResources.ComplexSensor;

import java.util.List;

public class ValidatorSensor {

    public static void returnResults(List<ComplexSensor> beforeValidationOfAvailaible ,List<ComplexSensor> mySensors) {
        for (ComplexSensor sensor : beforeValidationOfAvailaible){
            if (sensor.getSensor() != null){
                sensor.setAvailable(true);
                mySensors.add(sensor);
            }
        }
    }
}
