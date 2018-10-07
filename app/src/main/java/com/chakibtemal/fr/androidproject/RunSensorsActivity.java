package com.chakibtemal.fr.androidproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class RunSensorsActivity extends AppCompatActivity {

    List<DataForNextActivity> selectedSensors = new ArrayList<DataForNextActivity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_sensors);


        Bundle bundle = getIntent().getExtras();
        selectedSensors  = bundle.getParcelableArrayList("data");


    }

    public void onClickRun(View view) {
        for(DataForNextActivity actualSimplifiedSensor: selectedSensors){
            System.out.println("List Selectionnée : ");
            try{
                System.out.println(actualSimplifiedSensor.getName());
            }catch (Exception e){
                e.getStackTrace();
            }

        }
    }
}
