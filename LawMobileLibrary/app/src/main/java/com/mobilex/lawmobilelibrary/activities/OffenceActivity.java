package com.mobilex.lawmobilelibrary.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class OffenceActivity extends TabActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offence_tab);
        
        Bundle bundleActs = getIntent().getExtras();
        int selectedId = bundleActs.getInt("selectedRawId");
        String selectedLabel = bundleActs.getString("selectedLabel");
        
        this.setTitle(selectedLabel);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, OffenceWordActivity.class);
        intent.putExtra("selectedRawId", selectedId);
        spec = tabHost.newTabSpec("Word").setIndicator("Word").setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, OffenceListActivity.class);
        intent.putExtra("selectedRawId", selectedId);
        spec = tabHost.newTabSpec("List").setIndicator("List").setContent(intent);
        tabHost.addTab(spec);
    }
}