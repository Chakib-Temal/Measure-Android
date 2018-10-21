package com.chakibtemal.fr.modele.sharedResources;

import android.os.Parcel;
import android.os.Parcelable;

public class RunMode implements Parcelable {
    private String nameMode;
    private int necessaryIndex;




    public RunMode(String nameMode, int necessaryIndex ) {
        this.nameMode = nameMode;
        this.necessaryIndex = necessaryIndex;

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




    protected RunMode(Parcel in) {
        nameMode = in.readString();
        necessaryIndex = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameMode);
        dest.writeInt(necessaryIndex);
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