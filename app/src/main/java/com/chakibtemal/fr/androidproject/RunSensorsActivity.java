package com.chakibtemal.fr.androidproject;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RunSensorsActivity extends AppCompatActivity {

    private List<DataForNextActivity> selectedSensors = new ArrayList<DataForNextActivity>();

    protected SensorManager sensorManager = null;

    private List<ComplexSensor> mySensors = new ArrayList<ComplexSensor>();

    protected ArrayAdapter<ComplexSensor> adapter;

    protected ListView listSensors = null;


    protected float [] valuesOfGyroscope = {0, 0, 0};
    protected float [] valuesOfAccelerometer = {0, 0, 0};
    protected float [] valuesOfProximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_sensors);
        /**
         * Receive List<SimfliedSensor>
         */
        Bundle bundle = getIntent().getExtras();
        this.selectedSensors  = bundle.getParcelableArrayList("data");


        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);


        for(DataForNextActivity actualSimplifiedSensor: selectedSensors){
            this.mySensors.add(new ComplexSensor(sensorManager, actualSimplifiedSensor.getType()));
        }

        adapter = new SensorAdapter(this, 0, this.mySensors);
        listSensors = (ListView) findViewById(R.id.listWorkSensors);
        listSensors.setAdapter(adapter);


    }
    /*
    public void onClickRun(View view) {
        for(DataForNextActivity actualSimplifiedSensor: selectedSensors){
            System.out.println(actualSimplifiedSensor.getName() + " / " + actualSimplifiedSensor.getType());
        }

    }
    */

    @Override
    protected void onResume() {
        super.onResume();


        for (ComplexSensor sensor : this.mySensors){
            int type =sensor.getSensor().getType();

            if (type == Sensor.TYPE_ACCELEROMETER){
                sensorManager.registerListener(acceleroEvenetListner, sensor.getSensor(), (int) sensor.getDataOfSensor().getFrequency());
            }else if (type == Sensor.TYPE_GYROSCOPE){
                sensorManager.registerListener(gyrosCopeEventListner, sensor.getSensor(), (int) sensor.getDataOfSensor().getFrequency());
            }else if(type == Sensor.TYPE_PROXIMITY){
                sensorManager.registerListener(proximityEventListner, sensor.getSensor(), (int) sensor.getDataOfSensor().getFrequency());
            }
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        for (ComplexSensor sensor : this.mySensors){
            int type =sensor.getSensor().getType();

            if (type == Sensor.TYPE_ACCELEROMETER){
                sensorManager.unregisterListener(acceleroEvenetListner, sensor.getSensor());
            }else if (type == Sensor.TYPE_GYROSCOPE){
                sensorManager.unregisterListener(gyrosCopeEventListner, sensor.getSensor());
            }else if(type == Sensor.TYPE_PROXIMITY){
                sensorManager.unregisterListener(proximityEventListner, sensor.getSensor());
            }
        }





    }

    final SensorEventListener acceleroEvenetListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            valuesOfAccelerometer = sensorEvent.values;
            adapter.notifyDataSetChanged();
        }
    };

    final SensorEventListener gyrosCopeEventListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            valuesOfGyroscope = sensorEvent.values;
            adapter.notifyDataSetChanged();
        }
    };


    final SensorEventListener proximityEventListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            valuesOfProximity = sensorEvent.values;
            adapter.notifyDataSetChanged();
        }
    };

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
            View root = inflater.inflate(R.layout.sensorworkitem, null);

            TextView nameSensor = (TextView) root.findViewById(R.id.nameSensor);
            TextView valueX = (TextView) root.findViewById(R.id.valueX);
            TextView valueY = (TextView) root.findViewById(R.id.valueY);
            TextView valueZ = (TextView) root.findViewById(R.id.valueZ);
            ComplexSensor sensor = getItem(position);
            int type = sensor.getSensor().getType();

            if (type == Sensor.TYPE_ACCELEROMETER){
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf(valuesOfAccelerometer[0]));
                valueY.setText(String.valueOf(valuesOfAccelerometer[1]));
                valueZ.setText(String.valueOf(valuesOfAccelerometer[2]));
            } else if (type == Sensor.TYPE_GYROSCOPE){
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf(valuesOfGyroscope[0]));
                valueY.setText(String.valueOf(valuesOfGyroscope[1]));
                valueZ.setText(String.valueOf(valuesOfGyroscope[2]));
            }else if(type == Sensor.TYPE_PROXIMITY) {
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf(valuesOfProximity[0]));
                //valueY.setText(String.valueOf(valuesOfProximity[1]));
                //valueZ.setText(String.valueOf(valuesOfProximity[2]));
            }
            return root;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RunSensorsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
