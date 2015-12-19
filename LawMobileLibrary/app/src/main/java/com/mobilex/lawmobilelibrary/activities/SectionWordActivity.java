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
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.InputSource;

import com.mobilex.lawmobilelibrary.parser.XmlParser;
import com.mobilex.lawmobilelibrary.persistent.SharedPersistent;
import com.mobilex.lawmobilelibrary.util.CommonVariables;

public class SectionWordActivity extends Activity implements RadioGroup.OnCheckedChangeListener
{
    private String selectedLabel = "";
    private EditText edtSectionWord = null;
    private Button   btnSearch    = null;
    private RadioGroup radioSearch = null;
    private RadioGroup radioFull = null;
    private RadioGroup radioShort = null;
    private RadioButton radioSelected = null;
    private ListView listSections = null;
    private ArrayAdapter adapterSections = null;
    
    private Thread         thdSrh = null;
    private Message        msgSrh = null;
    private ProgressDialog diaSrh = null;
    
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.section_word);
        
        Log.i("#SecNsec_App#","---Welcome to Section word---");
        
        edtSectionWord = (EditText) findViewById(R.id.edt_section_word); 
        edtSectionWord.setInputType(InputType.TYPE_CLASS_TEXT);
        edtSectionWord.addTextChangedListener(new InputEditText());
        
        radioSearch = (RadioGroup) findViewById(R.id.radioSearch);
//        radioFull   = (RadioButton) findViewById(R.id.radioFullText);
//        radioShort  = (RadioButton) findViewById(R.id.radioShortTitle);
        
        radioSearch.setOnCheckedChangeListener(this);
        
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new InputButtons());
        
        listSections = (ListView) findViewById(R.id.listView_Section_Word);
        listSections.setOnItemClickListener(new InputList());
        
        Bundle bundleActs = getIntent().getExtras();
        selectedLabel = bundleActs.getString("selectedLabel");
        
        this.setTitle(selectedLabel);
        
        if(selectedLabel.equals("Constitution of India"))
        {
            edtSectionWord.setHint("Enter The Article Word");
        }
       
        showDialog(0);
        thdSrh = new Thread()
        {    
            @Override
            public void run()
            {
                int rawId = SharedPersistent.getRawID();                
                Log.i("#SecNsec_App#","---File Raw ID---" + rawId);
                
                CommonVariables.LIST_SECTION_VALUES  = new ArrayList<String>();
                CommonVariables.LIST_SECTION_CONTENT = new ArrayList<String>();
                CommonVariables.MAP_KEY_ID  = new HashMap<String, String>();
                CommonVariables.MAP_KEY_SNO = new HashMap<String, String>();
                
                InputStream inpstreamXml = getResources().openRawResource(rawId);      
                XmlParser.getParserList(new InputSource(inpstreamXml));          
                
                adapterSections = new ArrayAdapter(getApplicationContext(), R.layout.simplerow, CommonVariables.LIST_SECTION_VALUES);
               
                msgSrh     = new Message();
                msgSrh.obj = "Get"; 
                handler.sendMessage(msgSrh); 
            }
        };
        thdSrh.start();  
        
//        thdSrh = new Thread()
//        {    
//            @Override
//            public void run()
//            {
//                adapterSections = new ArrayAdapter(getApplicationContext(), R.layout.simplerow, CommonVariables.LIST_SECTION_VALUES);
//                
//                msgSrh     = new Message();
//                msgSrh.obj = "Get"; 
//                handler.sendMessage(msgSrh); 
//            }
//        };
//        thdSrh.start();  
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

    public void onCheckedChanged(RadioGroup arg0, int arg1) 
    {
        
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
            String strId      = strAct.substring(0,strAct.indexOf(":")).trim();        
            String strSno     = CommonVariables.MAP_KEY_ID.get(strId);
            String strContent = CommonVariables.LIST_SECTION_CONTENT.get(Integer.parseInt(strSno) - 1);
            Intent intentSubActs = new Intent(getApplicationContext(), ContentActivity.class);
            intentSubActs.putExtra("selectedLabel", selectedLabel);
            intentSubActs.putExtra("ifIndianPenalCode", flagIpc);
            intentSubActs.putExtra("selectedAct", strAct);
            intentSubActs.putExtra("selectedContent", strContent);
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
                                        
                                        int selectedId = radioSearch.getCheckedRadioButtonId();			
                                        radioSelected = (RadioButton) findViewById(selectedId);                                        
                                        
                                        if(radioSelected.getText().equals("Full Text"))
                                        {
                                            for(int i=0; i < CommonVariables.LIST_SECTION_VALUES.size(); i++)
                                            {                
                                                String strValue = CommonVariables.LIST_SECTION_VALUES.get(i);
                                                String subStr1 = strValue.substring(strValue.indexOf(":")+2).trim();     
                                                String strContent = CommonVariables.LIST_SECTION_CONTENT.get(i);

                                                if(null != subStr1)
                                                {
                                                    int index = subStr1.toLowerCase().indexOf(inpWord.toLowerCase());
                                                    int indexContent = strContent.toLowerCase().indexOf(inpWord.toLowerCase());

                                                    if(index != -1 || indexContent != -1)
                                                    {
                                                        listTemp.add(strValue);
                                                    }
                                                }
                                            }
                                        }
                                        
                                        else if(radioSelected.getText().equals("Short Title"))
                                        {                                            
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
                removeDialog(0);
            }
        }
    }; 
} 