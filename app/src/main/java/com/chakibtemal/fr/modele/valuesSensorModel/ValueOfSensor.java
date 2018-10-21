package com.chakibtemal.fr.modele.valuesSensorModel;

public class ValueOfSensor {
    private float [] values;



    public ValueOfSensor(float [] values){
        this.values = new float[3];
        this.values = values;
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }



}
