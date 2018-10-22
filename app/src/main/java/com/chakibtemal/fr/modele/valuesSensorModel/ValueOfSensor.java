package com.chakibtemal.fr.modele.valuesSensorModel;

public class ValueOfSensor {
    private float [] values = {0,0,0};
    private long time;



    public ValueOfSensor(float [] values, long time){
        this.values = new float[3];
        this.values[0] = 0;
        this.values[1] = 0;
        this.values[2] = 0;

        try{
            this.values[0] = values[0];
        }catch (Exception e){
            e.getStackTrace();
        }

        try{
            this.values[1] = values[1];
        }catch (Exception e){
            e.getStackTrace();
        }

        try{
            this.values[2] = values[2];
        }catch (Exception e){
            e.getStackTrace();
        }

        this.time = time;
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
