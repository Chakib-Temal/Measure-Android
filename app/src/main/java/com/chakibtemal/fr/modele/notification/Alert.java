package com.chakibtemal.fr.modele.notification;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Alert {
    private Context context;
    private int title;
    private String message ;

    public Alert(Context context, int title, String message) {
        this.context = context;
        this.title = title;
        this.message = message;
    }

    public void showAlert(){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
