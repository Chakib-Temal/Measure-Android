package com.chakibtemal.fr.androidproject;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected ListView listSensors = null;
    protected Button buttonRun = null;
    protected ArrayAdapter<ComplexSensor> adapter;


    protected SensorManager sensorManager = null;
    protected ComplexSensor accelerometer = null;
    protected ComplexSensor gyroscope = null;
    protected ComplexSensor aproximity = null;


    //to display available sensor
    protected List<ComplexSensor> availableSensors = new ArrayList<ComplexSensor>();
    //send to next Activity of corse
    protected List<ComplexSensor> selectedSensors = new ArrayList<ComplexSensor>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Configuration for Sensors
         */
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = new ComplexSensor(sensorManager, Sensor.TYPE_ACCELEROMETER);
        this.gyroscope = new ComplexSensor(sensorManager, Sensor.TYPE_GYROSCOPE);
        this.aproximity = new ComplexSensor(sensorManager, Sensor.TYPE_PROXIMITY);
        this.buttonRun = (Button) findViewById(R.id.start);




        /**
         *
         here we recover the availability of sensors with static variable(ValidatorSensor),
         finally we can use ComplexSensor to find the state of the sensor itself
         */

        ValidatorSensor.returnResults(this.accelerometer, this.gyroscope, this.aproximity, this.availableSensors);



        /**
         * Adapter for the View
         */
        adapter = new SensorAdapter(this, 0, this.availableSensors);
        listSensors = (ListView) findViewById(R.id.listSensor);
        listSensors.setAdapter(adapter);







        /**
         * Events on ListView
         */

        listSensors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                ComplexSensor actualSensor = availableSensors.get(i);

                if (!actualSensor.isSelectedInListView()){

                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    actualSensor.setSelectedInListView(true);
                    actualSensor.setSelected(true);
                    //set frequency
                    actualSensor.setFrequency(0.02);


                    if (actualSensor.getSensor().getName() == new ComplexSensor(sensorManager, Sensor.TYPE_ACCELEROMETER).getSensor().getName()){
                        TextView  nameSensor = (TextView)view.findViewById(R.id.nameSensor);
                        nameSensor.setText("change !!!");
                    }


                    selectedSensors.add(actualSensor);


                }else {
                    view.setBackgroundColor(getResources().getColor(R.color.colorBlank));
                    actualSensor.setSelectedInListView(false);
                    actualSensor.setSelected(false);
                    selectedSensors.remove(actualSensor);
                }


                if (selectedSensors.isEmpty()){
                    System.out.println("Empty List");
                }else {
                    for (ComplexSensor sensor : selectedSensors){
                        System.out.println(sensor.getSensor().getName());
                    }
                }
            }
        });

    }


    /**
     * Events on Button Run
     */
    public void onClickRun(View view) {

        Intent i = new Intent(this, RunSensorsActivity.class);

        startActivity(i);

    }


    /**
     * Class for the Adapter
     */
    private class SensorAdapter extends ArrayAdapter<ComplexSensor>{

        public SensorAdapter(@NonNull Context context, int resource, @NonNull List<ComplexSensor> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.sensor_item, null);

            TextView nameSensor = (TextView) root.findViewById(R.id.nameSensor);
            ComplexSensor sensor = getItem(position);

            /**
             * Dynamic Rows
             */
            if (sensor.getSensor().getName() == new ComplexSensor(sensorManager, Sensor.TYPE_ACCELEROMETER).getSensor().getName()){
               // Specification for View of Accelero
                nameSensor.setText(sensor.getSensor().getName());
            }else {
                nameSensor.setText(sensor.getSensor().getName());
            }




            return root;
        }
    }


}
