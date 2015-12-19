package com.mobilex.lawmobilelibrary.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressWarnings("deprecation")
public class ActsActivity extends ListActivity 
{   
    private int[] ridAboutArray = new int[]{ R.string.about_product,
                                             R.string.about_author,
                                             R.string.about_developer,
                                             R.string.about_licence,                                            
                                             R.string.about_contact};
     
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);   
        
//        getListView().setCacheColorHint(Color.rgb(36, 33, 32));
//        getListView().setBackgroundColor(Color.rgb(36, 33, 32));

        Log.i("#SecNsec_App#","---Welcome to Main Acts---");
        
        String[] bareActs = getResources().getStringArray(R.array.mainTopics);    
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,bareActs);        
        setListAdapter(adapter);  
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {
        String selectionLabel = l.getItemAtPosition(position).toString();
       
        selectionLabel = selectionLabel.substring(selectionLabel.indexOf('.') + 1, selectionLabel.length());        
        Intent intentSubActs = new Intent(this, SubActsActivity.class);
        intentSubActs.putExtra("selectedLabel", selectionLabel);
        intentSubActs.putExtra("selectedPosition", position);         
        startActivity(intentSubActs);
    }
    
    @Override  
    public boolean onCreateOptionsMenu(Menu menu)
    {  
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }  
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch(item.getItemId()) 
        {          
            case R.id.about:
                             openAboutDialog();
                             break;
                
            case R.id.share:
                             openShareDialog();
                             break;
                
            case R.id.help:
                             openHelpDialog();
                             break;
        }
        
        return true;
    }    
    
    private void openAboutDialog()
    {
        final CharSequence[] items = {"Law Mobile Library & Usage", "Concept By", "Developer", "Licence", "Contact Info"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        builder.setTitle("About");
        builder.setItems(items, new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int item)
            {
                String strAbout = getResources().getString(ridAboutArray[item]);
                
                if(item == 4)
                    strAbout += getResources().getString(R.string.customer_care_no_1);
                
                Intent aboutIntent = new Intent(getApplicationContext(), ContentActivity.class);
                aboutIntent.putExtra("selectedLabel", "About");
                aboutIntent.putExtra("selectedAct", items[item]);
                aboutIntent.putExtra("selectedContent", strAbout);
                startActivity(aboutIntent);
            }
        }).show();
    }
    
    private void openShareDialog()
    {
        final CharSequence[] items = {"People Contact", "SMS", "Email"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        builder.setTitle("Share to Friend");
        builder.setItems(items, new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int item)
            {
                String shareSub    = "", shareMsg = "";
                Intent shareIntent = null;
                
                switch(item) 
                {          
                    case 0:
                             shareIntent = new Intent();
                             shareIntent.setAction(android.content.Intent.ACTION_VIEW);  
                             shareIntent.setData(People.CONTENT_URI);
                             startActivity(shareIntent);
                             break;
                        
                    case 1:
                             shareMsg    = getString(R.string.share_msg);
                             shareIntent = new Intent(Intent.ACTION_VIEW);
                             shareIntent.setType("vnd.android-dir/mms-sms"); 
                             shareIntent.putExtra("sms_body", shareMsg);                             
                             startActivity(shareIntent);
                             break;

                    case 2:
                             shareSub    = getString(R.string.share_Sub);
                             shareMsg    = getString(R.string.share_msg);
                             shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                             shareIntent.setType("plain/text");
                             shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                             shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMsg);
                             startActivity(Intent.createChooser(shareIntent, "Send to Share Message..."));
                             break;
                }
            }
        }).show();
    }
    
    private void openHelpDialog()
    {
        final CharSequence[] items = {"Call", "SMS", "Email"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        builder.setTitle("Help");
        builder.setItems(items, new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int item)
            {
                String helpSub    = "", helpEmail = "", helpNo = "";
                Uri    helpUri    = null;
                Intent helpIntent = null;
                
                switch(item) 
                {          
                    case 0:
                             helpNo     = getString(R.string.customer_care_no_1);
                             helpUri    = Uri.parse("tel:" + helpNo);
                             helpIntent = new Intent(Intent.ACTION_CALL, helpUri);                            
                             startActivity(helpIntent);
                             break;
                        
                    case 1:
                             helpNo     = getString(R.string.customer_care_no_1);
                             helpUri    = Uri.parse("smsto:" + helpNo);
                             helpIntent = new Intent(Intent.ACTION_SENDTO, helpUri);
                             startActivity(helpIntent);
                             break;
                        
                    case 2:
                             helpEmail  = getString(R.string.customer_care_email);
                             helpSub    = getString(R.string.help_Sub);
                             helpIntent = new Intent(android.content.Intent.ACTION_SEND);
                             helpIntent.setType("plain/text");
                             helpIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{helpEmail});
                             helpIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, helpSub);
                             startActivity(Intent.createChooser(helpIntent, "Send to Customer Care..."));
                             break;
                }
            }
        }).show();
    }
}