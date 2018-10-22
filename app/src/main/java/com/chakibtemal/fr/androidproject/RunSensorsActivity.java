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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chakibtemal.fr.modele.sqliteDb.Solar;
import com.chakibtemal.fr.modele.sqliteDb.SolarBdd;
import com.chakibtemal.fr.modele.valuesSensorModel.ValueOfSensor;
import com.chakibtemal.fr.modele.sharedResources.ComplexSensor;
import com.chakibtemal.fr.modele.sharedResources.DataForNextActivity;
import com.chakibtemal.fr.modele.sharedResources.RunMode;

import java.util.ArrayList;
import java.util.List;

public class RunSensorsActivity extends AppCompatActivity {

    protected List<DataForNextActivity> selectedSensors = new ArrayList<DataForNextActivity>();
    protected SensorManager sensorManager = null;
    protected List<ComplexSensor> mySensors = new ArrayList<ComplexSensor>();
    protected ArrayAdapter<ComplexSensor> adapter;
    protected ListView listSensors = null;

    protected float [] valuesOfGyroscope = {0, 0, 0};
    protected float [] valuesOfAccelerometer = {0, 0, 0};
    protected float [] valuesOfProximity = {0, 0, 0};
    private RunMode configuration;

    private String runMode ="";
    private int necessaryIndex;



    //prepare table of sensor :
    private ValueOfSensor [] accelerometerValues;
    private int compterIndexAccelerometer = 0;
    private ValueOfSensor [] gyroscopeValues;
    private int compterIndexGyroscope = 0;
    private ValueOfSensor [] proximityValues;
    private int compterIndexProximity = 0;

