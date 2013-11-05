package com.noundla.playsound;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
	private EditText mPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPassword = (EditText)findViewById(R.id.editText1);
		if(Constants.IS_SET){
			findViewById(R.id.button1).setEnabled(false);
			findViewById(R.id.button1).setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void playAudio(View view) {
		if(!Constants.IS_SET){
			Constants.IS_SET = true;
			//setup round time at 00:00
			Calendar newcal  = Calendar.getInstance();
			newcal.set(newcal.get(Calendar.YEAR), 3, 24, 0, 0, 0);

			Calendar current  = Calendar.getInstance();
			if(newcal.after(current)){
				Intent intent = new Intent(this, PlayAudioService.class);
				PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
				AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
				alarm.set(AlarmManager.RTC_WAKEUP, newcal.getTimeInMillis(),  pintent);
				findViewById(R.id.button1).setEnabled(false);
				findViewById(R.id.button1).setVisibility(View.INVISIBLE);
			}
		}else{
			findViewById(R.id.button1).setEnabled(false);
			findViewById(R.id.button1).setVisibility(View.INVISIBLE);
		}
	}

	public void stopAudio(View view) {
		Intent objIntent = new Intent(this, PlayAudioService.class);
		stopService(objIntent);  

		if(Constants.IS_FIRST_PLALYED){
			//setup for the 7AM 
			Calendar newcal  = Calendar.getInstance();
			newcal.set(newcal.get(Calendar.YEAR), 3, 24, 7, 0, 0);

			Calendar current  = Calendar.getInstance();
			if(newcal.after(current)){
				Intent intent = new Intent(this, PlayAudioService.class);
				PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

				AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
				alarm.set(AlarmManager.RTC_WAKEUP, newcal.getTimeInMillis(),  pintent);
			}
		}




	}

	MediaPlayer objPlayer;

	public void additionalAudio(View view){
		if(mPassword.getText().toString().trim().equalsIgnoreCase("143")){
			if(objPlayer==null ){
				objPlayer = MediaPlayer.create(this,R.raw.hbd);
				objPlayer.start();
			}
		}else{
			Toast.makeText(this, "Code not valid", Toast.LENGTH_LONG).show();
		}
	}
	public void onStop(){
		if(objPlayer!=null){
			objPlayer.stop();
			objPlayer.release();
			objPlayer=null;
		}
		super.onStop();
	}

	public void additionalStop(View view){
		if(objPlayer!=null){
			objPlayer.stop();
			objPlayer.release();
			objPlayer=null;
		}
	}
}
