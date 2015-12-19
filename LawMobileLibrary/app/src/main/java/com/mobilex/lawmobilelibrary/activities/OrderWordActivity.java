package com.mobilex.lawmobilelibrary.activities;

import android.view.View;
import android.widget.AdapterView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import com.mobilex.lawmobilelibrary.util.CommonVariables;

public class OrderWordActivity extends Activity 
{
    private EditText edtSectionWord = null;
    private Button   btnSearch    = null;
    private ListView listSections = null;
    private ArrayAdapter adapterSections = null;
    
    private Thread  thdSrh = null;
    private Message msgSrh = null;
    
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.section_word);
        
        Log.i("#SecNsec_App#","---Welcome to Section word---");
        
        edtSectionWord = (EditText) findViewById(R.id.edt_section_word); 
        edtSectionWord.setInputType(InputType.TYPE_CLASS_TEXT);
        edtSectionWord.addTextChangedListener(new InputEditText());
        edtSectionWord.setHint("Enter The Order Word");
        
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new InputButtons());
        
        listSections = (ListView) findViewById(R.id.listView_Section_Word);
        listSections.setOnItemClickListener(new InputList());
        
        thdSrh = new Thread()
        {    
            @Override
            public void run()
            {              
                adapterSections = new ArrayAdapter(getApplicationContext(), R.layout.simplerow, CommonVariables.LIST_SECTION_VALUES);
                
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
            String strAct     = a.getItemAtPosition(position).toString();
            Intent intentSubActs = new Intent(getApplicationContext(), RuleWordListActivity.class);
            intentSubActs.putExtra("selectedAct", strAct);
            startActivity(intentSubActs);
        }        
    }
    
    private class InputEditText implements TextWatcher
    {
        public void afterTextChanged(Editable edb)
        {
            String edbChrs = edb.toString().toUpperCase();
            
            if(edbChrs.length() == 0)
            {
                adapterSections = new ArrayAdapter(getApplicationContext(), R.layout.simplerow, CommonVariables.LIST_SECTION_VALUES);
                listSections.setAdapter(adapterSections);                
                listSections.invalidateViews();
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count,int after)
        {
        }

        public void onTextChanged(CharSequence s, int start, int before,int count)
        {            
        }
    }  
    
    private class InputButtons implements OnClickListener
    {
        public void onClick(View v) 
        {
            switch(v.getId())
            {
                case R.id.btn_search:
                                        String inpWord = edtSectionWord.getText().toString();
                                        ArrayList<String> listTemp = new ArrayList<String>();
                
                                        for(int i=0; i < CommonVariables.LIST_SECTION_VALUES.size(); i++)
                                        {                
                                            String strValue = CommonVariables.LIST_SECTION_VALUES.get(i);
                                            String subStr1 = strValue.substring(strValue.indexOf(":")+2).trim();     

                                            if(null != subStr1)
                                            {
                                                int index = subStr1.toLowerCase().indexOf(inpWord.toLowerCase());

                                                if(index != -1)
                                                {
                                                    listTemp.add(strValue);
                                                }
                                            }
                                        }
                                        
                                        if(!listTemp.isEmpty())
                                        {
                                            adapterSections = new ArrayAdapter(getApplicationContext(), R.layout.simplerow, listTemp);
                                            listSections.setAdapter(adapterSections);     
                                            listSections.invalidateViews();
                                        }
                                        
                                        else 
                                        {
                                            adapterSections = new ArrayAdapter(getApplicationContext(), R.layout.simplerow);
                                            listSections.setAdapter(adapterSections);     
                                            listSections.invalidateViews();
                                        }
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
                listSections.setAdapter(adapterSections);
            }
        }
    }; 
} 