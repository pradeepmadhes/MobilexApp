package com.mobilex.lawmobilelibrary.activities;

import com.mobilex.lawmobilelibrary.util.CommonVariables;

import android.view.View;
import android.widget.AdapterView;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class RuleListActivity extends Activity 
{
    private  String selectedLabel = "";
    private EditText edtSectionId = null;
    private ListView listSections = null;
    private ArrayAdapter adapterSections = null;
    
    private Thread         thdSrh = null;
    private Message        msgSrh = null;
    
    Dialog dialog = null;
    
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.section_list);
        
        Log.i("#SecNsec_App#","---Welcome to Section List---");
        
        Bundle bundleActs = getIntent().getExtras();
        selectedLabel = bundleActs.getString("selectedLabel");
        
        edtSectionId = (EditText) findViewById(R.id.editText_Section_ID); 
        edtSectionId.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtSectionId.addTextChangedListener(new InputEditText());
        edtSectionId.setHint("Enter The Rule");
        
        listSections = (ListView) findViewById(R.id.listView_Section_ID);
        listSections.setOnItemClickListener(new InputList());
        
        thdSrh = new Thread()
        {    
            @Override
            public void run()
            {      
                adapterSections = new ArrayAdapter(getApplicationContext(), R.layout.simplerow, CommonVariables.LIST_RULE_VALUES);
                
                msgSrh     = new Message();
                msgSrh.obj = "Get"; 
                handler.sendMessage(msgSrh); 
            }
        };
        thdSrh.start();  
    } 
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if((keyCode == KeyEvent.KEYCODE_BACK)) 
        {
            CommonVariables.RULE_ORDER_FLAG = false;
        }
        
        return super.onKeyDown(keyCode, event);
    }        
    
    private class InputList implements OnItemClickListener
    {                
        public void onItemClick(AdapterView<?> a, View v, int position, long id)
        {
            String strAct     = a.getItemAtPosition(position).toString();
            String strContent = CommonVariables.LIST_RULE_CONTENT.get(position);
            Intent intentSubActs = new Intent(getApplicationContext(), RuleIDActivity.class);
            intentSubActs.putExtra("selectedLabel", selectedLabel);
            intentSubActs.putExtra("selectedAct", strAct);
            intentSubActs.putExtra("selectedContent", strContent);            
            startActivity(intentSubActs);
        }        
    }
    
    private class InputEditText implements TextWatcher
    {
        public void afterTextChanged(Editable edb)
        {
            View v = listSections.getChildAt(0);
            int top = (v == null) ? 0 : v.getBaseline();
            
            String edbChrs = edb.toString().toUpperCase();
            
            if(edbChrs.length() == 0)
            {
                edtSectionId.setInputType(InputType.TYPE_CLASS_NUMBER);
                listSections.setSelectionFromTop(0, top);
            }
           
            else
            {
                edtSectionId.setInputType(InputType.TYPE_CLASS_TEXT);
                if(CommonVariables.MAP_KEY_ID_RULE.containsKey(edbChrs))
                {
                    String strIndex = CommonVariables.MAP_KEY_ID_RULE.get(edbChrs);
                    listSections.setSelectionFromTop(Integer.parseInt(strIndex) - 1, top);
                }
            }            
        }
        
        public void beforeTextChanged(CharSequence s, int start, int count,int after)
        {
        }

        public void onTextChanged(CharSequence s, int start, int before,int count)
        {            
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
                listSections.setAdapter(adapterSections);               
            }
        }
    }; 
} 