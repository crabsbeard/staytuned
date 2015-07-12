package com.aditya.staytuned;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aditya.staytuned.pojo.Constants;
import com.aditya.staytuned.services.VisualDataService;

import java.util.Map;

/**
 *
 * Created by devad_000 on 12-07-2015.
 *
 */

public class InitializerActivity extends AppCompatActivity implements Constants{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mSharedPref = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        Map<String, ?> users = mSharedPref.getAll();
        if(users.size()==0){
            //do nothing
        }else{
            String username  = mSharedPref.getString("user", "");
            Intent intent = new Intent(this, VisualDataService.class);
            intent.putExtra("username.service", username);
            startService(intent);
        }
    }
}
