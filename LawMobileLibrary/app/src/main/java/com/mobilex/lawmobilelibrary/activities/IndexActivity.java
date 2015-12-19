package com.mobilex.lawmobilelibrary.activities;

import com.mobilex.lawmobilelibrary.adapter.IndexAdapter;
import com.mobilex.lawmobilelibrary.parser.XmlParser;
import com.mobilex.lawmobilelibrary.persistent.SharedPersistent;
import com.mobilex.lawmobilelibrary.util.CommonVariables;

import android.view.View;
import android.widget.AdapterView;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.InputStream;
import java.util.ArrayList;

import java.util.HashMap;
import org.xml.sax.InputSource;

public class IndexActivity extends Activity 
{
    private int rawId = -1;
    private String selectedLabel = "", selectedIndexLabel = "";
    
    private ArrayList<String> listTitle = null, listBody = null;
    
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
        setContentView(R.layout.index_list);
        
        Log.i("#SecNsec_App#","---Welcome to Index List---");
        
        Bundle bundleActs = getIntent().getExtras();
        selectedLabel = bundleActs.getString("selectedLabel");
        selectedIndexLabel = bundleActs.getString("selectedIndexLabel");  
        
        this.setTitle(selectedLabel);
        
        listSections = (ListView) findViewById(R.id.listView_index_list);
        listSections.setOnItemClickListener(new InputList());
        
