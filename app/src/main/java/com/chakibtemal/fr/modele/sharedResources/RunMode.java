package com.chakibtemal.fr.modele.sharedResources;

import android.os.Parcel;
import android.os.Parcelable;

public class RunMode implements Parcelable {
    private String nameMode;
    private int necessaryIndex;
    private String nameSensorCommand;
    private int frequency;



    public RunMode(String nameMode, int necessaryIndex, String nameSensorCommand, int frequency) {
        this.nameMode = nameMode;
        this.necessaryIndex = necessaryIndex;
        this.nameSensorCommand = nameSensorCommand;
        this.frequency = frequency;

    }

    public String getNameMode() {
        return nameMode;
    }

    public void setNameMode(String nameMode) {
        this.nameMode = nameMode;
    }

    public int getNecessaryIndex() {
        return necessaryIndex;
    }

    public void setNecessaryIndex(int necessaryIndex) {
        this.necessaryIndex = necessaryIndex;
    }

    public String getNameSensorCommand() {
        return nameSensorCommand;
    }

    public void setNameSensorCommand(String nameSensorCommand) {
        this.nameSensorCommand = nameSensorCommand;
    }
    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    protected RunMode(Parcel in) {
        nameMode = in.readString();
        necessaryIndex = in.readInt();
        nameSensorCommand = in.readString();
        frequency = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameMode);
        dest.writeInt(necessaryIndex);
        dest.writeString(nameSensorCommand);
        dest.writeInt(frequency);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RunMode> CREATOR = new Parcelable.Creator<RunMode>() {
        @Override
        public RunMode createFromParcel(Parcel in) {
            return new RunMode(in);
        }

        @Override
        public RunMode[] newArray(int size) {
            return new RunMode[size];
        }
    };
}