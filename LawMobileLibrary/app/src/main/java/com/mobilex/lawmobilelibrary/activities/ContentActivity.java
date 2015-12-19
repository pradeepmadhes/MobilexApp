package com.mobilex.lawmobilelibrary.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;

public class ContentActivity extends Activity 
{
    private String strHead = "", strBody = "", strHtml = "";
    
    private Thread         thdSrh = null;
    private Message        msgSrh = null;
    private ProgressDialog diaSrh = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   
        
        Bundle bundleActs = getIntent().getExtras();    
        strHead = bundleActs.getString("selectedAct");
        strBody = bundleActs.getString("selectedContent");
        String selectedLabel = bundleActs.getString("selectedLabel");
        
        this.setTitle(selectedLabel);
        
        showDialog(0);
        thdSrh = new Thread()
        {    
            @Override
            public void run()
            {
                strHtml = "<html>"
                        + "<body>"
                        + "<b><span style='color:#0070C0'>" + strHead + "</span></b>"
                        + "<p><hr></p>"
                        + "<p>" + strBody + "</p>"
                        + "</body>"
                        + "</html>";                
                
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
                WebView webHtml = (WebView)findViewById(R.id.webkit_content);        
                webHtml.getSettings().setJavaScriptEnabled(true);
                webHtml.getSettings().setBuiltInZoomControls(true);
//                webHtml.loadData(strHtml, "text/html", "UTF-8");
                webHtml.loadDataWithBaseURL("file:///android_asset/", strHtml, "text/html", "UTF-8", null);
                removeDialog(0);
            }
        }
    }; 
}