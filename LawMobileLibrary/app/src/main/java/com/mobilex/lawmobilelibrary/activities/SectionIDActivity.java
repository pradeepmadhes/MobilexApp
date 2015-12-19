package com.mobilex.lawmobilelibrary.activities; 

import com.mobilex.lawmobilelibrary.util.CommonVariables;

import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class SectionIDActivity extends Activity 
{
    int indexStart = 0, indexLast = 1;
    
    private boolean flagIpc = false;
    private String strHead = "", strBody = "";
    
    private WebView  webviewContent = null;
    private EditText edtSectionId   = null;
    private Button   btnPrev        = null, btnNext       = null;
    
    private Thread         thdSrh = null;
    private Message        msgSrh = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.section_id);
        
        Log.i("#SecNsec_App#","---Welcome to Section ID Screen---");
        
        this.setTitle("Search by Section");
        
        Bundle bundleActs = getIntent().getExtras();        
        flagIpc = bundleActs.getBoolean("ifIndianPenalCode");
        strHead = bundleActs.getString("selectedAct");
        strBody = bundleActs.getString("selectedContent");
        String selectedLabel = bundleActs.getString("selectedLabel");
        this.setTitle(selectedLabel);
        
        webviewContent = (WebView) findViewById(R.id.webview_section_content_view);
        webviewContent.getSettings().setJavaScriptEnabled(true);
        webviewContent.getSettings().setBuiltInZoomControls(true);
        
        edtSectionId = (EditText) findViewById(R.id.edt_section_id);
        edtSectionId.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtSectionId.addTextChangedListener(new InputEditText());
        
        btnPrev = (Button) findViewById(R.id.btn_prev);
        btnPrev.setOnClickListener(new InputButtons());
        
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new InputButtons());
        
        buttonOff();
        
        thdSrh = new Thread()
        {    
            @Override
            public void run()
            {
                indexLast = CommonVariables.MAP_KEY_SNO.size() - 1;
                Log.i("#SecNsec_App#","---Section ID Size---" + indexLast);
                
                msgSrh     = new Message();
                msgSrh.obj = "Get"; 
                handler.sendMessage(msgSrh); 
            }
        };
        thdSrh.start();  
    }       
    
    private class InputEditText implements TextWatcher
    {
        public void afterTextChanged(Editable edb)
        {            
            String edbChrs = edb.toString().toUpperCase();
            
            if(edbChrs.length() == 0)
            {
                edtSectionId.setInputType(InputType.TYPE_CLASS_NUMBER);
                viewContent("View Section ID & Content", "Please enter the correct section id");  
                buttonOff();
            }
           
            else
            {
                edtSectionId.setInputType(InputType.TYPE_CLASS_TEXT);
                
                if(CommonVariables.MAP_KEY_ID.containsKey(edbChrs))
                {
                    String strIndex = CommonVariables.MAP_KEY_ID.get(edbChrs);
                    int    inpIndex = Integer.parseInt(strIndex) - 1;
                    String strHead  = CommonVariables.LIST_SECTION_VALUES.get(inpIndex);
                    String strBody  = CommonVariables.LIST_SECTION_CONTENT.get(inpIndex);
                    
                    viewContent(strHead, strBody);        
                    
                    if(inpIndex == indexStart)
                    {
                        btnPrev.setEnabled(false);
                        btnNext.setEnabled(true);
                    }
                    
                    else if(inpIndex == indexLast)
                    {
                        btnPrev.setEnabled(true);
                        btnNext.setEnabled(false);
                    }
                    
                    else
                        buttonOn();
                }               
                
                else
                {
                    viewContent("Not Available in Section List", "Please enter the correct section id");                    
                    buttonOff();
                }
            }            
        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
        {           
        }

        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
        {            
        }
    }
    
    private void viewContent(String inpHead, String inpBody)
    {
        String inpHtml = "<html>"
                       + "<body>"
                       + "<b>" + inpHead + "</b>"
                       +"<p><hr></p>"
                       + "<p>" + inpBody + "</p>"
                       + "</body>"
                       + "</html>";
        
        webviewContent.loadData(inpHtml, "text/html", "UTF-8");       
        webviewContent.requestFocus();
    }
    
    private void buttonOn()
    {
        btnPrev.setEnabled(true);
        btnNext.setEnabled(true);
    }
    
    private void buttonOff()
    {
        btnPrev.setEnabled(false);
        btnNext.setEnabled(false);
    }
    
    private class InputButtons implements OnClickListener
    {
        public void onClick(View v) 
        {
            int inpIndex = -1;
            String strCurrent = "", strIndex = "", strSno = "", strId  = "";
            
            strCurrent = edtSectionId.getText().toString().toUpperCase();
            strIndex   = CommonVariables.MAP_KEY_ID.get(strCurrent);    
            
            switch(v.getId())
            {
                case R.id.btn_prev:
                                    inpIndex = Integer.parseInt(strIndex) - 1;
                                    strSno   = String.valueOf(inpIndex);
                                    strId    = CommonVariables.MAP_KEY_SNO.get(strSno);
                                    edtSectionId.setText(strId);
                                    break;
                    
                case R.id.btn_next:
                                    inpIndex = Integer.parseInt(strIndex) + 1;
                                    strSno   = String.valueOf(inpIndex);
                                    strId    = CommonVariables.MAP_KEY_SNO.get(strSno);
                                    edtSectionId.setText(strId);
                                    break;
            }
            
        }        
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
                String strSub = strHead.substring(0, strHead.indexOf(":")).trim();
                edtSectionId.setText(strSub);    
                edtSectionId.setFocusable(false);
                edtSectionId.setVisibility(View.INVISIBLE);
                viewContent(strHead, strBody);
            }
        }
    }; 
}