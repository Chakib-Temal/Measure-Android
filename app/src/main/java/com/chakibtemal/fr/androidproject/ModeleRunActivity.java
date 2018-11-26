package com.chakibtemal.fr.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chakibtemal.fr.Adapter.ModelsAdapter;
import com.chakibtemal.fr.modele.sharedResources.AllDataForRunActivity;
import com.chakibtemal.fr.modele.sharedResources.DataForNextActivity;
import com.chakibtemal.fr.modele.sharedResources.DataModels;
import com.chakibtemal.fr.modele.sharedResources.RunMode;
import com.chakibtemal.fr.modele.sqliteDb.ModelsRuning.DataForNextActivityBdd;
import com.chakibtemal.fr.modele.sqliteDb.ModelsRuning.RunModeBdd;

import java.util.ArrayList;
import java.util.List;

public class ModeleRunActivity extends AppCompatActivity {
    private RunMode configurationForNextActivity;
    private List<AllDataForRunActivity> allDataForRunActivities = new ArrayList<AllDataForRunActivity>();
    private List<DataModels> dataModels = new ArrayList<DataModels>();

    private ListView listViewModels;
    private List<AllDataForRunActivity> listModelsArray = new ArrayList<AllDataForRunActivity>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modele_run);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DataForNextActivityBdd dataSource = new DataForNextActivityBdd(this);
        dataSource.open();
        RunModeBdd data = new RunModeBdd(this);
        data.open();
        this.dataModels.addAll(dataSource.getAllDatas(data));
        data.close();
        dataSource.close();

        int maxIdTableRunMode = 0;

        for (DataModels d : dataModels){
            if (maxIdTableRunMode < d.getId_runMode()){
                maxIdTableRunMode = d.getId_runMode();
            }
        }

        for (int id= 1; id <= maxIdTableRunMode;  id++){
            AllDataForRunActivity adf = new AllDataForRunActivity();
            for (DataModels d : dataModels){
                if( d.getId_runMode() == id){
                    DataForNextActivity datas = new DataForNextActivity();
                    datas.setId_runMode(d.getId_runMode());
                    datas.setFrequency(d.getFrequency());
                    datas.setType(d.getType());
                    datas.setName(d.getName());
                    RunMode runMode = new RunMode();
                    runMode.setNecessaryIndex(d.getNecessaryIndex());
                    runMode.setNameMode(d.getNameMode());
                    adf.setRunMode(runMode);

                    adf.getDataForNextActivities().add(datas);
                }
            }
            listModelsArray.add(adf);
        }

        this.listViewModels = (ListView) findViewById(R.id.listModels);
        ModelsAdapter adapter = new ModelsAdapter(listModelsArray, this);
        this.listViewModels.setAdapter(adapter);

        for (AllDataForRunActivity d : listModelsArray){
            for (DataForNextActivity n : d.getDataForNextActivities()){
                System.out.println(n.getName() + "/ " + n.getId_runMode());
            }
        }

        listViewModels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AllDataForRunActivity actualModele = listModelsArray.get(i);
                RunMode runMode = actualModele.getRunMode();
                List<DataForNextActivity> dataForNextActivities = actualModele.getDataForNextActivities();


                for (DataForNextActivity d : actualModele.getDataForNextActivities()){
                    System.out.println(d.getName() + " / " + d.getId_runMode());
                }

                Bundle bundle = new Bundle();
                bundle.putParcelable("configuration", runMode);
                bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) dataForNextActivities);

                goToRunActivity(bundle);

            }


        });

    }

    public void goToRunActivity(Bundle bundle){
        Intent intent = new Intent(getApplicationContext(), RunSensorsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

}
