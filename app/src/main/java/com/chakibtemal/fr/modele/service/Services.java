package com.chakibtemal.fr.modele.service;

import android.content.Context;

import com.chakibtemal.fr.androidproject.R;
import com.chakibtemal.fr.modele.sharedResources.RunMode;

public class Services {

    public int getNecessaryIndex(String runMode, Context context, RunMode configuration){

        if (runMode.equals(context.getResources().getString(R.string.SAMPLE))){
            return configuration.getNecessaryIndex();

        }else if (runMode.equals(context.getResources().getString(R.string.TIME))){
            return configuration.getNecessaryIndex();

        }else if (runMode.equals(context.getResources().getString(R.string.UNLIMITED))){
            return 1000000;
        }

        return 0;
    }
}
