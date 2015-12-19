package com.mobilex.lawmobilelibrary.activities;

import com.mobilex.lawmobilelibrary.persistent.SharedPersistent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;

public class ScheduleActivity extends Activity 
{
    private int rawId = -1;
    
    private Thread  thdSrh = null;
    private Message msgSrh = null;
    private ProgressDialog diaSrh = null;
    
    @Override
    public void onCreate(Bundle icicle) 
    {
        super.onCreate(icicle);  
        setContentView(R.layout.schedule);
        
        Bundle bundle = getIntent().getExtras();
        final String strTitle = bundle.getString("selectedSchedule");
        final String strPosition = bundle.getString("schedulePosition");
        this.setTitle(strTitle);      
        
        rawId = SharedPersistent.getRawID();     
        Log.i("#SecNsec_App#","---File Raw ID---" + rawId);
       
        showDialog(0);
        thdSrh = new Thread()
        {    
            @Override
            public void run()
            {      
                WebView webHtml = (WebView) findViewById(R.id.webview_schedule);
                webHtml.getSettings().setJavaScriptEnabled(true);
                webHtml.getSettings().setBuiltInZoomControls(true);        
                webHtml.loadUrl("file:///android_asset/" + getScheduleName(strTitle, strPosition) + ".htm");
                
                msgSrh     = new Message();
                msgSrh.obj = "Get"; 
                handler.sendMessage(msgSrh); 
            }
        };
        thdSrh.start(); 
    }    
    
    @Override
    protected Dialog onCreateDialog(int id) 
    {
        switch (id) 
        {
            case 0: 
            {
                diaSrh = new ProgressDialog(this);
                diaSrh.setMessage("Please wait while loading...");
                diaSrh.setIndeterminate(true);
                diaSrh.setCancelable(true);
                
                return diaSrh;
            } 
        }
        
        return null;
    }  
    
    private String getScheduleName(String inpTitle, String inpPosition)
    {
        String strFileName = "";
        
        switch(rawId)
        {
            case R.raw.code_of_criminal_procedure_act_1973:
                if(inpTitle.equals("THE FIRST SCHEDULE"))
                    strFileName = "code_of_criminal_procedure_act_1973_schedule_1";
                break;
            
            case R.raw.constitution_of_india:
                if(inpTitle.equals("FIRST SCHEDULE"))
                    strFileName = "constitution_of_india_schedule_1_" + inpPosition;

                else if(inpTitle.equals("SECOND SCHEDULE"))
                    strFileName = "constitution_of_india_schedule_2_" + inpPosition;

                else if(inpTitle.equals("THIRD SCHEDULE"))
                    strFileName = "constitution_of_india_schedule_3";

                else if(inpTitle.equals("FOURTH SCHEDULE"))
                    strFileName = "constitution_of_india_schedule_4";

                else if(inpTitle.equals("FIFTH SCHEDULE"))
                    strFileName = "constitution_of_india_schedule_5_" + inpPosition;

                else if(inpTitle.equals("SIXTH SCHEDULE"))
                    strFileName = "constitution_of_india_schedule_6";

                else if(inpTitle.equals("SEVENTH SCHEDULE"))
                    strFileName = "constitution_of_india_schedule_7_" + inpPosition;

                else if(inpTitle.equals("EIGHTH SCHEDULE"))
                    strFileName = "constitution_of_india_schedule_8";

                else if(inpTitle.equals("NINTH SCHEDULE"))
                    strFileName = "constitution_of_india_schedule_9";

                else if(inpTitle.equals("TENTH SCHEDULE"))
                    strFileName = "constitution_of_india_schedule_10";

                else if(inpTitle.equals("ELEVENTH SCHEDULE"))
                    strFileName = "constitution_of_india_schedule_11";

                else if(inpTitle.equals("TWELTH SCHEDULE"))
                    strFileName = "constitution_of_india_schedule_12";
                break;
                
            case R.raw.employees_compensation_act_1923:
                if(inpTitle.equals("FIRST SCHEDULE"))
                    strFileName = "employees_compensation_act_1923_schedule_1_" + inpPosition;

                else if(inpTitle.equals("SECOND SCHEDULE"))
                    strFileName = "employees_compensation_act_1923_schedule_2";

                else if(inpTitle.equals("THIRD SCHEDULE"))
                    strFileName = "employees_compensation_act_1923_schedule_3";

                else if(inpTitle.equals("FOURTH SCHEDULE"))
                    strFileName = "employees_compensation_act_1923_schedule_4";
                break;
                
            case R.raw.limitation_act_1963:
                if(inpTitle.equals("FIRST DIVISION - SUITS"))
                    strFileName = "limitation_act_1963_schedule_1_" + inpPosition;

                else if(inpTitle.equals("SECOND DIVISION - APPEALS"))
                    strFileName = "limitation_act_1963_schedule_2";

                else if(inpTitle.equals("THIRD DIVISION - APPLICATIONS"))
                    strFileName = "limitation_act_1963_schedule_3_"+ inpPosition;
                break;
                
            case R.raw.arbitration_and_conciliation_act_1996:
                if(inpTitle.equals("FIRST SCHEDULE"))
                    strFileName = "arbitration_and_conciliation_act_1996_schedule_1_" + inpPosition;

                else if(inpTitle.equals("SECOND SCHEDULE"))
                    strFileName = "arbitration_and_conciliation_act_1996_schedule_2";

                else if(inpTitle.equals("THIRD SCHEDULE"))
                    strFileName = "arbitration_and_conciliation_act_1996_schedule_3_"+ inpPosition;
                break;
        }
        
        return strFileName;
    }
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            String loginmsg = (String)msg.obj;           
            
            if(loginmsg.equals("Get")) 
            {                       
                removeDialog(0);
            }
        }
    }; 
}