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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.InputSource;

import com.mobilex.lawmobilelibrary.parser.XmlParser;
import com.mobilex.lawmobilelibrary.persistent.SharedPersistent;
import com.mobilex.lawmobilelibrary.util.CommonVariables;

public class FormsActivity extends ListActivity 
{
    private ArrayAdapter<String> adapterForms = null;
    
    private Thread  thdSrh = null;
    private Message msgSrh = null;
    private ProgressDialog diaSrh = null;
    
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);  
        
        Bundle bundle = getIntent().getExtras();
        String strTitle = bundle.getString("selectedSchedule");
        this.setTitle(strTitle); 
        
        showDialog(0);
        thdSrh = new Thread()
        {    
            @Override
            public void run()
            {                               
                int rawId = SharedPersistent.getRawID();                
                Log.i("#SecNsec_App#","---File Raw ID---" + rawId);                
                              
                CommonVariables.LIST_RULE_VALUES  = new ArrayList<String>();
                CommonVariables.LIST_RULE_CONTENT = new ArrayList<String>();
                CommonVariables.MAP_KEY_ID_RULE  = new HashMap<String, String>();
                CommonVariables.MAP_KEY_SNO_RULE = new HashMap<String, String>();                
                
                InputStream inpstreamXml = getResources().openRawResource(rawId+2);      
                XmlParser.getParserList(new InputSource(inpstreamXml));          
                
                for(int i=0; i < CommonVariables.LIST_RULE_VALUES.size(); i++)
                    Log.i("#SecNsec_App#","---Form---" + i + " : " + CommonVariables.LIST_RULE_VALUES.get(i));     
                
                adapterForms = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, CommonVariables.LIST_RULE_VALUES);                
                
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
        String strLabel = CommonVariables.LIST_RULE_VALUES.get(position);
        String strContent = CommonVariables.LIST_RULE_CONTENT.get(position);

        Intent intent = new Intent(this, ContentActivity.class);
        intent.putExtra("selectedLabel", "THE SECOND SCHEDULE");
        intent.putExtra("selectedAct", strLabel);
        intent.putExtra("selectedContent", strContent);
        startActivity(intent);
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