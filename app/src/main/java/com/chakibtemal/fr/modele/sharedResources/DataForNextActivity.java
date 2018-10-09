package com.chakibtemal.fr.androidproject;

import android.hardware.SensorManager;
import android.os.Parcel;
import android.os.Parcelable;

public class DataForNextActivity implements Parcelable {
    private double frequency ;
    private String name ;
    private int type;



    public DataForNextActivity(){
        frequency =  SensorManager.SENSOR_DELAY_NORMAL;
        name = " ";
        type = 0;
    }
    public DataForNextActivity(Parcel in) {
       frequency =  in.readDouble();
       name = in.readString();
       type = in.readInt();

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
        parcel.writeInt(type);

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