    private Button buttonStop = null;
    private Button buttonSave = null;
    private LinearLayout body = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_sensors);
        /**
         * Receive List<SimfliedSensor> and RunMode configuration
         */
        Bundle bundle = getIntent().getExtras();
        this.selectedSensors  = bundle.getParcelableArrayList("data");
        this.configuration = bundle.getParcelable("configuration");

        this.runMode = configuration.getNameMode();
        this.body = (LinearLayout) findViewById(R.id.body);
        this.buttonSave = (Button) findViewById(R.id.saveInBase);
        this.buttonStop = (Button) findViewById(R.id.stopRuning);


        if (this.runMode.equals(getResources().getString(R.string.SAMPLE))){
            this.necessaryIndex = configuration.getNecessaryIndex();
            body.removeView(buttonSave);

        }else if (this.runMode.equals(getResources().getString(R.string.TIME))){
            this.necessaryIndex = configuration.getNecessaryIndex();
            body.removeView(buttonSave);
        }else if (this.runMode.equals(getResources().getString(R.string.UNLIMITED))){
            this.necessaryIndex =  1000000;
            body.removeView(buttonSave);
        }


        try {
            System.out.println("voici la configuration choisit : Mode :" + configuration.getNameMode() + " /   " + " et l'index necessaire est  " + configuration.getNecessaryIndex() + " / ");
        }catch (Exception e ){
            e.getStackTrace();
        }


        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        for(DataForNextActivity actualSimplifiedSensor: selectedSensors){
            this.mySensors.add(new ComplexSensor(sensorManager, actualSimplifiedSensor.getType()));
            this.mySensors.get(mySensors.size() - 1).getDataOfSensor().setFrequency(actualSimplifiedSensor.getFrequency());
            System.out.println("le capteur  " + actualSimplifiedSensor.getName() + " et ca frequence est   :" + actualSimplifiedSensor.getFrequency());
        }

        adapter = new SensorAdapter(this, 0, this.mySensors);
        listSensors = (ListView) findViewById(R.id.listWorkSensors);
        listSensors.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Double frequency = new Double(0);
        for (ComplexSensor sensor : this.mySensors){
            frequency = sensor.getDataOfSensor().getFrequency();
            int type = sensor.getSensor().getType();

            if (type == Sensor.TYPE_ACCELEROMETER){
                this.accelerometerValues = new ValueOfSensor[this.necessaryIndex];
                sensorManager.registerListener(acceleroEvenetListner, sensor.getSensor(),  frequency.intValue(), 100000000);
            }else if (type == Sensor.TYPE_GYROSCOPE){
                this.gyroscopeValues = new ValueOfSensor[this.necessaryIndex];
                sensorManager.registerListener(gyrosCopeEventListner, sensor.getSensor(),  frequency.intValue(), 100000000);
            }else if(type == Sensor.TYPE_PROXIMITY){
                this.proximityValues = new ValueOfSensor[this.necessaryIndex];
                sensorManager.registerListener(proximityEventListner, sensor.getSensor(), frequency.intValue(), 100000000);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.stopSensors();
    }

    final SensorEventListener acceleroEvenetListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            accelerometerValues[compterIndexAccelerometer] = new ValueOfSensor(sensorEvent.values, sensorEvent.timestamp) ;
            valuesOfAccelerometer = sensorEvent.values;
            adapter.notifyDataSetChanged();
            if(compterIndexAccelerometer == (necessaryIndex - 1)){
                stopSensors();
            }
            compterIndexAccelerometer++;
        }
    };

    final SensorEventListener gyrosCopeEventListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            gyroscopeValues[compterIndexGyroscope] = new ValueOfSensor(sensorEvent.values, sensorEvent.timestamp);
            valuesOfGyroscope = sensorEvent.values;
            adapter.notifyDataSetChanged();
            if(compterIndexGyroscope == (necessaryIndex - 1)){
                stopSensors();
            }
            compterIndexGyroscope++;
        }
    };

    final SensorEventListener proximityEventListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            proximityValues[compterIndexProximity] = new ValueOfSensor(sensorEvent.values, sensorEvent.timestamp);
            valuesOfProximity = sensorEvent.values;
            adapter.notifyDataSetChanged();
            if(compterIndexProximity == (necessaryIndex - 1)){
                stopSensors();
            }
            compterIndexProximity++;
        }
    };

    public void onClickStopRuning(View view) {
        stopSensors();
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


    public void onClickSaveButton(View view) {
        this.saveData(new SolarBdd(this));
    }

    public void stopSensors(){
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
        try {
            body.addView(buttonSave);
        }catch (Exception e){
            e.getStackTrace();
        }
       // System.out.println("compteur accelero : " + compterIndexAccelerometer + " : " + accelerometerValues.length + " /// gyro : " + compterIndexGyroscope +  " : " + gyroscopeValues.length + " /// proximity : " + compterIndexProximity+ " : " + proximityValues.length );
    }


    public void saveData(SolarBdd sensorBdd){
        Solar solar = new Solar();
        sensorBdd.open();

        if (accelerometerValues != null){
            for (int i=0 ; i < compterIndexAccelerometer ; i++){
                solar.setName(getResources().getString(R.string.ACCELEROMETER));
                solar.setValueX(accelerometerValues[i].getValues()[0]);
                solar.setValueY(accelerometerValues[i].getValues()[1]);
                solar.setValueZ(accelerometerValues[i].getValues()[2]);
                solar.setTime(accelerometerValues[i].getTime());
                sensorBdd.insertLivre(solar);
            }
        }

        if (valuesOfGyroscope != null){
            for (int i=0 ; i < compterIndexGyroscope; i++){
                solar.setName(getResources().getString(R.string.GYROSCOPE));
                solar.setValueX(gyroscopeValues[i].getValues()[0]);
                solar.setValueY(gyroscopeValues[i].getValues()[1]);
                solar.setValueZ(gyroscopeValues[i].getValues()[2]);
                solar.setTime(gyroscopeValues[i].getTime());
                sensorBdd.insertLivre(solar);
            }
        }

        if (valuesOfProximity != null){
            for (int i=0 ; i < compterIndexProximity ; i++){
                solar.setName(getResources().getString(R.string.PROXIMITY));
                solar.setValueX(5);
                solar.setValueX(proximityValues[i].getValues()[0]);
                solar.setValueY(0);
                solar.setValueZ(0);
                solar.setTime(proximityValues[i].getTime());
                sensorBdd.insertLivre(solar);
            }
        }
        sensorBdd.close();
    }
}
