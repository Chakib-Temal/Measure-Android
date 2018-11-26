package com.chakibtemal.fr.modele.sharedResources;

import android.hardware.SensorManager;
import android.os.Parcel;
import android.os.Parcelable;

public class DataForNextActivity implements Parcelable {
    private double frequency ;
    private String name ;
    private int type;
    private int id_runMode;


    public DataForNextActivity(){
        frequency =  SensorManager.SENSOR_DELAY_NORMAL;
        name = " ";
        type = 0;
        this.id_runMode = 0;
    }
    public DataForNextActivity(Parcel in) {
       frequency =  in.readDouble();
       name = in.readString();
       type = in.readInt();
       id_runMode = in.readInt();
    }

    public int getId_runMode() {
        return id_runMode;
    }

    public void setId_runMode(int id_runMode) {
        this.id_runMode = id_runMode;
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
    public String toString() {
        return "DataForNextActivity{" +
                "frequency=" + frequency +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", id_runMode=" + id_runMode +
                '}';
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
        parcel.writeInt(id_runMode);
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
