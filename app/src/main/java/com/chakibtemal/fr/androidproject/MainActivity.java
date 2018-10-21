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

public class MainActivity extends AppCompatActivity  {

    private ListView listSensors = null;
    private Button goToCalibrageActivity = null;
    private LinearLayout body = null;
    private EditText sampledInput = null;
    private EditText timeInput = null;
    private RadioGroup choiceModeGroup = null;
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

    private String choiceMode ;


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
                }else {
                    view.setBackgroundColor(getResources().getColor(R.color.colorBlank));
                    actualSensor.setSelected(false);
                    dataForNextActivities.remove(actualSensor.getDataOfSensor());
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
                }else if (i == R.id.radioTimeMode){
                    body.removeView(sampledInput);
                    body.addView(timeInput, 1);
                    choiceMode = getResources().getString(R.string.TIME);
                }else {
                    body.removeView(sampledInput);
                    body.removeView(timeInput);
                    choiceMode = getResources().getString(R.string.UNLIMITED);
                    numberOfSample = 0;
                    numberOfSecond = 0;
                }
            }
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

        if (choiceMode == getResources().getString(R.string.SAMPLE) && numberOfSample == new Long(0)){
            return;
        }else if (choiceMode == getResources().getString(R.string.TIME) && numberOfSecond == new Long(0)){
            return;
        }else {
            startActivity(intent);
            finish();
        }
    }

    public Bundle prepareData(){
        try{
            numberOfSample = Long.parseLong(sampledInput.getText().toString());
        }catch (RuntimeException e) {
            e.getStackTrace();
        }

        try {
            numberOfSecond = Long.parseLong(timeInput.getText().toString());
        }catch (Exception e){
            e.getStackTrace();
        }

        Double frequencyDouble = new Double(0);
        int frequency = frequencyDouble.intValue();
        int fr = frequencyDouble.intValue();

        Bundle bundle = new Bundle();

        if (choiceMode == getResources().getString(R.string.SAMPLE)){
            RunMode configurationForNextActivity = new RunMode(choiceMode,(int) numberOfSample );
            bundle.putParcelable("configuration", configurationForNextActivity);

        }else if (choiceMode == getResources().getString(R.string.TIME)) {

            for (int i = 0; i < dataForNextActivities.size(); i++) {

                if (dataForNextActivities.get(i).getFrequency() == SensorManager.SENSOR_DELAY_NORMAL) {
                    fr = ((int) ((((numberOfSecond * 1000000000) / resultsOfCalibrage[0]) % 100000) + 1));
                } else if (dataForNextActivities.get(i).getFrequency() == SensorManager.SENSOR_DELAY_UI) {
                    fr = ((int) ((((numberOfSecond * 1000000000) / resultsOfCalibrage[1]) % 100000) + 1));
                } else if (dataForNextActivities.get(i).getFrequency() ==  SensorManager.SENSOR_DELAY_GAME) {
                    fr = ((int) ((((numberOfSecond * 1000000000) / resultsOfCalibrage[2]) % 100000) + 1));
                } else if (dataForNextActivities.get(i).getFrequency() ==  SensorManager.SENSOR_DELAY_FASTEST) {
                    fr = ((int) ((((numberOfSecond * 1000000000) / resultsOfCalibrage[3]) % 100000) + 1));
                }

                // to take greatest value of frequency
                if (fr > frequency){
                    frequency = fr;
                }
            }
            RunMode configurationForNextActivity = new RunMode(choiceMode, frequency );
            bundle.putParcelable("configuration", configurationForNextActivity);

        }else if (choiceMode == getResources().getString(R.string.UNLIMITED)){
            RunMode configurationForNextActivity = new RunMode(choiceMode,(int) 0 );
            bundle.putParcelable("configuration", configurationForNextActivity);
        }

        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) dataForNextActivities);
        return bundle;
    }

}
