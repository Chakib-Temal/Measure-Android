package com.chakibtemal.fr.modele.sharedResources;

import java.util.ArrayList;
import java.util.List;

public class AllDataForRunActivity  {
    private List<DataForNextActivity> dataForNextActivities = new ArrayList<DataForNextActivity>();
    private RunMode runMode;

    public AllDataForRunActivity(){

    }

    public AllDataForRunActivity(List<DataForNextActivity> dataForNextActivities, RunMode runMode) {
        this.dataForNextActivities = dataForNextActivities;
        this.runMode = runMode;
    }

    public List<DataForNextActivity> getDataForNextActivities() {
        return dataForNextActivities;
    }

    public void setDataForNextActivities(List<DataForNextActivity> dataForNextActivities) {
        this.dataForNextActivities = dataForNextActivities;
    }

    public RunMode getRunMode() {
        return runMode;
    }

    public void setRunMode(RunMode runMode) {
        this.runMode = runMode;
    }

    @Override
    public String toString() {
        String sensor = "";
        for (DataForNextActivity data : this.dataForNextActivities){
            sensor += data.getName() + " / " + data.getFrequency() + "/ " + data.getType();
        }
        return sensor;
    }
}
