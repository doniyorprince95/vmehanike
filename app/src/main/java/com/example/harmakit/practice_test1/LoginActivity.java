package com.example.harmakit.practice_test1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class LoginActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneFormatWatcher.installOn((EditText)findViewById(R.id.phoneEditText));
    }


    public void onLoginClicked(View view) {
        boolean inputFailed = false;
        EditText phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        EditText passwordEditText = (EditText)findViewById(R.id.passwordEditText);

        String phoneEditTextStr = phoneEditText.getText().toString();
        String passwordEditTextStr = passwordEditText.getText().toString();

        String passwordEditTextStrMD5 = md5(passwordEditTextStr);
        if (TextUtils.isEmpty(phoneEditText.getText().toString()) || phoneEditText.getText().toString().length() < 18) {
            phoneEditText.setError("Введите корректный номер");
            inputFailed = true;
        }
        if (TextUtils.isEmpty(passwordEditText.getText().toString()) || passwordEditText.getText().toString().length() < 6) {
            passwordEditText.setError("Не менее 6 символов");
            inputFailed = true;
        }
        if (inputFailed)
            return;

        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(LoginActivity.this);
        backgroundWorker.execute(type, phoneEditTextStr, passwordEditTextStrMD5);

       }
}
