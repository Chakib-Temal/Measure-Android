package com.chakibtemal.fr.androidproject;

import android.hardware.SensorManager;
import android.os.Parcel;
import android.os.Parcelable;

public class DataForNextActivity implements Parcelable {
    private double frequency ;
    private String name ;


    public DataForNextActivity(){
        frequency =  SensorManager.SENSOR_DELAY_NORMAL;
        name = " ";

    }
    public DataForNextActivity(Parcel in) {
       frequency =  in.readDouble();
       name = in.readString();

    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(frequency);
        parcel.writeString(name);

    }

    public static final Parcelable.Creator<DataForNextActivity> CREATOR = new Parcelable.Creator<DataForNextActivity>()
    {
        public DataForNextActivity createFromParcel(Parcel in)
        {
            return new DataForNextActivity(in);
        }
        public DataForNextActivity[] newArray(int size)
        {
            return new DataForNextActivity[size];
        }
    };
}
