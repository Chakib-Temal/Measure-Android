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
import com.chakibtemal.fr.modele.service.ServiceHelper;
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
    protected float [] valuesOfPhotometer = {0, 0, 0};
    protected float [] valuesOfAmbiant_temperature = {0, 0, 0};
    protected float [] valuesOfGravity = {0, 0, 0};
    protected float [] valuesOfLinear_acceleration = {0, 0, 0};
    protected float [] valuesOfMagnetic_field = {0, 0, 0};
    protected float [] valuesOfOrientation = {0, 0, 0};
    protected float [] valuesOfPressure = {0, 0, 0};
    protected float [] valuesOfRelative_humidity = {0, 0, 0};
    protected float [] valuesOfRotation_vector = {0, 0, 0};
    protected float [] valuesOfTemperature = {0, 0, 0};

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
    private ValueOfSensor [] photomerValues;
    private int compterIndexPhometer = 0;

    private ValueOfSensor [] ambiant_temperatureValues;
    private int compterIndexAmbiant_temperature = 0;
    private ValueOfSensor [] gravityValues;
    private int compterIndexGravity = 0;
    private ValueOfSensor [] linear_accelerationValues;
    private int compterIndexLinear_acceleration= 0;
    private ValueOfSensor [] magnetic_fieldValues;
    private int compterIndexMagnetic_field = 0;
    private ValueOfSensor [] orientationValues;
    private int compterIndexOrientation = 0;
    private ValueOfSensor [] pressureValues;
    private int compterIndexPressure = 0;
    private ValueOfSensor [] relative_humidityValues;
    private int compterIndexRelative_humidity = 0;
    private ValueOfSensor [] rotation_vectorValues;
    private int compterIndexRotation_vector = 0;
    private ValueOfSensor [] temperatureValues;
    private int compterIndexTemperature = 0;


    private Button buttonStop = null;
    private Button buttonSave = null;
    private Button buttonDrawGraphs = null;
    private LinearLayout body = null;

    private boolean stopAllRuning = true;

    private View activity_run_sensors, activity_graph_drawing;
    private ViewFlipper viewFlipperGraphs;
    private List<View> listGraphView = new ArrayList<View>();

    private Context context;
    private ServiceHelper serviceHelper = new ServiceHelper();
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


        serviceHelper.addSelectedSensors(selectedSensors, sensorManager, mySensors);
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
                } else if (type == Sensor.TYPE_LIGHT) {
                    sensorManager.registerListener(photometerEventListner, sensor.getSensor(), frequency.intValue(), 100000000);
                }else if(type == Sensor.TYPE_AMBIENT_TEMPERATURE){
                    sensorManager.registerListener(ambiant_temperatureEvenetListner, sensor.getSensor(), frequency.intValue(), 100000000);
                }else if(type == Sensor.TYPE_GRAVITY){
                    sensorManager.registerListener(gravityEvenetListner, sensor.getSensor(), frequency.intValue(), 100000000);
                }else if(type == Sensor.TYPE_LINEAR_ACCELERATION){
                    sensorManager.registerListener(linear_accelerationEvenetListner, sensor.getSensor(), frequency.intValue(), 100000000);
                }else if(type == Sensor.TYPE_MAGNETIC_FIELD){
                    sensorManager.registerListener(magnetic_fieldEvenetListner, sensor.getSensor(), frequency.intValue(), 100000000);
                }else if(type == Sensor.TYPE_ORIENTATION){
                    sensorManager.registerListener(orientationEvenetListner, sensor.getSensor(), frequency.intValue(), 100000000);
                }else if(type == Sensor.TYPE_PRESSURE){
                    sensorManager.registerListener(pressureEvenetListner, sensor.getSensor(), frequency.intValue(), 100000000);
                }else if(type == Sensor.TYPE_RELATIVE_HUMIDITY){
                    sensorManager.registerListener(relative_humidityEvenetListner, sensor.getSensor(), frequency.intValue(), 100000000);
                }else if(type == Sensor.TYPE_ROTATION_VECTOR){
                    sensorManager.registerListener(rotation_vectorEvenetListner, sensor.getSensor(), frequency.intValue(), 100000000);
                }else if(type == Sensor.TYPE_TEMPERATURE){
                    sensorManager.registerListener(temperatureEvenetListner, sensor.getSensor(), frequency.intValue(), 100000000);
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

    final SensorEventListener photometerEventListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            photomerValues[compterIndexPhometer] = new ValueOfSensor(sensorEvent.values, sensorEvent.timestamp);
            valuesOfPhotometer = sensorEvent.values;
            adapter.notifyDataSetChanged();
            if(compterIndexPhometer == (necessaryIndex - 1)){
                stopSensors();
            }
            compterIndexPhometer++;
        }
    };

    final SensorEventListener ambiant_temperatureEvenetListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            ambiant_temperatureValues[compterIndexAmbiant_temperature] = new ValueOfSensor(sensorEvent.values, sensorEvent.timestamp) ;
            valuesOfAmbiant_temperature = sensorEvent.values;
            adapter.notifyDataSetChanged();
            if(compterIndexAmbiant_temperature == (necessaryIndex - 1)){
                stopSensors();
            }
            compterIndexAmbiant_temperature++;
        }
    };

    final SensorEventListener gravityEvenetListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            gravityValues[compterIndexGravity] = new ValueOfSensor(sensorEvent.values, sensorEvent.timestamp) ;
            valuesOfGravity = sensorEvent.values;
            adapter.notifyDataSetChanged();
            if(compterIndexGravity == (necessaryIndex - 1)){
                stopSensors();
            }
            compterIndexGravity++;
        }
    };

    final SensorEventListener linear_accelerationEvenetListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            linear_accelerationValues[compterIndexLinear_acceleration] = new ValueOfSensor(sensorEvent.values, sensorEvent.timestamp) ;
            valuesOfLinear_acceleration = sensorEvent.values;
            adapter.notifyDataSetChanged();
            if(compterIndexLinear_acceleration == (necessaryIndex - 1)){
                stopSensors();
            }
            compterIndexLinear_acceleration++;
        }
    };

    final SensorEventListener magnetic_fieldEvenetListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            magnetic_fieldValues[compterIndexMagnetic_field] = new ValueOfSensor(sensorEvent.values, sensorEvent.timestamp) ;
            valuesOfMagnetic_field = sensorEvent.values;
            adapter.notifyDataSetChanged();
            if(compterIndexMagnetic_field == (necessaryIndex - 1)){
                stopSensors();
            }
            compterIndexMagnetic_field++;
        }
    };

    final SensorEventListener orientationEvenetListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            orientationValues[compterIndexOrientation] = new ValueOfSensor(sensorEvent.values, sensorEvent.timestamp) ;
            valuesOfOrientation = sensorEvent.values;
            adapter.notifyDataSetChanged();
            if(compterIndexOrientation == (necessaryIndex - 1)){
                stopSensors();
            }
            compterIndexOrientation++;
        }
    };

    final SensorEventListener pressureEvenetListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            pressureValues[compterIndexPressure] = new ValueOfSensor(sensorEvent.values, sensorEvent.timestamp) ;
            valuesOfPressure = sensorEvent.values;
            adapter.notifyDataSetChanged();
            if(compterIndexPressure == (necessaryIndex - 1)){
                stopSensors();
            }
            compterIndexPressure++;
        }
    };

    final SensorEventListener relative_humidityEvenetListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            relative_humidityValues[compterIndexRelative_humidity] = new ValueOfSensor(sensorEvent.values, sensorEvent.timestamp) ;
            valuesOfRelative_humidity = sensorEvent.values;
            adapter.notifyDataSetChanged();
            if(compterIndexRelative_humidity == (necessaryIndex - 1)){
                stopSensors();
            }
            compterIndexRelative_humidity++;
        }
    };

    final SensorEventListener rotation_vectorEvenetListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            rotation_vectorValues[compterIndexRotation_vector] = new ValueOfSensor(sensorEvent.values, sensorEvent.timestamp) ;
            valuesOfRotation_vector = sensorEvent.values;
            adapter.notifyDataSetChanged();
            if(compterIndexRotation_vector == (necessaryIndex - 1)){
                stopSensors();
            }
            compterIndexRotation_vector++;
        }
    };

    final SensorEventListener temperatureEvenetListner = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

        public void onSensorChanged(SensorEvent sensorEvent) {
            temperatureValues[compterIndexTemperature] = new ValueOfSensor(sensorEvent.values, sensorEvent.timestamp) ;
            valuesOfTemperature = sensorEvent.values;
            adapter.notifyDataSetChanged();
            if(compterIndexTemperature == (necessaryIndex - 1)){
                stopSensors();
            }
            compterIndexTemperature++;
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

        if (photomerValues != null){
            drawGraphHelper.prepareGraphe(compterIndexPhometer, photomerValues,getResources().getString(R.string.PHOTOMETER), 1, listGraphView );
        }

        if (ambiant_temperatureValues != null){
            drawGraphHelper.prepareGraphe(compterIndexAmbiant_temperature, ambiant_temperatureValues,getResources().getString(R.string.AMBIENT_TEMPERATURE), 1, listGraphView );
        }

        if (gravityValues != null){
            drawGraphHelper.prepareGraphe(compterIndexGravity, gravityValues,getResources().getString(R.string.GRAVITY), 3, listGraphView );
        }

        if (linear_accelerationValues != null){
            drawGraphHelper.prepareGraphe(compterIndexLinear_acceleration, linear_accelerationValues,getResources().getString(R.string.LINEAR_ACCELERATION), 3, listGraphView );
        }

        if (magnetic_fieldValues != null){
            drawGraphHelper.prepareGraphe(compterIndexMagnetic_field, magnetic_fieldValues,getResources().getString(R.string.MAGNETIC_FIELD), 3, listGraphView );
        }

        if (orientationValues != null){
            drawGraphHelper.prepareGraphe(compterIndexOrientation, orientationValues,getResources().getString(R.string.ORIENTATION), 3, listGraphView );
        }

        if (pressureValues != null){
            drawGraphHelper.prepareGraphe(compterIndexPressure, pressureValues,getResources().getString(R.string.PRESSURE), 1, listGraphView );
        }

        if (relative_humidityValues != null){
            drawGraphHelper.prepareGraphe(compterIndexRelative_humidity, relative_humidityValues,getResources().getString(R.string.RELATIVE_HUMIDITY), 1, listGraphView );
        }

        if (rotation_vectorValues != null){
            drawGraphHelper.prepareGraphe(compterIndexRotation_vector, rotation_vectorValues,getResources().getString(R.string.ROTATION_VECTOR), 3, listGraphView );
        }

        if (temperatureValues != null){
            drawGraphHelper.prepareGraphe(compterIndexTemperature, temperatureValues,getResources().getString(R.string.TEMPERATURE), 1, listGraphView );
        }

        drawGraphHelper.putViewChildFlliper(listGraphView,  viewFlipperGraphs);
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
            }else if (type == Sensor.TYPE_LIGHT){
                sensorManager.unregisterListener(photometerEventListner, sensor.getSensor());
            }else if(type == Sensor.TYPE_AMBIENT_TEMPERATURE){
                sensorManager.unregisterListener(ambiant_temperatureEvenetListner, sensor.getSensor());
            }else if(type == Sensor.TYPE_GRAVITY){
                sensorManager.unregisterListener(gravityEvenetListner, sensor.getSensor());
            }else if(type == Sensor.TYPE_LINEAR_ACCELERATION){
                sensorManager.unregisterListener(linear_accelerationEvenetListner, sensor.getSensor());
            }else if(type == Sensor.TYPE_MAGNETIC_FIELD){
                sensorManager.unregisterListener(magnetic_fieldEvenetListner, sensor.getSensor());
            }else if(type == Sensor.TYPE_ORIENTATION){
                sensorManager.unregisterListener(orientationEvenetListner, sensor.getSensor());
            }else if(type == Sensor.TYPE_PRESSURE){
                sensorManager.unregisterListener(pressureEvenetListner, sensor.getSensor());
            }else if(type == Sensor.TYPE_RELATIVE_HUMIDITY){
                sensorManager.unregisterListener(relative_humidityEvenetListner, sensor.getSensor());
            }else if(type == Sensor.TYPE_ROTATION_VECTOR){
                sensorManager.unregisterListener(rotation_vectorEvenetListner, sensor.getSensor());
            }else if(type == Sensor.TYPE_TEMPERATURE){
                sensorManager.unregisterListener(temperatureEvenetListner, sensor.getSensor());
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

        if (gravityValues != null){
            for (int i=0 ; i < compterIndexGravity ; i++){
                solar.setName(getResources().getString(R.string.GRAVITY));
                solar.setValueX(gravityValues[i].getValues()[0]);
                solar.setValueY(gravityValues[i].getValues()[1]);
                solar.setValueZ(gravityValues[i].getValues()[2]);
                solar.setTime(gravityValues[i].getTime());
                sensorBdd.insertActualSensor(solar);
            }
        }

        if (linear_accelerationValues != null){
            for (int i=0 ; i < compterIndexLinear_acceleration ; i++){
                solar.setName(getResources().getString(R.string.LINEAR_ACCELERATION));
                solar.setValueX(linear_accelerationValues[i].getValues()[0]);
                solar.setValueY(linear_accelerationValues[i].getValues()[1]);
                solar.setValueZ(linear_accelerationValues[i].getValues()[2]);
                solar.setTime(linear_accelerationValues[i].getTime());
                sensorBdd.insertActualSensor(solar);
            }
        }

        if (magnetic_fieldValues != null){
            for (int i=0 ; i < compterIndexMagnetic_field ; i++){
                solar.setName(getResources().getString(R.string.MAGNETIC_FIELD));
                solar.setValueX(magnetic_fieldValues[i].getValues()[0]);
                solar.setValueY(magnetic_fieldValues[i].getValues()[1]);
                solar.setValueZ(magnetic_fieldValues[i].getValues()[2]);
                solar.setTime(magnetic_fieldValues[i].getTime());
                sensorBdd.insertActualSensor(solar);
            }
        }

        if (orientationValues != null){
            for (int i=0 ; i < compterIndexOrientation ; i++){
                solar.setName(getResources().getString(R.string.ORIENTATION));
                solar.setValueX(orientationValues[i].getValues()[0]);
                solar.setValueY(orientationValues[i].getValues()[1]);
                solar.setValueZ(orientationValues[i].getValues()[2]);
                solar.setTime(orientationValues[i].getTime());
                sensorBdd.insertActualSensor(solar);
            }
        }

        if (photomerValues != null){
            for (int i=0 ; i < compterIndexPhometer ; i++){
                solar.setName(getResources().getString(R.string.PHOTOMETER));
                solar.setValueX(photomerValues[i].getValues()[0]);
                solar.setValueY(0);
                solar.setValueZ(0);
                solar.setTime(photomerValues[i].getTime());
                sensorBdd.insertActualSensor(solar);
            }
        }

        if (pressureValues != null){
            for (int i=0 ; i < compterIndexPressure ; i++){
                solar.setName(getResources().getString(R.string.PRESSURE));
                solar.setValueX(pressureValues[i].getValues()[0]);
                solar.setValueY(0);
                solar.setValueZ(0);
                solar.setTime(pressureValues[i].getTime());
                sensorBdd.insertActualSensor(solar);
            }
        }

        if (relative_humidityValues != null){
            for (int i=0 ; i < compterIndexRelative_humidity ; i++){
                solar.setName(getResources().getString(R.string.RELATIVE_HUMIDITY));
                solar.setValueX(relative_humidityValues[i].getValues()[0]);
                solar.setValueY(0);
                solar.setValueZ(0);
                solar.setTime(relative_humidityValues[i].getTime());
                sensorBdd.insertActualSensor(solar);
            }
        }

        if (rotation_vectorEvenetListner != null){
            for (int i=0 ; i < compterIndexRotation_vector ; i++){
                solar.setName(getResources().getString(R.string.ROTATION_VECTOR));
                solar.setValueX(rotation_vectorValues[i].getValues()[0]);
                solar.setValueY(0);
                solar.setValueZ(0);
                solar.setTime(rotation_vectorValues[i].getTime());
                sensorBdd.insertActualSensor(solar);
            }
        }

        if (ambiant_temperatureValues != null){
            for (int i=0 ; i < compterIndexAmbiant_temperature ; i++){
                solar.setName(getResources().getString(R.string.AMBIENT_TEMPERATURE));
                solar.setValueX(ambiant_temperatureValues[i].getValues()[0]);
                solar.setValueY(0);
                solar.setValueZ(0);
                solar.setTime(ambiant_temperatureValues[i].getTime());
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
            }else if(type == Sensor.TYPE_LIGHT){
                this.photomerValues = new ValueOfSensor[this.necessaryIndex];
            }else if(type == Sensor.TYPE_AMBIENT_TEMPERATURE){
                this.ambiant_temperatureValues = new ValueOfSensor[this.necessaryIndex];
            }else if(type == Sensor.TYPE_GRAVITY){
                this.gravityValues = new ValueOfSensor[this.necessaryIndex];
            }else if(type == Sensor.TYPE_LINEAR_ACCELERATION){
                this.linear_accelerationValues = new ValueOfSensor[this.necessaryIndex];
            }else if(type == Sensor.TYPE_MAGNETIC_FIELD){
                this.magnetic_fieldValues = new ValueOfSensor[this.necessaryIndex];
            }else if(type == Sensor.TYPE_ORIENTATION){
                this.orientationValues = new ValueOfSensor[this.necessaryIndex];
            }else if(type == Sensor.TYPE_PRESSURE){
                this.pressureValues = new ValueOfSensor[this.necessaryIndex];
            }else if(type == Sensor.TYPE_RELATIVE_HUMIDITY){
                this.relative_humidityValues = new ValueOfSensor[this.necessaryIndex];
            }else if(type == Sensor.TYPE_ROTATION_VECTOR){
                this.rotation_vectorValues = new ValueOfSensor[this.necessaryIndex];
            }else if(type == Sensor.TYPE_TEMPERATURE){
                this.temperatureValues = new ValueOfSensor[this.necessaryIndex];
            }
        }
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

            }else if(type == Sensor.TYPE_GRAVITY) {
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf("X = " + valuesOfGravity[0]));
                valueY.setText(String.valueOf("Y = " + valuesOfGravity[1]));
                valueZ.setText(String.valueOf("Z = " + valuesOfGravity[2]));

            } else if(type == Sensor.TYPE_LINEAR_ACCELERATION) {
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf("X = " + valuesOfLinear_acceleration[0]));
                valueY.setText(String.valueOf("Y = " + valuesOfLinear_acceleration[1]));
                valueZ.setText(String.valueOf("Z = " + valuesOfLinear_acceleration[2]));

            }else if(type == Sensor.TYPE_MAGNETIC_FIELD) {
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf("X = " + valuesOfMagnetic_field[0]));
                valueY.setText(String.valueOf("Y = " + valuesOfMagnetic_field[1]));
                valueZ.setText(String.valueOf("Z = " + valuesOfMagnetic_field[2]));

            }else if(type == Sensor.TYPE_ORIENTATION) {
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf("X = " + valuesOfOrientation[0]));
                valueY.setText(String.valueOf("Y = " + valuesOfOrientation[1]));
                valueZ.setText(String.valueOf("Z = " + valuesOfOrientation[2]));

            }else if(type == Sensor.TYPE_PRESSURE) {
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf("X = " + valuesOfPressure[0]));
                valueY.setText(String.valueOf("Y = 0"));
                valueZ.setText(String.valueOf("Z = 0"));

            }else if(type == Sensor.TYPE_LIGHT) {
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf("X = " + valuesOfPhotometer[0]));
                valueY.setText(String.valueOf("Y = 0"));
                valueZ.setText(String.valueOf("Z = 0"));

            }else if(type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf("X = " + valuesOfTemperature[0]));
                valueY.setText(String.valueOf("Y = 0"));
                valueZ.setText(String.valueOf("Z = 0"));

            }else if(type == Sensor.TYPE_RELATIVE_HUMIDITY) {
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf("X = " + valuesOfRelative_humidity[0]));
                valueY.setText(String.valueOf("Y = 0"));
                valueZ.setText(String.valueOf("Z = 0"));

            }else if(type == Sensor.TYPE_ROTATION_VECTOR) {
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf("X = " + valuesOfRotation_vector[0]));
                valueY.setText(String.valueOf("Y = " + valuesOfRotation_vector[1]));
                valueZ.setText(String.valueOf("Z = " + valuesOfRotation_vector[2]));

            }else if(type == Sensor.TYPE_TEMPERATURE) {
                nameSensor.setText(sensor.getSensor().getName());
                valueX.setText(String.valueOf("X = " + valuesOfTemperature[0]));
                valueY.setText(String.valueOf("Y = 0"));
                valueZ.setText(String.valueOf("Z = 0"));
            }

            return root;
        }
    }
}

/*
add attribute     protected float [] valuesOfPhotometer = {0, 0, 0};
private ValueOfSensor [] photomerValues;
private int compterIndexPhometer = 0;

InitialArray
else if(type == Sensor.TYPE_LIGHT){
                this.photomerValues = new ValueOfSensor[this.necessaryIndex];
            }

add events
stop Sensors
getView adapter
saveData
onClickDrawGraphs
 */