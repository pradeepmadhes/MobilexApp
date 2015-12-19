package com.mobilex.lawmobilelibrary.activities;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mobilex.lawmobilelibrary.persistent.SharedPersistent;

public class ScheduleList2Activity extends ListActivity 
{
    private int rawId = -1;
    
    private String selectedIndex = "";
    
    private ArrayList<String> schList = null;
    
    private ArrayAdapter adapterForms = null;
    
    private Thread  thdSrh = null;
    private Message msgSrh = null;
    private ProgressDialog diaSrh = null;
    
    @Override
    public void onCreate(Bundle icicle) 
    {
        super.onCreate(icicle);
        
        Bundle bundle = getIntent().getExtras();
        selectedIndex = bundle.getString("selectedSchedule");
        this.setTitle(selectedIndex); 
        
        rawId = SharedPersistent.getRawID();          
        Log.i("#SecNsec_App#","---File Raw ID---" + rawId);
        
        showDialog(0);
        thdSrh = new Thread()
        {    
            @Override
            public void run()
            {                
                schList = new ArrayList<String>();
                
                switch(rawId)
                {    
                    case R.raw.limitation_act_1963:                        
                            schList.add("FIRST DIVISION - SUITS");
                            schList.add("SECOND DIVISION - APPEALS");
                            schList.add("THIRD DIVISION - APPLICATIONS");                       
                        break;
                }
               
                adapterForms = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, schList);                
                
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
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {     
    	selectedIndex = schList.get(position);
    	
    	 if(selectedIndex.equals("SECOND DIVISION - APPEALS"))
         {
             Intent intentSubActs = new Intent(getApplicationContext(), ScheduleActivity.class);
             intentSubActs.putExtra("selectedSchedule", selectedIndex);
             intentSubActs.putExtra("schedulePosition", "");
             startActivity(intentSubActs);
         }
        
        else if(selectedIndex.equals("FIRST DIVISION - SUITS") || selectedIndex.equals("THIRD DIVISION - APPLICATIONS"))
        {
            Intent intentSubActs = new Intent(getApplicationContext(), ScheduleListActivity.class);
            intentSubActs.putExtra("selectedSchedule", selectedIndex);
            startActivity(intentSubActs);
        }

//        Intent intentSubActs = new Intent(getApplicationContext(), ScheduleActivity.class);
//        intentSubActs.putExtra("selectedSchedule", selectedIndex);
//        intentSubActs.putExtra("schedulePosition", String.valueOf(position + 1));
//        startActivity(intentSubActs);
    }
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Log.i("#SecNsec_App#","---Search Handler Entered---");
            
            String loginmsg = (String)msg.obj;           
            
            if(loginmsg.equals("Get")) 
            {                       
                removeDialog(0);
                setListAdapter(adapterForms);
            }
        }
    }; 
}