        showDialog(0);
        thdSrh = new Thread()
        {    
            @Override
            public void run()
            {
                rawId = SharedPersistent.getRawID();                
                Log.i("#SecNsec_App#","---File Raw ID---" + rawId);
                
                if(rawId == R.raw.indian_penal_code_1860)
                {                    
                    CommonVariables.IPC_FLAG = true;
                    Log.i("@@SecNsec@@","IPC_FLAG : " + CommonVariables.IPC_FLAG);
                }       

                else
                { 
                    CommonVariables.IPC_FLAG = false;
                    Log.i("$$SecNsec$$","IPC_FLAG : " + CommonVariables.IPC_FLAG);
                }    
                
                CommonVariables.LIST_PART_CONTENT = new ArrayList<String>(); 
                CommonVariables.PART_KEY_ID = new HashMap<String, String>();
                
                InputStream inpstreamXml = getResources().openRawResource(rawId+1);      
                XmlParser.getParserList(new InputSource(inpstreamXml));
                
                CommonVariables.LIST_SECTION_VALUES  = new ArrayList<String>();
                CommonVariables.LIST_SECTION_CONTENT = new ArrayList<String>();
                CommonVariables.MAP_KEY_ID  = new HashMap<String, String>();
                CommonVariables.MAP_KEY_SNO = new HashMap<String, String>();
                
                inpstreamXml = getResources().openRawResource(rawId);      
                XmlParser.getParserList(new InputSource(inpstreamXml));    

                //adapterSections = new ArrayAdapter(getApplicationContext(), R.layout.simplerow, CommonVariables.LIST_PART_CONTENT);
                
                getTitleBody();
                
                msgSrh     = new Message();
                msgSrh.obj = "Get"; 
                handler.sendMessage(msgSrh); 
            }
        };
        thdSrh.start();  
    }  
    
    private void getTitleBody()
    {
        listTitle = new ArrayList<String>();
        listBody = new ArrayList<String>();
        
        for(int i = 0; i < CommonVariables.LIST_PART_CONTENT.size(); i++)
        { 
            String strIndex = CommonVariables.LIST_PART_CONTENT.get(i);
            
            if(strIndex.indexOf(":") != -1)
            {
                String[] arrSplit = strIndex.split(":");
//                Log.i("#" + i + "#" , "Title # " + arrSplit[0].trim() + " # Body # " + arrSplit[1].trim());
                listTitle.add(arrSplit[0].trim());
                listBody.add(arrSplit[1].trim());                
            }
            
            else if(strIndex.endsWith("SCHEDULE") || strIndex.endsWith("schedule"))
            {     
//                Log.i("#____#", "@@@@@@@@@@@@@@@@@@ : " + strIndex);
                listTitle.add(strIndex);
                listBody.add("no_body");                
            }
            
            else 
            {                
//                Log.i("#____#", "%%%%%%%%%%%%%%%% : " + strIndex);
                listTitle.add("no_title");
                listBody.add(strIndex);                
            }
        }
        
//        Log.i("############", "Size 1 : " + listTitle.size());
//        Log.i("############", "Size 2 : " + listBody.size());
//        Log.i("############", "Size 3 : " + CommonVariables.LIST_PART_CONTENT.size());
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
            boolean flag = false;
            String selectedIndex = CommonVariables.LIST_PART_CONTENT.get(position);
            String strSub1 = "", strSub2 = "", indexStart = "", indexEnd = "";
//            Log.i("SecNsec", "---Selected Substr---" + selectedIndex);
            
            if(selectedIndex.indexOf("Sec.") != -1)
            {
                flag = true;
                strSub1 = selectedIndex.substring(selectedIndex.indexOf("Sec.") + 4).trim();
            }                
            
            else if(selectedIndex.indexOf("Art.") != -1)
            {
                flag = true;
                strSub1 = selectedIndex.substring(selectedIndex.indexOf("Art.") + 4).trim(); 
            }
            
            Log.i("SecNsec", "---Selected Substr---" + strSub1);

            if(flag)
            {
                if(strSub1.contains("-"))        
                {
                    String[] strArr = strSub1.split("-");
                    strSub2 = strArr[1].substring(0,strArr[1].indexOf("."));

                    indexStart = strArr[0].trim();
                    indexEnd   = strSub2.trim();

//                    Log.i("SecNsec", "---Start---" + indexStart + "---End---" + indexEnd + "---");
//                    Log.i("SecNsec", "---Start Key---" + CommonVariables.MAP_KEY_ID.get(indexStart) + "---End Key---" + CommonVariables.MAP_KEY_ID.get(indexEnd) + "---");

                    CommonVariables.TEMP_KEY_ID      = new HashMap<String, String>();
                    CommonVariables.LIST_TEMP_VALUES = new ArrayList<String>();
                    CommonVariables.TEMP_KEY_SNO     = new HashMap<String, String>();
                    CommonVariables.LIST_TEMP_CONTENT = new ArrayList<String>();

                    int indexStartNo = Integer.parseInt(CommonVariables.MAP_KEY_ID.get(indexStart));
                    int indexEndNo   = Integer.parseInt(CommonVariables.MAP_KEY_ID.get(indexEnd));
                    int indexCount = 0;

                    for(int i = indexStartNo - 1; i < indexEndNo; i++)
                    {
                        String strKey = CommonVariables.MAP_KEY_SNO.get(String.valueOf(i+1));
                        String strVal = String.valueOf(++indexCount);
    //                    Log.i("SecNsec", "---Key---" + strKey + "Value---" + strVal);
                        CommonVariables.TEMP_KEY_ID.put(strKey, strVal);
                        CommonVariables.TEMP_KEY_SNO.put(strVal, strKey);
                        CommonVariables.LIST_TEMP_VALUES.add(CommonVariables.LIST_SECTION_VALUES.get(i));
                        CommonVariables.LIST_TEMP_CONTENT.add(CommonVariables.LIST_SECTION_CONTENT.get(i));
                    }

                    Intent intentSubActs = new Intent(getApplicationContext(), IndexListActivity.class);
                    intentSubActs.putExtra("selectedLabel", selectedLabel);
                    intentSubActs.putExtra("selectedIndex", selectedIndex);
                    startActivity(intentSubActs);
                }

                else if(strSub1.contains("."))        
                {
                    strSub2 = strSub1.substring(0,strSub1.indexOf("."));                    
                    indexStart = strSub2.trim();
                    Log.i("SecNsec", "---Start---" + indexStart + "---");                

                    String strSno     = CommonVariables.MAP_KEY_ID.get(indexStart);
                    String strAct     = CommonVariables.LIST_SECTION_VALUES.get(Integer.parseInt(strSno) - 1);
                    String strContent = CommonVariables.LIST_SECTION_CONTENT.get(Integer.parseInt(strSno) - 1);
                    Intent intentSubActs = new Intent(getApplicationContext(), ContentActivity.class);
                    intentSubActs.putExtra("selectedLabel", selectedLabel);
                    intentSubActs.putExtra("ifIndianPenalCode", false);
                    intentSubActs.putExtra("selectedAct", strAct);
                    intentSubActs.putExtra("selectedContent", strContent);
                    startActivity(intentSubActs);                
                }
            }
            
            else
            {
                Log.i("#Schedule#", "" + rawId);
                
                switch(rawId)
                {
                    case R.raw.code_of_criminal_procedure_act_1973:
                        if(selectedIndex.equals("THE FIRST SCHEDULE"))
                        {
                            Intent intentSubActs = new Intent(getApplicationContext(), ScheduleActivity.class);
                            intentSubActs.putExtra("selectedSchedule", selectedIndex);
                            intentSubActs.putExtra("schedulePosition", "");
                            startActivity(intentSubActs);
                        }
                        
                        else if(selectedIndex.equals("THE SECOND SCHEDULE"))
                        {
                            Intent intentSubActs = new Intent(getApplicationContext(), FormsActivity.class);
                            intentSubActs.putExtra("selectedSchedule", selectedIndex);
                            startActivity(intentSubActs);
                        }
                        break;
                        
                    case R.raw.constitution_of_india:
                        if( selectedIndex.equals("THIRD SCHEDULE")
                         || selectedIndex.equals("FOURTH SCHEDULE")
                         || selectedIndex.equals("SIXTH SCHEDULE")
                         || selectedIndex.equals("EIGHTH SCHEDULE")
                         || selectedIndex.equals("NINTH SCHEDULE")
                         || selectedIndex.equals("TENTH SCHEDULE")
                         || selectedIndex.equals("ELEVENTH SCHEDULE")
                         || selectedIndex.equals("TWELTH SCHEDULE"))
                         {
                             Intent intentSubActs = new Intent(getApplicationContext(), ScheduleActivity.class);
                             intentSubActs.putExtra("selectedSchedule", selectedIndex);
                             intentSubActs.putExtra("schedulePosition", "");
                             startActivity(intentSubActs);
                         }
                        
                        else if(selectedIndex.equals("FIRST SCHEDULE") 
                        || selectedIndex.equals("SECOND SCHEDULE")
                        || selectedIndex.equals("FIFTH SCHEDULE")
                        || selectedIndex.equals("SEVENTH SCHEDULE"))
                        {
                            Intent intentSubActs = new Intent(getApplicationContext(), ScheduleListActivity.class);
                            intentSubActs.putExtra("selectedSchedule", selectedIndex);
                            startActivity(intentSubActs);
                        }
                        break;
                        
                    case R.raw.employees_compensation_act_1923:
                         if( selectedIndex.equals("SECOND SCHEDULE")
                         || selectedIndex.equals("THIRD SCHEDULE")
                         || selectedIndex.equals("FOURTH SCHEDULE"))
                         {
                             Intent intentSubActs = new Intent(getApplicationContext(), ScheduleActivity.class);
                             intentSubActs.putExtra("selectedSchedule", selectedIndex);
                             intentSubActs.putExtra("schedulePosition", "");
                             startActivity(intentSubActs);
                         }
                        
                        else if(selectedIndex.equals("FIRST SCHEDULE"))
                        {
                            Intent intentSubActs = new Intent(getApplicationContext(), ScheduleListActivity.class);
                            intentSubActs.putExtra("selectedSchedule", selectedIndex);
                            startActivity(intentSubActs);
                        }
                        break;
                        
                    case R.raw.limitation_act_1963:
                        if(selectedIndex.equals("The Schedule Period of Limitations"))
                        {
                            Intent intentSubActs = new Intent(getApplicationContext(), ScheduleList2Activity.class);
                            intentSubActs.putExtra("selectedSchedule", selectedIndex);
                            intentSubActs.putExtra("schedulePosition", "");
                            startActivity(intentSubActs);
                        }                       
                       break;
                       
                    case R.raw.arbitration_and_conciliation_act_1996:
                        if( selectedIndex.equals("SECOND SCHEDULE"))
                        {
                            Intent intentSubActs = new Intent(getApplicationContext(), ScheduleActivity.class);
                            intentSubActs.putExtra("selectedSchedule", selectedIndex);
                            intentSubActs.putExtra("schedulePosition", "");
                            startActivity(intentSubActs);
                        }
                       
                       else if(selectedIndex.equals("FIRST SCHEDULE") || selectedIndex.equals("THIRD SCHEDULE"))
                       {
                           Intent intentSubActs = new Intent(getApplicationContext(), ScheduleListActivity.class);
                           intentSubActs.putExtra("selectedSchedule", selectedIndex);
                           startActivity(intentSubActs);
                       }
                       break; 
                }
                      
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
//                listSections.setAdapter(adapterSections);
                listSections.setAdapter(new IndexAdapter(getApplicationContext(), listTitle, listBody));
                removeDialog(0);
            }
        }
    }; 
} 
