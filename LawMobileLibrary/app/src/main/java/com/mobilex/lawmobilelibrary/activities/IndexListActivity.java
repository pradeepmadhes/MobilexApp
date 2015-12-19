package com.mobilex.lawmobilelibrary.activities;

import com.mobilex.lawmobilelibrary.persistent.SharedPersistent;
import com.mobilex.lawmobilelibrary.util.CommonVariables;

import android.view.View;
import android.widget.AdapterView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class IndexListActivity extends Activity 
{
    private String selectedLabel = "";
    private ListView listSections = null;
    private ArrayAdapter adapterSections = null;
    
    private Thread  thdSrh = null;
    private Message msgSrh = null;
    
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.index_section_list);
        
        Log.i("#SecNsec_App#","---Welcome to Section List---"); 
        
        Bundle bundleActs = getIntent().getExtras();
        selectedLabel = bundleActs.getString("selectedLabel");
        
        this.setTitle(selectedLabel);
        
        listSections = (ListView) findViewById(R.id.listView_Section_ID_Index);
        listSections.setOnItemClickListener(new InputList());
        
        thdSrh = new Thread()
        {    
            @Override
            public void run()
            {                               
                adapterSections = new ArrayAdapter(getApplicationContext(), R.layout.simplerow, CommonVariables.LIST_TEMP_VALUES);
                
                msgSrh     = new Message();
                msgSrh.obj = "Get"; 
                handler.sendMessage(msgSrh); 
            }
        };
        thdSrh.start();  
    }  
    
    private class InputList implements OnItemClickListener
    {                
        public void onItemClick(AdapterView<?> a, View v, int position, long id)
        {
            boolean flagIpc = false;
            int rawId = SharedPersistent.getRawID();  
        
            if(rawId == R.raw.indian_penal_code_1860)
                flagIpc = true;
            
            String strAct     = a.getItemAtPosition(position).toString();
            String strContent = CommonVariables.LIST_TEMP_CONTENT.get(position);
            Intent intentSubActs = new Intent(getApplicationContext(), IndexIDActivity.class);
            intentSubActs.putExtra("selectedLabel", selectedLabel);
            intentSubActs.putExtra("selectedAct", strAct);
            intentSubActs.putExtra("selectedContent", strContent);            
            startActivity(intentSubActs);
        }        
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
                listSections.setAdapter(adapterSections);              
            }
        }
    }; 
} 