package com.example.harmakit.practice_test1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class RegisterActivity extends AppCompatActivity {

    FormatWatcher phoneFormatWatcher = new MaskFormatWatcher(
            MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
    );

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean legalClient = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        phoneFormatWatcher.installOn((EditText)findViewById(R.id.phoneEditText));
        Switch clientTypeSwitch = (Switch)findViewById(R.id.clientTypeSwitch);
        clientTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            EditText lName = (EditText)findViewById(R.id.lastnameEditText);
            EditText fName = (EditText)findViewById(R.id.firstnameEditText);
            EditText pName = (EditText)findViewById(R.id.patronymicEditText);
            EditText cName = (EditText)findViewById(R.id.companynameEditText);

            public int invertVisibility(int a){
                if (a == View.INVISIBLE)
                    a = View.VISIBLE;
                else
                    a = View.INVISIBLE;
                return a;
            }

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lName.setVisibility(invertVisibility(lName.getVisibility()));
                fName.setVisibility(invertVisibility(fName.getVisibility()));
                pName.setVisibility(invertVisibility(pName.getVisibility()));
                cName.setVisibility(invertVisibility(cName.getVisibility()));
                legalClient = !legalClient;
            }
        });
    }


       public void onRegisterClicked(View view) {
           EditText phoneEditText = (EditText)findViewById(R.id.phoneEditText);
           EditText passwordEditText = (EditText)findViewById(R.id.passwordEditText);
           EditText firstnameEditText = (EditText)findViewById(R.id.firstnameEditText);
           EditText lastnameEditText = (EditText)findViewById(R.id.lastnameEditText);
           EditText patronymicEditText = (EditText)findViewById(R.id.patronymicEditText);
           EditText companynameEditText = (EditText)findViewById(R.id.companynameEditText);
           boolean inputFailed = false;

           String phoneEditTextStr = phoneEditText.getText().toString();
           String passwordEditTextStr = passwordEditText.getText().toString();

           String passwordEditTextStrMD5 = md5(passwordEditTextStr);
           String firstnameEditTextStr = firstnameEditText.getText().toString();
           String lastnameEditTextStr = lastnameEditText.getText().toString();
           String patronymicEditTextStr = patronymicEditText.getText().toString();
           String companynameEditTextStr = companynameEditText.getText().toString();

           if (TextUtils.isEmpty(phoneEditTextStr) || phoneEditTextStr.length() < 18) {
               phoneEditText.setError("Введите корректный номер");
               inputFailed = true;
           }
           if (!legalClient && TextUtils.isEmpty(firstnameEditTextStr)) {
               firstnameEditText.setError("Поле должно быть заполнено");
               inputFailed = true;
           }
           if (!legalClient && TextUtils.isEmpty(lastnameEditTextStr)) {
               lastnameEditText.setError("Поле должно быть заполнено");
               inputFailed = true;
           }
           if (!legalClient && TextUtils.isEmpty(patronymicEditTextStr)) {
               patronymicEditText.setError("Поле должно быть заполнено");
               inputFailed = true;
           }
           if (legalClient && TextUtils.isEmpty(companynameEditTextStr)) {
               companynameEditText.setError("Поле должно быть заполнено");
               inputFailed = true;
           }
           if (TextUtils.isEmpty(passwordEditTextStr) || passwordEditTextStr.length() < 6) {
               passwordEditText.setError("Не менее 6 символов");
               inputFailed = true;
           }
           if (inputFailed)
                   return;

           String type = "register";
           BackgroundWorker backgroundWorker = new BackgroundWorker(this);
           backgroundWorker.execute(type, phoneEditTextStr, passwordEditTextStrMD5,firstnameEditTextStr, lastnameEditTextStr, patronymicEditTextStr, companynameEditTextStr, (legalClient ? "юр" : "физ") );

       }

}

