package com.mobilex.lawmobilelibrary.activities;

import android.view.View;
import android.widget.AdapterView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.mobilex.lawmobilelibrary.util.CommonVariables;

public class MenuRuleSearchActivity extends Activity 
{
    private TextView txtOrderId = null;
    private ListView listSections = null;
    private ArrayAdapter adapterSections = null;
    private Intent intentMenuSearch = null;
    
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.rule_menu);
        
        Log.i("#SecNsec_App#","---Welcome to Section word---");
        
        Bundle bundleActs = getIntent().getExtras();
        String strHead = bundleActs.getString("selectedAct");
        txtOrderId = (TextView) findViewById(R.id.txt_rule_menu); 
        txtOrderId.setText(strHead);
        
        String strSub = strHead.substring(0,strHead.indexOf(":")).trim();        
        CommonVariables.RULE_ORDER_ID   = strSub;
        CommonVariables.RULE_ORDER_FLAG = true;
        
        List<String> listSearch = new ArrayList<String>();
        listSearch.add("Search by Rule");
        listSearch.add("Search by Rule Word");
        listSearch.add("Rules List");
        listSections = (ListView) findViewById(R.id.listView_Rule_Menu);
        listSections.setOnItemClickListener(new InputList());
        
        adapterSections = new ArrayAdapter(getApplicationContext(), R.layout.simplerow, listSearch);       
        listSections.setAdapter(adapterSections);
    } 
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if((keyCode == KeyEvent.KEYCODE_BACK)) 
        {
            CommonVariables.RULE_ORDER_FLAG = false;
//            startActivity(new Intent(getApplicationContext(), EnrollChoiceActivity.class));
//            finish();
        }
        
        return super.onKeyDown(keyCode, event);
    }    
    
    private class InputList implements OnItemClickListener
    {                
        public void onItemClick(AdapterView<?> a, View v, int position, long id)
        {
            switch(position)
            {
                case 0:
                         intentMenuSearch = new Intent(getApplicationContext(), RuleIDActivity.class);                         
                         break;

                case 1:
                         intentMenuSearch = new Intent(getApplicationContext(), RuleWordActivity.class);
                         break;

                case 2:
                         intentMenuSearch = new Intent(getApplicationContext(), RuleListActivity.class);
                         break; 
            }
            
            startActivity(intentMenuSearch);
        }        
    }
} 