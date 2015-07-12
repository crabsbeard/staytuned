package com.aditya.staytuned.toaster;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by devad_000 on 12-07-2015.
 */
public class Toaster {
    private Context context;
    public Toaster(Context context) {
        this.context = context;
    }
    public void ShortToaster(String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void LongToaster(String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }
}
