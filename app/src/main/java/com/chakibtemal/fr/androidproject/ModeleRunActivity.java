package com.chakibtemal.fr.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
    private List<DataModels> dataModels = new ArrayList<DataModels>();

    private ListView listViewModels;
    private List<AllDataForRunActivity> listModelsArray;

    private RunModeBdd data;
    private DataForNextActivityBdd dataSource;
    private ModelsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modele_run);
    }

    @Override
    protected void onResume() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listModelsArray = new ArrayList<AllDataForRunActivity>();

        this.dataSource = new DataForNextActivityBdd(this);
        dataSource.open();
        this.data = new RunModeBdd(this);
        data.open();
        this.dataModels.addAll(dataSource._getAllDatas(data));
        data.close();
        dataSource.close();

        int maxIdTableRunMode = 0;

        for (DataModels d : dataModels){
            if (maxIdTableRunMode < d.getId_runMode()){
                maxIdTableRunMode = d.getId_runMode();
            }
        }
        String empty = new String();

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
            try {
                if (!adf.getRunMode().getNameMode().equals(empty) || adf.getRunMode().getNameMode() != null  ){
                    listModelsArray.add(adf);
                }
            }catch (Exception e){
                continue;
            }
        }

        this.listViewModels = (ListView) findViewById(R.id.listModels);
        this.adapter = new ModelsAdapter(listModelsArray, this);
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

        listViewModels.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AllDataForRunActivity actualModele = listModelsArray.get(i);
                actualModele.getDataForNextActivities();
                dataSource.open();
                int id_RunMode = dataSource.deleteSensorsOfModel(actualModele);
                dataSource.close();
                data.open();
                data.deleteModeRun(id_RunMode);
                data.close();

                listModelsArray.remove(i);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        super.onResume();
    }

    public void goToRunActivity(Bundle bundle){
        Intent intent = new Intent(getApplicationContext(), RunSensorsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
