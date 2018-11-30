package com.chakibtemal.fr.modele.valuesSensorModel;

import android.os.Parcel;
import android.os.Parcelable;

public class ValueOfSensor implements Parcelable {
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

    protected ValueOfSensor(Parcel in) {
        time = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ValueOfSensor> CREATOR = new Parcelable.Creator<ValueOfSensor>() {
        @Override
        public ValueOfSensor createFromParcel(Parcel in) {
            return new ValueOfSensor(in);
        }

        @Override
        public ValueOfSensor[] newArray(int size) {
            return new ValueOfSensor[size];
        }
    };
}