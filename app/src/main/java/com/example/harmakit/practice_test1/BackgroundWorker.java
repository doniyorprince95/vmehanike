package com.example.harmakit.practice_test1;

    import android.app.AlertDialog;
    import android.app.ProgressDialog;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.AsyncTask;

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

public class BackgroundWorker extends AsyncTask<String,Void,String> {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_LOGIN = "login";
    public static final String APP_PREFERENCES_LOGINCHECK = "logincheck";
    public static final String APP_PREFERENCES_FIRSTNAME = "firstname";
    public static final String APP_PREFERENCES_LASTNAME = "lastname";
    public static final String APP_PREFERENCES_PATRONYMIC = "patronymic";
    public static final String APP_PREFERENCES_COMPANY_NAME = "company_name";
    public static final String APP_PREFERENCES_CLIENT = "client";

    ProgressDialog WaitingDialog;


    Context context;
    AlertDialog alertDialog;
    BackgroundWorker (Context ctx) {
        context = ctx;
        alertDialog = new AlertDialog.Builder(context).create();
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String registerUrl = "http://pavel.meshok24.ru/register.php";
        String loginUrl = "http://pavel.meshok24.ru/login.php";
        String getinfoUrl = "http://pavel.meshok24.ru/getinfo.php";
        if(type.equals("register")) {
            try {

                alertDialog.setTitle("Статус регистрации");

                String login  = params[1];
                String password_hash = params[2];
                String firstname = params[3];
                String lastname = params[4];
                String patronymic = params[5];
                String company_name = params[6];
                String client = params[7];
                company_name = company_name.replace(" ", "_");


                URL url = new URL(registerUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = null;
                boolean isConnected = true;
                try{
                    outputStream = httpURLConnection.getOutputStream();}
                catch (IOException e){isConnected = false;}

                if (isConnected){
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data;
                if (client.equals("юр")){
                    post_data = URLEncoder.encode("login","UTF-8")+"="+URLEncoder.encode(login,"UTF-8")+"&"
                            +URLEncoder.encode("password_hash","UTF-8")+"="+URLEncoder.encode(password_hash,"UTF-8")+"&"
                            +URLEncoder.encode("company_name","UTF-8")+"="+URLEncoder.encode(company_name,"UTF-8")+"&"
                            +URLEncoder.encode("client","UTF-8")+"="+URLEncoder.encode(client ,"UTF-8");
                }
                else {
                    post_data = URLEncoder.encode("login","UTF-8")+"="+URLEncoder.encode(login,"UTF-8")+"&"
                            +URLEncoder.encode("password_hash","UTF-8")+"="+URLEncoder.encode(password_hash,"UTF-8")+"&"
                            +URLEncoder.encode("firstname","UTF-8")+"="+URLEncoder.encode(firstname,"UTF-8")+"&"
                            +URLEncoder.encode("lastname","UTF-8")+"="+URLEncoder.encode(lastname,"UTF-8")+"&"
                            +URLEncoder.encode("patronymic","UTF-8")+"="+URLEncoder.encode(patronymic,"UTF-8")+"&"
                            +URLEncoder.encode("client","UTF-8")+"="+URLEncoder.encode(client ,"UTF-8");
                }


                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                    result = result.replace("\uFEFF", ""); //удаление BOM

                    if (result.equals("Регистрация завершена!")) {
                        SharedPreferences.Editor editor = getPrefs(context).edit();
                        editor.putString(APP_PREFERENCES_LOGIN, login);
                        editor.putString(APP_PREFERENCES_CLIENT, null);
                        if (client.equals("физ")) {
                            editor.putString(APP_PREFERENCES_LASTNAME, lastname);
                            editor.putString(APP_PREFERENCES_FIRSTNAME, firstname);
                            editor.putString(APP_PREFERENCES_PATRONYMIC, patronymic);

                        } else {
                            editor.putString(APP_PREFERENCES_COMPANY_NAME, company_name);
                        }
                        editor.apply();
                    }
                    return result;
            }
            else {
                    String result = null;
                    return result;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {

                alertDialog.setTitle("Статус авторизации");

                String login  = params[1];
                String password_hash = params[2];

                URL url = new URL(loginUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();


                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = null;
                boolean isConnected = true;
                try{
                outputStream = httpURLConnection.getOutputStream();}
                catch (IOException e){isConnected = false;}

                if (isConnected){
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));


                String post_data;
                    post_data = URLEncoder.encode("login","UTF-8")+"="+URLEncoder.encode(login,"UTF-8")+"&"
                            +URLEncoder.encode("password_hash","UTF-8")+"="+URLEncoder.encode(password_hash,"UTF-8")+"&";

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();



                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                result = result.replace("\uFEFF", ""); //удаление BOM

                if (result.equals("success")) {

                URL infoUrl = new URL(getinfoUrl);
                HttpURLConnection httpinfoURLConnection = (HttpURLConnection)infoUrl.openConnection();

                httpinfoURLConnection.setRequestMethod("POST");
                httpinfoURLConnection.setDoOutput(true);
                httpinfoURLConnection.setDoInput(true);

                OutputStream infooutputStream = httpinfoURLConnection.getOutputStream();
                BufferedWriter infobufferedWriter = new BufferedWriter(new OutputStreamWriter(infooutputStream, "UTF-8"));

                infobufferedWriter.write(post_data);
                infobufferedWriter.flush();
                infobufferedWriter.close();
                infooutputStream.close();

                InputStream infoinputStream = httpinfoURLConnection.getInputStream();
                BufferedReader infobufferedReader = new BufferedReader(new InputStreamReader(infoinputStream,"UTF-8"));
                String inforesult="";
                while((line = infobufferedReader.readLine())!= null) {
                    inforesult += line;
                }
                infobufferedReader.close();
                infoinputStream.close();
                httpinfoURLConnection.disconnect();



                    SharedPreferences.Editor editor = getPrefs(context).edit();
                    editor.putString(APP_PREFERENCES_LOGIN, login);
                    String[] tokens = inforesult.split(" ");
                    String client = tokens[0];
                    editor.putString(APP_PREFERENCES_CLIENT, null);
                    if (tokens.length == 4) {
                        String lastname = tokens[1];
                        String firstname = tokens[2];
                        String patronymic = tokens[3];

                        editor.putString(APP_PREFERENCES_LASTNAME, lastname);
                        editor.putString(APP_PREFERENCES_FIRSTNAME, firstname);
                        editor.putString(APP_PREFERENCES_PATRONYMIC, patronymic);

                    } else {
                        String company_name = tokens[4];
                        editor.putString(APP_PREFERENCES_COMPANY_NAME, company_name);
                    }
                    editor.apply();
                    }
                    return result;
                }
                else{
                String result = null;
                return result;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        WaitingDialog = ProgressDialog.show(context, "Подключение к серверу", "Отправляем данные...", true);
    }

    @Override
    protected void onPostExecute(String result) {
        WaitingDialog.dismiss();
        if (result == null){
            alertDialog.setMessage("Нет подключения к интернету");
            alertDialog.show();
        }
        else {
            if (result.equals("success") || result.equals("Регистрация завершена!")) {
                SharedPreferences.Editor editor = getPrefs(context).edit();
                editor.putInt(APP_PREFERENCES_LOGINCHECK, 1);
                editor.apply();

                Intent intent = new Intent(context, StartScreenActivity.class);
                context.startActivity(intent);
            } else {
                alertDialog.setMessage(result);
                alertDialog.show();
            }
        }
    }

}