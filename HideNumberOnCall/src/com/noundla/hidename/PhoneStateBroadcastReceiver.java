/**
//**************************************************************************************************
//         				Copyright (c) 2013 by ValueLabs
//                				ALL RIGHTS RESERVED
//**************************************************************************************************
//**************************************************************************************************
//
//	Project name			: POC For caller app
//	Class Name 				: PhoneStateBroadcastReceiver
//	Date					: 07 Feb 2013
//	Author 					: R.Rajesh
//	Version					: 1.0                          
//
//***************************************************************************************************
//	Class Description:
// 
//***************************************************************************************************
//	Update history:
//	Date :    			Developer Name :R.Rajesh     Modification Comments :  
//
**/ 
package com.noundla.hidename;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneStateBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new CustomPhoneStateListener(context), PhoneStateListener.LISTEN_CALL_STATE);

    }

}
