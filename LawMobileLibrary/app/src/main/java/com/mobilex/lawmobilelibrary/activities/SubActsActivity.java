package com.mobilex.lawmobilelibrary.activities;

import com.mobilex.lawmobilelibrary.persistent.SharedPersistent;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SubActsActivity extends ListActivity 
{   
    private int selectedAct = -1;
    
    private int[] ridActsArray = new int[]{ R.array.actBanking,
                                            R.array.actCorporate,
                                            R.array.actCriminal,
                                            R.array.actEnvironment,                                            
                                            R.array.actFamily,
                                            R.array.actLabour,                                            
                                            R.array.actLegal,
                                            R.array.actMiscellaneous,                                            
                                            R.array.actProperty};
    
    private int[] ridActsFiles = new int[]{ R.array.rawBanking,
                                            R.array.rawCorporate,
                                            R.array.rawCriminal,
                                            R.array.rawEnvironment,                                            
                                            R.array.rawFamily,
                                            R.array.rawLabour,                                            
                                            R.array.rawLegal,
                                            R.array.rawMiscellaneous,                                            
                                            R.array.rawProperty};
    
    private String[] bareActs = null;
    private TypedArray typedActs = null;
        
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);        
        
        Log.i("#SecNsec_App#","---Welcome to Sub Acts---");
        
        Bundle bundleActs = getIntent().getExtras();
        String selectedLabel = bundleActs.getString("selectedLabel");
        this.setTitle(selectedLabel);
        
        selectedAct = bundleActs.getInt("selectedPosition");        
        bareActs    = getResources().getStringArray(ridActsArray[selectedAct]);    
        typedActs   = getResources().obtainTypedArray(ridActsFiles[selectedAct]);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,bareActs);
        setListAdapter(adapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {
         String selectionLabel = l.getItemAtPosition(position).toString();
         int selectedRawId = typedActs.getResourceId(position, 0);
         
         SharedPersistent.setRawID(selectedRawId);        
         selectionLabel = selectionLabel.substring(selectionLabel.indexOf('.') + 1, selectionLabel.length());
         Intent intentSubActs = new Intent(this, MenuSearchActivity.class);
         intentSubActs.putExtra("selectedLabel", selectionLabel);
         startActivity(intentSubActs);
    }
}