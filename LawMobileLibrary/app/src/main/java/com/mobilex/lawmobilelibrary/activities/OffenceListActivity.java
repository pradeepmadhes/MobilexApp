package com.mobilex.lawmobilelibrary.activities;

import android.view.View;
import android.widget.AdapterView;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.xml.sax.InputSource;

import com.mobilex.lawmobilelibrary.parser.XmlParser;
import com.mobilex.lawmobilelibrary.util.CommonVariables;

public class OffenceListActivity extends Activity 
{
    private EditText edtSectionId = null;
    private ListView listSections = null;
    private ArrayAdapter adapterSections = null;
    
    private Thread         thdSrh = null;
    private Message        msgSrh = null;
    private ProgressDialog diaSrh = null;
    
    Dialog dialog = null;
    
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.section_list);
        
        Log.i("#SecNsec_App#","---Welcome to Section List---");
        
        edtSectionId = (EditText) findViewById(R.id.editText_Section_ID); 
        edtSectionId.setHint("Enter the number");
        edtSectionId.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtSectionId.addTextChangedListener(new InputEditText());
        
        TextView txtOffence = (TextView) findViewById(R.id.textView_Section_ID);
        txtOffence.setVisibility(View.GONE);
        
        listSections = (ListView) findViewById(R.id.listView_Section_ID);
        listSections.setOnItemClickListener(new InputList());
        
        showDialog(0);
        thdSrh = new Thread()
        {    
            @Override
            public void run()
            {
                Bundle bundleActs = getIntent().getExtras();
                int selectedId = bundleActs.getInt("selectedRawId");        
                Log.i("#SecNsec_App#","---File Raw ID---" + selectedId);
                
                CommonVariables.LIST_SECTION_VALUES  = new ArrayList<String>();
                CommonVariables.LIST_SECTION_CONTENT = new ArrayList<String>();
                CommonVariables.MAP_KEY_ID  = new HashMap<String, String>();
                
                InputStream inpstreamXml = getResources().openRawResource(selectedId);     
                XmlParser.getParserList(new InputSource(inpstreamXml));

                adapterSections = new ArrayAdapter(getApplicationContext(), R.layout.simplerow, CommonVariables.LIST_SECTION_VALUES);
                
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
    
    private class InputList implements OnItemClickListener
    {                
        public void onItemClick(AdapterView<?> a, View v, int position, long id)
        {
            String strAct     = a.getItemAtPosition(position).toString();
            String strContent = CommonVariables.LIST_SECTION_CONTENT.get(position);
            Intent intentSubActs = new Intent(getApplicationContext(), ContentActivity.class);
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
                if(CommonVariables.MAP_KEY_ID.containsKey(edbChrs))
                {
                    String strIndex = CommonVariables.MAP_KEY_ID.get(edbChrs);
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
                removeDialog(0);
            }
        }
    }; 
} 