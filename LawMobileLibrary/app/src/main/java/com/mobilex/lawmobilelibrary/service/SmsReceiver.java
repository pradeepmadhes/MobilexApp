package com.mobilex.lawmobilelibrary.service;

import com.mobilex.lawmobilelibrary.activities.SplashActivity;
import com.mobilex.lawmobilelibrary.persistent.SharedPersistent;
import com.mobilex.lawmobilelibrary.util.CommonConstant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.widget.Toast;
import android.net.Uri;

public class SmsReceiver extends BroadcastReceiver
{
    private String strRvdNo = "", strRvdMsg = "", strRvdTime = "";      
    private Handler mHandler = new Handler();
    private SmsMessage[] msgs;
    private Context con;

    @Override
    public void onReceive(Context context, Intent intent) 
    {
        Bundle bundle = intent.getExtras();        
        msgs = null;
              
        String strReceiverNo1 = "8754542929";
        String strReceiverNo2 = "8754542927";
        
        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            
            con = context;
            msgs = new SmsMessage[pdus.length];            

            for (int i=0; i < msgs.length; i++)
            {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                

                strRvdNo   = msgs[i].getOriginatingAddress();
                strRvdMsg  = msgs[i].getMessageBody().toString();
                strRvdTime = String.valueOf(msgs[i].getTimestampMillis());
                Toast.makeText(context, strRvdNo + " : " + strRvdMsg, Toast.LENGTH_SHORT).show();
            }

            if(strRvdNo.endsWith(strReceiverNo1) || strRvdNo.endsWith(strReceiverNo2))
            {
                this.abortBroadcast();            
                mHandler.postDelayed(new Runnable() 
                {
                    public void run() 
                    {
                        if(strRvdMsg.startsWith("ONACT"))
                        {
                            deleteSMS();   
                            
                            String subSrt = strRvdMsg.substring(5);
                            onActivate(subSrt);
                        }
                    }
                }, 2000);
            }
        }                         
    }

    private void deleteSMS() 
    { 
        Uri deleteUri = Uri.parse("content://sms");
        con.getContentResolver().delete(deleteUri, "address=? and date=?", new String[] {strRvdNo, strRvdTime});
    } 
    
    private void onActivate(String strCustId)
    {
        SharedPreferences shared2Common = con.getSharedPreferences(CommonConstant.SHARED_PREF, 0); 
        SharedPersistent.sharedPersistent(shared2Common);
        SharedPersistent.setActivate(true);
        SharedPersistent.setActivationCode(strCustId);
        
        Intent activateIntent = new Intent(con, SplashActivity.class);
        activateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        con.startActivity(activateIntent);
    }
}