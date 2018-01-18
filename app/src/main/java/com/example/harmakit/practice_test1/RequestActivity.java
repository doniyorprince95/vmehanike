package com.example.harmakit.practice_test1;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RequestActivity extends Activity {

	private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
	};

	protected static final int PICK_PHOTO = 1;

	String title;
	String text;
	String from;
	String where;
	String attach;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        attach = "";
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        (findViewById(R.id.photoButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				int permission = ActivityCompat.checkSelfPermission(RequestActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

				if (permission != PackageManager.PERMISSION_GRANTED) {

					ActivityCompat.requestPermissions(
							RequestActivity.this,
							PERMISSIONS_STORAGE,
							REQUEST_EXTERNAL_STORAGE
					);
				}
				Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, PICK_PHOTO);

			}
		});
        
        (findViewById(R.id.sendRequestButton)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				title = ((EditText)findViewById(R.id.requestNameEditText)).getText().toString();
				text = ((EditText)findViewById(R.id.requestTextEditText)).getText().toString();

				MailSenderAsync async_sending = new MailSenderAsync();
				async_sending.execute(title, text);
			}
		});
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		ImageView iv = (ImageView)findViewById(R.id.imageView);
		super.onActivityResult(requestCode, resultCode, data);

		switch(requestCode){
			case PICK_PHOTO:
				if (resultCode == RESULT_OK){
					Uri uri = data.getData();
					String []projection={MediaStore.Images.Media.DATA};

					Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
					cursor.moveToFirst();

					int columnIndex = cursor.getColumnIndex(projection[0]);
					String filePath = cursor.getString(columnIndex);
					cursor.close();

					Bitmap selectedImage = BitmapFactory.decodeFile(filePath);

					iv.setImageBitmap(selectedImage);
					attach = filePath;
				}
			default:
				break;
		}
    }
    
    private class MailSenderAsync extends AsyncTask<String, String, Boolean> {
    	ProgressDialog WaitingDialog;

		@Override
		protected void onPreExecute() {

			WaitingDialog = ProgressDialog.show(RequestActivity.this, "Отправка данных", "Отправляем сообщение...", true);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			WaitingDialog.dismiss();
			Toast.makeText(RequestActivity.this, "Отправка завершена!", Toast.LENGTH_LONG).show();
			(RequestActivity.this).finish();
		}

		@Override
		protected Boolean doInBackground(String... args) {

			try {

				title = args[0];
				text = args[1];

				from = "autorequests@gmail.com";
				where = "requestsinbox@gmail.com";
				
                MailSenderClass sender = new MailSenderClass("autorequests@gmail.com", "dE2e8I1y4dH3", RequestActivity.this);

				try{
                sender.sendMail(title, text, from, where, attach);}
				catch (Exception e){
					Log.e("sendMail","Ошибка отправки сообщения! ");
					e.printStackTrace();
					RequestActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(RequestActivity.this, "Нет подключения к интернету!", Toast.LENGTH_SHORT).show();
						}
					});
				}
			} catch (Exception e) {
				Log.e("sendMail","Ошибка отправки сообщения! ");
				e.printStackTrace();
				RequestActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(RequestActivity.this, "Ошибка отправки сообщения!", Toast.LENGTH_SHORT).show();
					}
				});
			}
			
			return false;
		}
	}
}