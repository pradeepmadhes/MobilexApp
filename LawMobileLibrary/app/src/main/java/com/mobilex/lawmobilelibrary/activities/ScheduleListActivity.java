package com.mobilex.lawmobilelibrary.activities;

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
import java.util.ArrayList;

import com.mobilex.lawmobilelibrary.persistent.SharedPersistent;

public class ScheduleListActivity extends ListActivity 
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
                    case R.raw.constitution_of_india:
                        if(selectedIndex.equals("FIRST SCHEDULE"))
                        {
                            schList.add("I.The States");
                            schList.add("II.The Union territories");
                        }

                        else if(selectedIndex.equals("SECOND SCHEDULE"))
                        {
                            schList.add("PART A");
                            schList.add("PART B");
                            schList.add("PART C");
                            schList.add("PART D");
                            schList.add("PART E");
                        }

                        else if(selectedIndex.equals("FIFTH SCHEDULE"))
                        {
                            schList.add("PART A - General");
                            schList.add("PART B");
                            schList.add("PART C");
                            schList.add("PART D");
                        }

                        else if(selectedIndex.equals("SEVENTH SCHEDULE"))
                        {
                            schList.add("Union List");
                            schList.add("State List");
                            schList.add("Concurrent List");
                        }
                        break;
                        
                    case R.raw.employees_compensation_act_1923:
                        if(selectedIndex.equals("FIRST SCHEDULE"))
                        {
                            schList.add("PART 1");
                            schList.add("PART 2");
                        }
                        break;
                        
                    case R.raw.limitation_act_1963:
                        if(selectedIndex.equals("FIRST DIVISION - SUITS"))
                        {
                            schList.add("Part I - Suits relating to Accounts");
                            schList.add("Part II - Suits relating to Contracts");
                            schList.add("Part III - Suits relating to Declarations");
                            schList.add("Part IV - Suits relating to Decrees and Instruments");
                            schList.add("Part V - Suits relating to Immovable Property");
                            schList.add("Part VI - Suits relating to Movable Property");
                            schList.add("Part VII - Suits relating to Tort");
                            schList.add("Part VIII - Suits relating to Trusts and Trust Property");
                            schList.add("Part IX - Suits relating to Miscellaneous Maters");
                            schList.add("Part X - Suits for which there is no prescribed period");
                        }

                        else if(selectedIndex.equals("THIRD DIVISION - APPLICATIONS"))
                        {
                            schList.add("Part I - Applications in specified cases");
                            schList.add("Part II - Other applications");
                        }
                        break;
                        
                    case R.raw.arbitration_and_conciliation_act_1996:
                        if(selectedIndex.equals("FIRST SCHEDULE"))
                        {
                            schList.add("ARTICLE I");
                            schList.add("ARTICLE II");
                            schList.add("ARTICLE III");
                            schList.add("ARTICLE IV");
                            schList.add("ARTICLE V");
                            schList.add("ARTICLE VI");
                            schList.add("ARTICLE VII");
                            schList.add("ARTICLE VIII");
                            schList.add("ARTICLE IX");
                            schList.add("ARTICLE X");
                            schList.add("ARTICLE XI");
                            schList.add("ARTICLE XII");
                            schList.add("ARTICLE XIII");
                            schList.add("ARTICLE XIV");
                            schList.add("ARTICLE XV");
                            schList.add("ARTICLE XVI");
                        }

                        else if(selectedIndex.equals("THIRD SCHEDULE"))
                        {
                        	schList.add("ARTICLE I");
                            schList.add("ARTICLE II");
                            schList.add("ARTICLE III");
                            schList.add("ARTICLE IV");
                            schList.add("ARTICLE V");
                            schList.add("ARTICLE VI");
                            schList.add("ARTICLE VII");
                            schList.add("ARTICLE VIII");
                            schList.add("ARTICLE IX");
                            schList.add("ARTICLE X");
                            schList.add("ARTICLE XI");
                        }
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
        Intent intentSubActs = new Intent(getApplicationContext(), ScheduleActivity.class);
        intentSubActs.putExtra("selectedSchedule", selectedIndex);
        intentSubActs.putExtra("schedulePosition", String.valueOf(position + 1));
        startActivity(intentSubActs);
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