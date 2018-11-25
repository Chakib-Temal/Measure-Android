package com.chakibtemal.fr.androidproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.chakibtemal.fr.Adapter.ModelsAdapter;
import com.chakibtemal.fr.modele.sharedResources.AllDataForRunActivity;
import com.chakibtemal.fr.modele.sharedResources.DataForNextActivity;
import com.chakibtemal.fr.modele.sharedResources.RunMode;

import java.util.ArrayList;
import java.util.List;

public class ModeleRunActivity extends AppCompatActivity {
    private List<DataForNextActivity> dataForNextActivities = new ArrayList<DataForNextActivity>();
    private RunMode configurationForNextActivity;

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

        this.listViewModels = (ListView) findViewById(R.id.listModels);
        ModelsAdapter adapter = new ModelsAdapter(listModelsArray, this);
        this.listViewModels.setAdapter(adapter);

    }

}
