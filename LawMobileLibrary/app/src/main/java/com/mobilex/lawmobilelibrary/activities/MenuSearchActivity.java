package com.mobilex.lawmobilelibrary.activities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xml.sax.InputSource;

import com.mobilex.lawmobilelibrary.parser.XmlParser;
import com.mobilex.lawmobilelibrary.persistent.SharedPersistent;
import com.mobilex.lawmobilelibrary.util.CommonVariables;

public class MenuSearchActivity extends ListActivity 
{      
	private boolean flagActivate = false;
    private String selectedLabel = "", selectedIndexLabel = "";
    private Intent intentMenuSearch = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);   
        
        Log.i("#SecNsec_App#","---Welcome to Menu Search---");
        
        SharedPreferences shared2Common = getSharedPreferences(getResources().getString(R.string.shared_pref), MODE_PRIVATE); 
        SharedPersistent.sharedPersistent(shared2Common);
        flagActivate = SharedPersistent.getActivate();
        
        Bundle bundleActs = getIntent().getExtras();
        selectedLabel = bundleActs.getString("selectedLabel");
        
        this.setTitle(selectedLabel);
        
        String[] arrSearch  = getResources().getStringArray(R.array.menuSearch);
        String[] arrPart    = getResources().getStringArray(R.array.partIndex); 
        String[] arrChapter = getResources().getStringArray(R.array.chapterIndex); 
        
        List<String> listSearch  = new ArrayList<String>(Arrays.asList(arrSearch));
        List<String> listPart    = new ArrayList<String>(Arrays.asList(arrPart));
        List<String> listChapter = new ArrayList<String>(Arrays.asList(arrChapter));
        
        if(listPart.contains(selectedLabel))
        {
            selectedIndexLabel = "Enter the Part Index";
            listSearch.add("Part Index");
        }            
        
        else if(listChapter.contains(selectedLabel))
        {          
            selectedIndexLabel = "Enter the Chapter Index";
            listSearch.add("Chapter Index");
        }
        
        if(selectedLabel.equals("Constitution of India"))
        {
            listSearch.set(0,"Search by Article");
            listSearch.set(1,"Search by Word");
            listSearch.set(2,"Preamble");
            listSearch.add("Part Index");
        }
        
        else if(selectedLabel.equals("Civil Procedure Code, 1908"))
        {           
            listSearch.add("Search by Order, Word");
        }

        else if(selectedLabel.equals("Motor Vehicles Act, 1988"))
        {
            listSearch.add("Offence & Punishment");
        }

        else if(selectedLabel.equals("Hindu Succession Act, 1956"))
        {
            listSearch.add("Class I Heirs");
            listSearch.add("Class II Heirs");
        }      

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listSearch); 
        setListAdapter(adapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {        
    	 if(flagActivate || selectedLabel.equals("Indian Penal Code, 1860"))//	  if(true)
    	 {
    		 switch(position)
             {
                 case 0:
                         intentMenuSearch = new Intent(this, SectionListActivity.class);
                         intentMenuSearch.putExtra("selectedLabel", selectedLabel);
//                         intentMenuSearch.putExtra("selectedIndex", "");
                         startActivity(intentMenuSearch);
                         break;
                     
                 case 1:
                         intentMenuSearch = new Intent(this, SectionWordActivity.class);
                         intentMenuSearch.putExtra("selectedLabel", selectedLabel);
//                         intentMenuSearch.putExtra("selectedIndex", "");
                         startActivity(intentMenuSearch);
                         break;            
                     
                 case 2:
                         if(selectedLabel.equals("Constitution of India"))
                             openContentView("Preamble", 0, R.raw.y_preamble);
                         
                         else
                         {
                             intentMenuSearch = new Intent(this, IndexActivity.class);
                             intentMenuSearch.putExtra("selectedLabel", selectedLabel);
                             intentMenuSearch.putExtra("selectedIndexLabel", selectedIndexLabel);
                             startActivity(intentMenuSearch);
                         }
                         
                         break;

                 case 3:
                         if(selectedLabel.equals("Constitution of India"))
                         {
                             intentMenuSearch = new Intent(this, IndexActivity.class);
                             intentMenuSearch.putExtra("selectedLabel", selectedLabel);
                             intentMenuSearch.putExtra("selectedIndexLabel", selectedIndexLabel);                         
                             startActivity(intentMenuSearch);
                         }                         

                         else if(selectedLabel.equals("Motor Vehicles Act, 1988"))
                             openOffenceDialog();

                         else if(selectedLabel.equals("Hindu Succession Act, 1956"))
                             openContentView("Class I Heirs", 0, R.raw.y_class_heirs);
                         
                         else
                         {
                             intentMenuSearch = new Intent(this, OrderWordListActivity.class);
                             intentMenuSearch.putExtra("selectedLabel", selectedLabel);
                             startActivity(intentMenuSearch);
                         }
                         
                         break;

                 case 4:
                         if(selectedLabel.equals("Hindu Succession Act, 1956")) 
                             openContentView("Class II Heirs", 1, R.raw.y_class_heirs);
                         
                         else
                             intentMenuSearch = new Intent(this, OrderWordListActivity.class);
                             intentMenuSearch.putExtra("selectedLabel", selectedLabel);
                             startActivity(intentMenuSearch);
                         break;                 
             }
    	 }
    	 
    	 else
    	 {
    		 functionAlertActivation();
    	 }
    }
    
    private void functionAlertActivation()
    {
    	 AlertDialog alertDialog1 = new AlertDialog.Builder(this).create();
         alertDialog1.setTitle("Alert");
         alertDialog1.setMessage("In this free version, you can access Indian Penal Code,1860 entire Bare Act absolutely free by clicking III.Criminal & Motor Accident Laws heading then 4.Indian Penal Code,1860.\nFor the remaining 70 Acts please buy and enter the Activation code to access all the contents. The activation code cost Rs.400. Its a one time charge to access all the 71 Acts.\nFor more details call\n+91 8754542929 from 10 a.m to 6 p.m.\nThank you. ");
         alertDialog1.setIcon(android.R.drawable.ic_dialog_alert);
         alertDialog1.setCancelable(false);

         alertDialog1.setButton("OK", new DialogInterface.OnClickListener()
         {
             public void onClick(final DialogInterface dialog, final int which) 
             {
            	 dialog.dismiss();
             }
         });

         alertDialog1.show();
    }
    
    private void openOffenceDialog()
    {
        final int[] itemsId = { R.raw.y_offence_documents,
                                R.raw.y_offence_driving,
                                R.raw.y_miscellaneous};
        
        final CharSequence[] items = { "1.Documents Related Offence",
                                       "2.Driving Related Offence",
                                       "3.Miscellaneous"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        builder.setTitle("Offence & Punishment");
        builder.setItems(items, new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int item)
            {            
                Intent aboutIntent = new Intent(getApplicationContext(), OffenceActivity.class);
                aboutIntent.putExtra("selectedRawId", itemsId[item]);
                aboutIntent.putExtra("selectedLabel", items[item]);                
                startActivity(aboutIntent);
            }
        }).show();
    }
    
    private void openContentView(String strLable, int position, int rawId)
    {
        CommonVariables.LIST_SECTION_CONTENT = new ArrayList<String>();

        InputStream inpstreamXml = getResources().openRawResource(rawId);      
        XmlParser.getParserList(new InputSource(inpstreamXml));

        String strContent = CommonVariables.LIST_SECTION_CONTENT.get(position);

        intentMenuSearch = new Intent(this, ContentActivity.class);
        intentMenuSearch.putExtra("selectedLabel", selectedLabel);
        intentMenuSearch.putExtra("selectedAct", strLable);
        intentMenuSearch.putExtra("selectedContent", strContent);
        startActivity(intentMenuSearch);
    }
}