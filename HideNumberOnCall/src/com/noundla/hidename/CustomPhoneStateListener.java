package com.noundla.hidename;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class CustomPhoneStateListener extends PhoneStateListener {
	private static final String TAG = CustomPhoneStateListener.class.getSimpleName();
	Context context; //Context to make Toast if required 
	private static final String EXTRA_RING_VOLUME = "RING_VOLUME";
	private static final String SHARED_PREFERENCES_NAME = "HIDE_NAME";
	public static int count = 0;
	public static boolean ringing = false;
	public static boolean idle = false;
	public static boolean hook = false;
	public static boolean isOutgoing = false;

	

	private boolean isSilentModeEnabled = false;

	private WindowManager wm;
	private View view;


	//	public static boolean callStatus;
	public CustomPhoneStateListener(Context context) {
		super();
		this.context = context;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {

		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		switch (state){
		case TelephonyManager.CALL_STATE_IDLE:
			idle = true;
			hook = false;
			ringing = false;
			if(isSilentModeEnabled){
				isSilentModeEnabled = false;
				//reset the ring volume to previous
				AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
				audioManager.setRingerMode(getIntFromSP(context, EXTRA_RING_VOLUME));
			}
			// removing the view (block/cancel) when call is cut from other end or from our end....
			try{
				if(view != null)
					wm.removeView(view);
			}catch (Exception e) {
				e.printStackTrace();
			}
			isOutgoing = true;
			Log.v(TAG,"Phone state Idle");
			break;


		case TelephonyManager.CALL_STATE_OFFHOOK:
			hook = true;
			idle = false;
			ringing = false;
			Log.v(TAG,"Phone state Off hook");
			hideMuteButton();
			break;

		case TelephonyManager.CALL_STATE_RINGING:
			hook = false;
			idle = false;
			//when Ringing
			ringing = true;
			
			try{
				wm.removeView(view);
			}catch (Exception e) {

			}
			view = View.inflate(context, R.layout.hide_screen, null);
			showView(view);

			break;
		}
	}



	private void showView(View view){
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT |
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.RGB_888);
		params.x = 0;
		params.height = LayoutParams.WRAP_CONTENT;
		params.width = LayoutParams.MATCH_PARENT;
		params.format = PixelFormat.TRANSLUCENT;
		final Context ct =context;

		params.softInputMode=WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
		params.gravity = Gravity.TOP;
		params.setTitle("Call Block!");



		final ImageButton silent=(ImageButton)view.findViewById(R.id.silent);
		final ImageButton showNumber=(ImageButton)view.findViewById(R.id.show);
		showNumber.setVisibility(View.VISIBLE);
		silent.setVisibility(View.VISIBLE);
		showNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNumber.setVisibility(View.GONE);
			}
		});

		silent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
				if(audioManager!=null && audioManager.getRingerMode()!= AudioManager.RINGER_MODE_SILENT){
					saveIntInSP(context, EXTRA_RING_VOLUME, audioManager.getRingerMode());
					audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					isSilentModeEnabled = true;
				}
				hideMuteButton();
			}
		});

		wm.addView(view, params);
	}

	private void hideMuteButton(){
		try {
			view.findViewById(R.id.silent).setVisibility(View.GONE);
		} catch (NullPointerException e) {
			//nope.... :(
		}catch (Exception e) {
			e.printStackTrace();
		}
	}



	/**
	 * Retrieve Integer value from SharedPreference for the given key
	 */
	public static void saveIntInSP(Context context, String key, int value) {
		SharedPreferences preferences = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, android.content.Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	/**
	 * Retrieve integer value from SharedPreference for the given key
	 */
	public static int getIntFromSP(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences(
				SHARED_PREFERENCES_NAME, android.content.Context.MODE_PRIVATE);
		return preferences.getInt(key, 0);
	}

}

