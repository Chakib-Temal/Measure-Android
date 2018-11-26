package com.chakibtemal.fr.modele.sharedResources;

public class DataModels {
    private double frequency ;
    private String name ;
    private int type;
    private int id_runMode;

    private String nameMode;
    private int necessaryIndex;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId_runMode() {
        return id_runMode;
    }

    public void setId_runMode(int id_runMode) {
        this.id_runMode = id_runMode;
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
}
