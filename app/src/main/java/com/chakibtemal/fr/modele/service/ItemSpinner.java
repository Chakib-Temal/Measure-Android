package com.chakibtemal.fr.modele.service;

public class ItemSpinner {
    private Double frequency = new Double(0);
    private String nameFrequency;


    public ItemSpinner(Double frequency, String nameFrequency) {
        this.frequency = frequency;
        this.nameFrequency = nameFrequency;
    }

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    public String getNameFrequency() {
        return nameFrequency;
    }

    public void setNameFrequency(String nameFrequency) {
        this.nameFrequency = nameFrequency;
    }

    @Override
    public String toString() {
        return this.nameFrequency;
    }
}
