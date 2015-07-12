package com.aditya.staytuned;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aditya.staytuned.database.DataBaseAdapter;
import com.aditya.staytuned.pojo.Constants;
import com.aditya.staytuned.toaster.Toaster;

import java.util.Map;


public class MainActivity extends AppCompatActivity implements Constants {

    private EditText et_username;
    private EditText et_password;
    private Button b_login;
    private Button b_register;
    private TextView tv_forgot;
    SharedPreferences mSharedPreferences;
    Toaster toaster;
    DataBaseAdapter dbHelper;
    RelativeLayout relativeLayout;
    RelativeLayout relativeLayout_loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = (RelativeLayout) findViewById(R.id.loginGroup);
        relativeLayout_loader = (RelativeLayout) findViewById(R.id.loaderGroup);
        checkUser();
        toaster = new Toaster(this);
        initLayout();
        dbHelper = new DataBaseAdapter(this);
    }

    private void checkUser() {
        mSharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        Map<String, ?> users = mSharedPreferences.getAll();
        if(users.size()==0){
            toaster.ShortToaster("No user found, please login!");
            relativeLayout_loader.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
        if(users.size()>0||users.size()<0){
            toaster.ShortToaster("Error!");
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.clear();
            editor.apply();
        }else{
            toaster.ShortToaster("Authenticated!");
            launchMainApp();
        }
    }

    public void loginUser(View view){
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        boolean doesIt = dbHelper.fetchUserData(username, password);
        if(doesIt){
            launchMainApp();
            mSharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("user", username);
            editor.apply();
        }else {
            toaster.ShortToaster("Username Does Not Exist or Password doesn't Match!");
        }
    }

    private void launchMainApp() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void registerUser(View view){
        mSharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        if(username.length()==0 && password.length()==0) {
            toaster.ShortToaster("Fields Can Not Be Empty");
        }else{
            dbHelper.insertUsersData(username, password);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("user", username);
            editor.apply();
        }
    }

    public void forgotPassword(View view){
        toaster.ShortToaster("To be implemented!");
    }

    private void initLayout() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        b_login = (Button) findViewById(R.id.b_login);
        b_register = (Button) findViewById(R.id.b_register);
        tv_forgot = (TextView) findViewById(R.id.tv_forgotPass);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
