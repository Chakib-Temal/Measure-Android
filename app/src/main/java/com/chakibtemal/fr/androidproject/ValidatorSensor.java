package com.chakibtemal.fr.androidproject;

import java.util.List;

public class ValidatorSensor {

    public static boolean accelerometerIsAvailable = false;
    public static boolean gyroscopeIsAvailable = false;
    public static boolean aproximityIsAvailable = false;


    public static void returnResults(ComplexSensor accelerometer, ComplexSensor gyroscope, ComplexSensor aproximity , List<ComplexSensor> mySensors) {

        if (accelerometer.getSensor() != null){
            ValidatorSensor.accelerometerIsAvailable = true;
            accelerometer.setAvailable(true);
            mySensors.add(accelerometer);
        }

        if (gyroscope.getSensor() != null) {
            ValidatorSensor.gyroscopeIsAvailable = true;
            gyroscope.setAvailable(true);
            mySensors.add(gyroscope);
        }

        if (aproximity.getSensor() != null){
            ValidatorSensor.aproximityIsAvailable = true;
            aproximity.setAvailable(true);
            mySensors.add(aproximity);
        }



    }


}
