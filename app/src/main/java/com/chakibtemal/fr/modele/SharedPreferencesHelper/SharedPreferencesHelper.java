package com.chakibtemal.fr.modele.SharedPreferencesHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesHelper  {
    public SharedPreferences preferences;
    public SharedPreferences.Editor editor;
    public Context context;

    public SharedPreferencesHelper(Context context){
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        this.editor = this.preferences.edit();
    }
}
