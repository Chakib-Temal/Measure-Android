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
import android.widget.ListView;
import android.widget.Spinner;

import com.chakibtemal.fr.Adapter.BasicSpinnerAdapter;
import com.chakibtemal.fr.modele.calibrationSensor.CalibrateTime;
import com.chakibtemal.fr.modele.sharedResources.ComplexSensor;
import com.chakibtemal.fr.modele.sharedResources.DataForNextActivity;
import com.chakibtemal.fr.modele.validator.ValidatorSensor;

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
    protected List<DataForNextActivity> dataForNextActivities = new ArrayList<DataForNextActivity>();

    private List<Double> itemSpinner = new ArrayList<Double>();
    private BasicSpinnerAdapter adapter2;


    private Context context;

    public static long timeForOneShoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemSpinner.add(new Double(0));itemSpinner.add(new Double(1));
        itemSpinner.add(new Double(2));itemSpinner.add(new Double(3));
        this.context = this;
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
        adapter2 = new BasicSpinnerAdapter(availableSensors, itemSpinner, this);
        listSensors = (ListView) findViewById(R.id.listSensor);
        listSensors.setAdapter(adapter2);

        /**
         * Calibrage du temps
         * boutton retour nous donne la valeur et a chaque fois qu'on relance l'activity ya un nv calibrage
         */
        CalibrateTime calibrateTime = new CalibrateTime();
        calibrateTime.calibrateTimeForSensor(this);


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

                    /*  changement d'une seule ligne
                    if (actualSensor.getSensor().getName() == new ComplexSensor(sensorManager, Sensor.TYPE_ACCELEROMETER).getSensor().getName()){
                        TextView  nameSensor = (TextView)view.findViewById(R.id.nameSensor);
                        nameSensor.setText("change !!!");
                    }
                    */

                }else {
                    view.setBackgroundColor(getResources().getColor(R.color.colorBlank));
                    actualSensor.setSelected(false);
                    dataForNextActivities.remove(actualSensor.getDataOfSensor());
                }
            }
        });
        //end OnCreate(); here you can complete programme



    }

    /**
     * Events on Button Run
     */
    public void onClickRun(View view) {

        Intent intent = new Intent(getApplicationContext(), RunSensorsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) dataForNextActivities);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        System.out.println("Temps d'exécution " + timeForOneShoot);
        // le temps qu'on recoit * 10^9  / timeForOneShoot

        onResume();
    }

}
