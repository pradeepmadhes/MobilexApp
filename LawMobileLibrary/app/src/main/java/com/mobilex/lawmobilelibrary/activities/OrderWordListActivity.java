package com.mobilex.lawmobilelibrary.activities;

import android.app.TabActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TabHost;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.InputSource;

import com.mobilex.lawmobilelibrary.parser.XmlParser;
import com.mobilex.lawmobilelibrary.util.CommonVariables;

public class OrderWordListActivity extends TabActivity
{
    private Thread         thdSrh = null;
    private Message        msgSrh = null;
    private ProgressDialog diaSrh = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offence_tab);
        
        Bundle bundleActs = getIntent().getExtras();
        String selectedLabel = bundleActs.getString("selectedLabel");
        
        this.setTitle(selectedLabel);
        
        showDialog(0);
        thdSrh = new Thread()
        {    
            @Override
            public void run()
            {
                CommonVariables.LIST_SECTION_VALUES  = new ArrayList<String>();
                CommonVariables.LIST_SECTION_CONTENT = new ArrayList<String>();
                CommonVariables.MAP_KEY_ID  = new HashMap<String, String>();
                CommonVariables.MAP_KEY_SNO = new HashMap<String, String>();
                
                InputStream inpstreamXml = getResources().openRawResource(R.raw.y_order_rule_with_content);      
                XmlParser.getParserList(new InputSource(inpstreamXml));              
                
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
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Log.i("#SecNsec_App#","---Search Handler Entered---");
            
            String loginmsg=(String)msg.obj;
            
            if(loginmsg.equals("Get")) 
            {            
                TabHost tabHost = getTabHost();
                TabHost.TabSpec spec;
                Intent intent;

//                intent = new Intent().setClass(getApplicationContext(), OrderIDActivity.class);
//                spec = tabHost.newTabSpec("Order").setIndicator("Order").setContent(intent);
//                tabHost.addTab(spec);

                intent = new Intent().setClass(getApplicationContext(), OrderListActivity.class);
                spec = tabHost.newTabSpec("Search by Order").setIndicator("Search by Order").setContent(intent);
                tabHost.addTab(spec);
                
                intent = new Intent().setClass(getApplicationContext(), OrderWordActivity.class);
                spec = tabHost.newTabSpec("Search by Word").setIndicator("Search by Word").setContent(intent);
                tabHost.addTab(spec);

                removeDialog(0);
            }
        }
    }; 
}