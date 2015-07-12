package com.aditya.staytuned.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aditya.staytuned.InitializerActivity;

/**
 *
 * Created by devad_000 on 12-07-2015.
 *
 */
public class LaunchBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent_r = new Intent(context, InitializerActivity.class);
        context.startActivity(intent_r);
    }
}
