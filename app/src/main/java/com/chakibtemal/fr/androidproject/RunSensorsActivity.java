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
import android.widget.ViewFlipper;

import com.chakibtemal.fr.modele.drawingGraphs.DrawGraphHelper;
import com.chakibtemal.fr.modele.service.Services;
import com.chakibtemal.fr.modele.sharedResources.ComplexSensor;
import com.chakibtemal.fr.modele.sharedResources.DataForNextActivity;
import com.chakibtemal.fr.modele.sharedResources.RunMode;
import com.chakibtemal.fr.modele.sqliteDb.Solar;
import com.chakibtemal.fr.modele.sqliteDb.SolarBdd;
import com.chakibtemal.fr.modele.valuesSensorModel.ValueOfSensor;

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

    private Services services = new Services();

    //prepare table of sensor :
    private ValueOfSensor [] accelerometerValues;
    private int compterIndexAccelerometer = 0;
    private ValueOfSensor [] gyroscopeValues;
    private int compterIndexGyroscope = 0;
    private ValueOfSensor [] proximityValues;
    private int compterIndexProximity = 0;

    private Button buttonStop = null;
    private Button buttonSave = null;
    private Button buttonDrawGraphs = null;
    private LinearLayout body = null;

    private boolean stopAllRuning = true;

    private View activity_run_sensors, activity_graph_drawing;
    private ViewFlipper viewFlipperGraphs;
    private List<View> listGraphView = new ArrayList<View>();

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        this.activity_run_sensors   = getLayoutInflater().inflate(R.layout.activity_run_sensors, null);
        this.activity_graph_drawing = getLayoutInflater().inflate(R.layout.activity_graph_drawing, null);
        setContentView(activity_run_sensors);
        this.context = this;
        /**
         * Receive List<SimfliedSensor> and RunMode configuration
         */
        Bundle bundle = getIntent().getExtras();
        this.selectedSensors  = bundle.getParcelableArrayList("data");
        this.configuration = bundle.getParcelable("configuration");

        this.runMode          = configuration.getNameMode();
        this.body             = (LinearLayout) activity_run_sensors.findViewById(R.id.body);
        this.buttonSave       = (Button) activity_run_sensors.findViewById(R.id.saveInBase);
        this.buttonStop       = (Button) activity_run_sensors.findViewById(R.id.stopRuning);
        this.buttonDrawGraphs = (Button) activity_run_sensors.findViewById(R.id.goToDrawGraphs);

        body.removeView(buttonSave);
        body.removeView(buttonDrawGraphs);

        this.necessaryIndex   = services.getNecessaryIndex(this.runMode, this, this.configuration);

        try { System.out.println("voici la configuration choisit : Mode :" + configuration.getNameMode() + " /   " + " et l'index necessaire est  " + configuration.getNecessaryIndex() + " / ");
        }catch (Exception e ){ e.getStackTrace(); }

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        for(DataForNextActivity actualSimplifiedSensor: selectedSensors){
            this.mySensors.add(new ComplexSensor(sensorManager, actualSimplifiedSensor.getType()));
            this.mySensors.get(mySensors.size() - 1).getDataOfSensor().setFrequency(actualSimplifiedSensor.getFrequency());
            //System.out.println("le capteur  " + actualSimplifiedSensor.getName() + " et ca frequence est   :" + actualSimplifiedSensor.getFrequency());
        }
        this.initializArrays();

        adapter = new SensorAdapter(this, 0, this.mySensors);
        listSensors = (ListView) findViewById(R.id.listWorkSensors);
        listSensors.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!stopAllRuning){

        }else {
            buttonStop.setText(R.string.stop);
            stopAllRuning = true;
            Double frequency = new Double(0);
            for (ComplexSensor sensor : this.mySensors) {
                frequency = sensor.getDataOfSensor().getFrequency();
                int type = sensor.getSensor().getType();
                if (type == Sensor.TYPE_ACCELEROMETER) {
                    sensorManager.registerListener(acceleroEvenetListner, sensor.getSensor(), frequency.intValue(), 100000000);
                } else if (type == Sensor.TYPE_GYROSCOPE) {
                    sensorManager.registerListener(gyrosCopeEventListner, sensor.getSensor(), frequency.intValue(), 100000000);
                } else if (type == Sensor.TYPE_PROXIMITY) {
                    sensorManager.registerListener(proximityEventListner, sensor.getSensor(), frequency.intValue(), 100000000);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAllRuning = false;
        this.stopSensors();
        buttonStop.setText(R.string.waiting);
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
     * GO TO THE GRAPHSDRAWING ACTIVITY
     */
    public void onClickDrawGraphs(View view) {
        setContentView(activity_graph_drawing);
        this.viewFlipperGraphs   = (ViewFlipper) activity_graph_drawing.findViewById(R.id.viewFlipperGraphs);
        Button previousButton = (Button) activity_graph_drawing.findViewById(R.id.previousGraph);
        Button nextButton = (Button) activity_graph_drawing.findViewById(R.id.nextGraph);

        viewFlipperGraphs.removeAllViews();
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipperGraphs.showPrevious();

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipperGraphs.showNext();
            }
        });

        DrawGraphHelper drawGraphHelper = new DrawGraphHelper(this);
        if (accelerometerValues != null){
           drawGraphHelper.prepareGraphe(compterIndexAccelerometer, accelerometerValues, getResources().getString(R.string.ACCELEROMETER), 3, listGraphView);
        }
        if(gyroscopeValues != null){
            drawGraphHelper.prepareGraphe(compterIndexGyroscope, gyroscopeValues, getResources().getString(R.string.GYROSCOPE), 3, listGraphView);
        }

        if (proximityValues != null){
            drawGraphHelper.prepareGraphe(compterIndexProximity, proximityValues,getResources().getString(R.string.PROXIMITY), 1, listGraphView );
        }

        drawGraphHelper.putViewChildFlliper(listGraphView,  viewFlipperGraphs);
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
            TextView valueX     = (TextView) root.findViewById(R.id.valueX);
            TextView valueY     = (TextView) root.findViewById(R.id.valueY);
            TextView valueZ     = (TextView) root.findViewById(R.id.valueZ);
            ComplexSensor sensor = getItem(position);
            int type = sensor.getSensor().getType();

            if (type == Sensor.TYPE_ACCELEROMETER){
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf("X = " + valuesOfAccelerometer[0]));
                valueY.setText(String.valueOf("Y = " + valuesOfAccelerometer[1]));
                valueZ.setText(String.valueOf("Z = " + valuesOfAccelerometer[2]));
            } else if (type == Sensor.TYPE_GYROSCOPE){
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf("X = " + valuesOfGyroscope[0]));
                valueY.setText(String.valueOf("Y = " + valuesOfGyroscope[1]));
                valueZ.setText(String.valueOf("Z = " + valuesOfGyroscope[2]));

            }else if(type == Sensor.TYPE_PROXIMITY) {
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf("X = " +valuesOfProximity[0]));
                valueY.setText(String.valueOf("Y = 0" ));
                valueZ.setText(String.valueOf("Z = 0" ));
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
        final Context context = getApplicationContext();
        new Thread(new Runnable() {
            @Override
            public void run() {
                saveData(new SolarBdd(context));
            }
        }).start();
    }

    /**
     * Stop Sensors
     */
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

        if (stopAllRuning){
            try {
                body.removeView(buttonStop);
                body.addView(buttonSave);
                body.addView(buttonDrawGraphs);
            }catch (Exception e){
                e.getStackTrace();
            }
        }else {
            stopAllRuning = true;
        }
    }


    /**
     * Save Data in BD
     */
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
                sensorBdd.insertActualSensor(solar);
            }
        }

        if (gyroscopeValues != null){
            for (int i=0 ; i < compterIndexGyroscope; i++){
                solar.setName(getResources().getString(R.string.GYROSCOPE));
                solar.setValueX(gyroscopeValues[i].getValues()[0]);
                solar.setValueY(gyroscopeValues[i].getValues()[1]);
                solar.setValueZ(gyroscopeValues[i].getValues()[2]);
                solar.setTime(gyroscopeValues[i].getTime());
                sensorBdd.insertActualSensor(solar);
            }
        }

        if (proximityValues != null){
            for (int i=0 ; i < compterIndexProximity ; i++){
                solar.setName(getResources().getString(R.string.PROXIMITY));
                solar.setValueX(proximityValues[i].getValues()[0]);
                solar.setValueY(0);
                solar.setValueZ(0);
                solar.setTime(proximityValues[i].getTime());
                sensorBdd.insertActualSensor(solar);
            }
        }
        sensorBdd.close();
    }

    public void initializArrays(){
        for (ComplexSensor sensor : this.mySensors){
            int type = sensor.getSensor().getType();
            if (type == Sensor.TYPE_ACCELEROMETER){
                this.accelerometerValues = new ValueOfSensor[this.necessaryIndex];
            }else if (type == Sensor.TYPE_GYROSCOPE){
                this.gyroscopeValues = new ValueOfSensor[this.necessaryIndex];
            }else if(type == Sensor.TYPE_PROXIMITY){
                this.proximityValues = new ValueOfSensor[this.necessaryIndex];
            }
        }
    }
}
