package com.example.harmakit.practice_test1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartScreenActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "login";
    public static final String APP_PREFERENCES_LOGINCHECK = "logincheck";
    public static final String APP_PREFERENCES_FIRSTNAME = "firstname";
    public static final String APP_PREFERENCES_LASTNAME = "lastname";
    public static final String APP_PREFERENCES_PATRONYMIC = "patronymic";
    public static final String APP_PREFERENCES_COMPANY_NAME = "company_name";
    public static final String APP_PREFERENCES_CLIENT = "client";
    private SharedPreferences mSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        Button registartionActivityButton = (Button)findViewById(R.id.registartionActivityButton);
        Button loginActivityButton = (Button)findViewById(R.id.loginActivityButton);
        Button createRequestButton = (Button)findViewById(R.id.createRequestButton);
        Button logoutButton = (Button)findViewById(R.id.logoutButton);
        createRequestButton.setVisibility(Button.INVISIBLE);
        logoutButton.setVisibility(Button.INVISIBLE);

        if (mSettings.contains(APP_PREFERENCES_LOGINCHECK)) {
            // Получаем параметр из настроек
            int isLogin = mSettings.getInt(APP_PREFERENCES_LOGINCHECK, 0);
            if (isLogin == 1){
                registartionActivityButton.setVisibility(Button.INVISIBLE);
                loginActivityButton.setVisibility(Button.INVISIBLE);
                createRequestButton.setVisibility(Button.VISIBLE);
                logoutButton.setVisibility(Button.VISIBLE);
            }
            else{
                registartionActivityButton.setVisibility(Button.VISIBLE);
                loginActivityButton.setVisibility(Button.VISIBLE);
                createRequestButton.setVisibility(Button.INVISIBLE);
                logoutButton.setVisibility(Button.INVISIBLE);
            }
        }
    }

    public void openRegisterActivity(View view){
        Intent intent = new Intent(StartScreenActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void openLoginActivity(View view) {
        Intent intent = new Intent(StartScreenActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public  void openRequestActivity(View view){
        Intent intent = new Intent (StartScreenActivity.this, RequestActivity.class);
        startActivity(intent);
    }

    public void logout(View view){

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_LOGINCHECK, 0);
        editor.putString(APP_PREFERENCES_LOGIN, null);
        editor.putString(APP_PREFERENCES_FIRSTNAME, null);
        editor.putString(APP_PREFERENCES_LASTNAME, null);
        editor.putString(APP_PREFERENCES_PATRONYMIC, null);
        editor.putString(APP_PREFERENCES_COMPANY_NAME, null);
        editor.putString(APP_PREFERENCES_CLIENT, null);
        editor.apply();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}
