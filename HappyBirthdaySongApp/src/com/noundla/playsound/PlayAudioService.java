package com.noundla.playsound;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.util.Log;

public class PlayAudioService extends Service {
	private static final String LOGCAT = PlayAudioService.class.getSimpleName();
	private MediaPlayer objPlayer;



	public void onCreate(){
		super.onCreate();
		Log.d(LOGCAT, "Service Started!");
		objPlayer = MediaPlayer.create(this,R.raw.hbd);
		objPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				//setup for the 7AM
				Calendar newcal  = Calendar.getInstance();
				newcal.set(newcal.get(Calendar.YEAR), 3, 24, 7, 0, 0);

				Calendar current  = Calendar.getInstance();
				if(newcal.after(current)){
					Intent intent = new Intent(PlayAudioService.this, PlayAudioService.class);
					PendingIntent pintent = PendingIntent.getService(PlayAudioService.this, 0, intent, 0);

					AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
					alarm.set(AlarmManager.RTC_WAKEUP, newcal.getTimeInMillis(),  pintent);
				}
			}
		});

	}

	public int onStartCommand(Intent intent, int flags, int startId){
		if(!Constants.IS_FIRST_PLALYED)
			Constants.IS_FIRST_PLALYED = true;
		
		AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
				audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
				AudioManager.FLAG_SHOW_UI);
		objPlayer.start();
		Log.d(LOGCAT, "Media Player started!");
		if(objPlayer.isLooping() != true){
			Log.d(LOGCAT, "Problem in Playing Audio");
		}
		return 1;
	}

	public void onStop(){
		objPlayer.stop();
		objPlayer.release();
		
		
		
	}

	public void onPause(){
		objPlayer.stop();
		objPlayer.release();
	}
	public void onDestroy(){
		objPlayer.stop();
		objPlayer.release();
	}
	@Override
	public IBinder onBind(Intent objIndent) {
		return null;
	}
}
