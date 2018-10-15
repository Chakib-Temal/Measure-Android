package com.chakibtemal.fr.androidproject;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.chakibtemal.fr.Adapter.BasicSpinnerAdapter;
import com.chakibtemal.fr.modele.SharedPreferencesHelper.SharedPreferencesHelper;
import com.chakibtemal.fr.modele.sharedResources.ComplexSensor;
import com.chakibtemal.fr.modele.sharedResources.DataForNextActivity;
import com.chakibtemal.fr.modele.sharedResources.RunMode;
import com.chakibtemal.fr.modele.validator.ValidatorSensor;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ListView listSensors = null;
    private Button goToCalibrageActivity = null;
    private LinearLayout body = null;
    private EditText sampledInput = null;
    private EditText timeInput = null;
    private RadioGroup choiceModeGroup = null;
    private Spinner choiceSensorforMode = null;
    private LinearLayout bodyChild = null;

    private SensorManager sensorManager = null;
    private ComplexSensor accelerometer = null;
    private ComplexSensor gyroscope = null;
    private ComplexSensor aproximity = null;

    //to display available sensor
    private List<ComplexSensor> availableSensors = new ArrayList<ComplexSensor>();
    private List<DataForNextActivity> dataForNextActivities = new ArrayList<DataForNextActivity>();

    private List<Double> itemSpinner = new ArrayList<Double>();
    private BasicSpinnerAdapter adapter;

    private long [] resultsOfCalibrage = {0,0,0,0};
    private SharedPreferencesHelper preferences;

    private long numberOfSample = 0;
    private long numberOfSecond = 0;

    private List<String> listSpinner = new ArrayList<String>();
    private ArrayAdapter<String> adaptere;

    private String sensorCommand;
    private String choiceMode ;

    private String saveValueOfCommandSensor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * Verification if the Smartphone is calibred with sensor
         * if it's not calibred, another activity will be called
         */
        this.testCalibrationOfSensors();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemSpinner.add(new Double(0));itemSpinner.add(new Double(1));
        itemSpinner.add(new Double(2));itemSpinner.add(new Double(3));
        this.choiceMode = getResources().getString(R.string.SAMPLE);

        /**
         * Configuration for Sensors
         */
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = new ComplexSensor(sensorManager, Sensor.TYPE_ACCELEROMETER);
        this.gyroscope = new ComplexSensor(sensorManager, Sensor.TYPE_GYROSCOPE);
        this.aproximity = new ComplexSensor(sensorManager, Sensor.TYPE_PROXIMITY);

        /**
         *
         here we recover the availability of sensors with static variable(ValidatorSensor),
         finally we can use ComplexSensor to find the state of the sensor itself
         */
        ValidatorSensor.returnResults(this.accelerometer, this.gyroscope, this.aproximity, this.availableSensors);

        /**
         * Adapter for the View
         */
        this.adapter = new BasicSpinnerAdapter(availableSensors, itemSpinner, this);
        this.listSensors = (ListView) findViewById(R.id.listSensor);
        this.listSensors.setAdapter(adapter);

        /**
         * Prepare View And Actions
         */
        this.body = (LinearLayout) findViewById(R.id.bodyMainActivity);
        this.sampledInput = (EditText) findViewById(R.id.sampledInput);
        this.timeInput = (EditText) findViewById(R.id.timeInput);
        this.bodyChild = (LinearLayout) findViewById(R.id.bodyChild);
        this.choiceModeGroup = (RadioGroup) findViewById(R.id.listOfChoicesMode);
        this.choiceModeGroup.check(R.id.radioSampleMode);
        this.adaptere = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listSpinner);
        this.choiceSensorforMode = (Spinner) findViewById(R.id.spinnerChoiceSensor);
        this.choiceSensorforMode.setAdapter(adaptere);

        this.goToCalibrageActivity = (Button) findViewById(R.id.gotoCalibrageActivity);
        this.body.removeView(timeInput);

        /**
         * Events on ListView
         */
        listSensors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ComplexSensor actualSensor = availableSensors.get(i);
                Spinner frequency = (Spinner) view.findViewById(R.id.spinner1);

                actualSensor.getDataOfSensor().setFrequency( (double) frequency.getSelectedItem());

                if (!actualSensor.isSelected()){
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    actualSensor.setSelected(true);
                    dataForNextActivities.add(actualSensor.getDataOfSensor());
                    listSpinner.add(actualSensor.getSensor().getName());
                    adaptere.notifyDataSetChanged();

                }else {
                    view.setBackgroundColor(getResources().getColor(R.color.colorBlank));
                    actualSensor.setSelected(false);
                    dataForNextActivities.remove(actualSensor.getDataOfSensor());
                    listSpinner.remove(actualSensor.getSensor().getName());
                    adaptere.notifyDataSetChanged();
                    if (listSpinner.isEmpty()){
                        sensorCommand = null;
                    }
                }
            }
        });

        /**
         * Event for Calibrage Button
         */
        this.goToCalibrageActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalibrageSensorActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /**
         * Event for GroupCheckButton
         */
        this.choiceModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if ( i == R.id.radioSampleMode){
                    body.removeView(timeInput);
                    body.addView(sampledInput, 1);
                    choiceMode = getResources().getString(R.string.SAMPLE);
                    try{
                        bodyChild.addView(choiceSensorforMode);
                        sensorCommand = saveValueOfCommandSensor;
                    }catch (Exception e){
                        e.getStackTrace();
                    }
                }else if (i == R.id.radioTimeMode){
                    body.removeView(sampledInput);
                    body.addView(timeInput, 1);
                    choiceMode = getResources().getString(R.string.TIME);
                    try{
                        bodyChild.addView(choiceSensorforMode);
                        sensorCommand =  saveValueOfCommandSensor ;
                    }catch (Exception e){
                        e.getStackTrace();
                    }
                }else {
                    body.removeView(sampledInput);
                    body.removeView(timeInput);
                    bodyChild.removeView(choiceSensorforMode);
                    choiceMode = getResources().getString(R.string.UNLIMITED);
                    numberOfSample = 0;
                    numberOfSecond = 0;
                    saveValueOfCommandSensor = sensorCommand;
                    sensorCommand = null;
                }
            }
        });

        this.choiceSensorforMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sensorCommand = (String) choiceSensorforMode.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        //end OnCreate(); here you can complete programme
    }

    /**
     * Events on Button Run
     */
    public void onClickRun(View view) {
        prepareDataAndGoToTheNextActivity();
    }

    /**
     * event for Return Button
     */
    @Override
    public void onBackPressed() {
        System.out.println("Temps d'exécution par echantillon  mode Normal :" + resultsOfCalibrage[0] + "// mode UI: " + resultsOfCalibrage[1] +
        "// mode Game :  " + resultsOfCalibrage[2] + "// mode Fastest : " + resultsOfCalibrage[3] );

        try{
            numberOfSample = Long.parseLong(sampledInput.getText().toString());
            numberOfSecond = Long.parseLong(timeInput.getText().toString());
        }catch (RuntimeException e){
            e.getStackTrace();
        }finally {
            System.out.println("voici le choix qui depend du mode : " + choiceMode+  "/////////" +numberOfSample + " // " + numberOfSecond);
        }

        System.out.println("Voici le capteur qui va etre prioritaire : " + sensorCommand);
        onResume();
    }

    /**
     * Verification Of calibration
     */
    public void testCalibrationOfSensors(){
        this.preferences  = new SharedPreferencesHelper(this);
        if (!preferences.preferences.getBoolean("alreadyCalibred", false)){
            Intent intent = new Intent(getApplicationContext(), CalibrageSensorActivity.class);
            startActivity(intent);
            finish();
        }else {
            resultsOfCalibrage[0] = preferences.preferences.getLong("normalMode", 0);
            resultsOfCalibrage[1] = preferences.preferences.getLong("uiMode", 0);
            resultsOfCalibrage[2] = preferences.preferences.getLong("gameMode", 0);
            resultsOfCalibrage[3] = preferences.preferences.getLong("fastestMode", 0);
        }
    }


    public void prepareDataAndGoToTheNextActivity(){
        Intent intent = new Intent(getApplicationContext(), RunSensorsActivity.class);
        intent.putExtras(this.prepareData());
        startActivity(intent);
        finish();
    }

    public Bundle prepareData(){
        try{
            numberOfSample = Long.parseLong(sampledInput.getText().toString());
            numberOfSecond = Long.parseLong(timeInput.getText().toString());
        }catch (RuntimeException e) {
            e.getStackTrace();
        }
        int frequency = 0 ;
        for (DataForNextActivity data : dataForNextActivities){
            if (data.getName() == sensorCommand){
                System.out.println("XXXXXXXXXXXXXXXXXXX");
                System.out.println(data.getFrequency());
                frequency = (int) data.getFrequency();
                System.out.println(frequency);
                System.out.println("XXXXXXXXXXXXXXXXXXX");
            }
        }

        RunMode configurationForNextActivity = new RunMode(choiceMode,(int) numberOfSample, sensorCommand, frequency );
        Bundle bundle = new Bundle();
        long z = 1000000000;


        if (choiceMode == getResources().getString(R.string.SAMPLE)){
            bundle.putParcelable("configuration", configurationForNextActivity);
        }else if (choiceMode == getResources().getString(R.string.TIME)){
            if (frequency == SensorManager.SENSOR_DELAY_NORMAL){
                configurationForNextActivity.setNecessaryIndex((int) (((numberOfSecond*1000000000)/ resultsOfCalibrage[0]) % 10));
            }else if (frequency == SensorManager.SENSOR_DELAY_UI){
                configurationForNextActivity.setNecessaryIndex((int) (((numberOfSecond*1000000000)/ resultsOfCalibrage[0]) % 10));
            }else if (frequency == SensorManager.SENSOR_DELAY_GAME){
                configurationForNextActivity.setNecessaryIndex((int) (((numberOfSecond*1000000000)/ resultsOfCalibrage[0]) % 10));
            }else if(frequency == SensorManager.SENSOR_DELAY_FASTEST)  {
                configurationForNextActivity.setNecessaryIndex((int) (((numberOfSecond*1000000000)/ resultsOfCalibrage[0]) % 10));
            }
        }else if (choiceMode == getResources().getString(R.string.UNLIMITED)){
            bundle.putParcelable("configuration", configurationForNextActivity);
        }

        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) dataForNextActivities);
        return bundle;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
