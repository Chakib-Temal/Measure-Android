package com.chakibtemal.fr.modele.sqliteDb;

public class Solar {
    private int id;
    private String name;
    private float valueX;
    private float valueY;
    private float valueZ;
    private long time;

    public Solar(){
        this.name = "";
        this.valueX = 0;
        this.valueY = 0;
        this.valueZ = 0;
        this.time = 0;
    }

    public Solar (String name, float valueX, float valueY, float valueZ, long time) {
        this.name = name;
        this.valueX = valueX;
        this.valueY = valueY;
        this.valueZ = valueZ;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValueX() {
        return valueX;
    }

    public void setValueX(float valueX) {
        this.valueX = valueX;
    }

    public float getValueY() {
        return valueY;
    }

    public void setValueY(float valueY) {
        this.valueY = valueY;
    }

    public float getValueZ() {
        return valueZ;
    }

    public void setValueZ(float valueZ) {
        this.valueZ = valueZ;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
