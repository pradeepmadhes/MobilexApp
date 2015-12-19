package com.mobilex.lawmobilelibrary.activities;

import java.util.Random;

import com.mobilex.lawmobilelibrary.persistent.SharedPersistent;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.widget.Button;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class HomeActivity extends Activity
{
    private int[] ridAboutArray = new int[]{ R.string.about_product,
                                             R.string.about_author,
                                             R.string.about_developer,
                                             R.string.about_licence,                                            
                                             R.string.about_contact};
    
    private EditText edtEnter = null;
    private Button btnGoto = null, btnEnter = null, btnDemo = null;
    
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);     
        setContentView(R.layout.home_new);
        
        edtEnter = (EditText) findViewById(R.id.edt_enter_act);
        
        btnGoto = (Button) findViewById(R.id.btn_goto);
        btnGoto.setOnClickListener(new InputButtons());
        
        btnEnter = (Button) findViewById(R.id.btn_enter_act);
        btnEnter.setOnClickListener(new InputButtons()); 
        
        btnDemo = (Button) findViewById(R.id.btn_enter_demo);
        btnDemo.setOnClickListener(new InputButtons()); 
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        if(resultCode == 1)
        {
            Toast.makeText(getBaseContext(), "Activate code request sent successfully", Toast.LENGTH_SHORT).show();
            SharedPersistent.setActivateRequest(true);
        }
    }
    
    private class InputButtons implements View.OnClickListener
    {
        public void onClick(View v) 
        {
            switch(v.getId())
            {
                case R.id.btn_goto:
		                					functionCreateActivate();
		                                    break;
                    
                case R.id.btn_enter_act:                                       
		                                    String strEnter = edtEnter.getText().toString().trim();                                    
		                                    functionValidateCode(strEnter);
		                                    
		                                    break;
                                    
                case R.id.btn_enter_demo:                                       
                	 						startActivity(new Intent(getApplicationContext(), ActsActivity.class));
						                    
						                    break;
            }            
        }        
    }
    
    private String functionGetEmail(Context context) 
    {
        AccountManager mAccountManager = AccountManager.get(context); 
        Account mAccount = functionGetAccount(mAccountManager);

        if (mAccount == null) 
        {
            return null;
        } 
        
        else 
        {
            return mAccount.name;
        }
    }

    private Account functionGetAccount(AccountManager accountManager) 
    {
        Account[] mAccounts = accountManager.getAccountsByType("com.google");
        Account mAccount;
        
        if (mAccounts.length > 0) 
        {
            mAccount = mAccounts[0];     
        } 
        
        else
        {
            mAccount = null;
        }
        
        return mAccount;
    }     
    
    private void functionValidateCode(String strEnteredCode)
	{		
		String lStrActivationCode = SharedPersistent.getActivationCode();  
//		Log.i("--- Original Activation Code ---", lStrActivationCode);		
//		int lCodelength = lStrActivationCode.length();
//		lStrActivationCode = lStrActivationCode.substring(0,2).trim() + lStrActivationCode.substring(lCodelength-2,lCodelength).trim();		
//         
//        Log.i("--- Valid Activation Code ---", lStrActivationCode);
         
        if(strEnteredCode.toLowerCase().equals(lStrActivationCode))
        {
            SharedPersistent.setActivate(true);
            dialogSuccessMessage();
        }
         
        else
        	dialogErrorCode(); 
    }
	   
	private void functionCreateActivate()
	{
		 String lStrEmail = functionGetEmail(this);        
		 TelephonyManager lTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);      
		 String lStrDeviceId = lTelephonyManager.getDeviceId(); // Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)
         String lStrActivationCode = "";
         
         Random mRandom = new Random();
         int mRandomNo = mRandom.nextInt(9999);
         
         if(mRandomNo <= 999)
        	 mRandomNo += 1000;
         
         String mStrRandomNo = String.valueOf(mRandomNo);
         
         try 
         {    
        	lStrActivationCode = lStrEmail + "#" + lStrDeviceId + "#" + mRandomNo;
        	byte[] data = lStrActivationCode.getBytes("UTF-8");
         	lStrActivationCode = Base64.encodeToString(data, Base64.DEFAULT);//ActivateCrypto.encrypt(getResources().getString(R.string.app_name), lStrDeviceId).toLowerCase();
         	SharedPersistent.setActivationCode(mStrRandomNo);
         	Log.i("--- Encrypted Device ID ---", lStrActivationCode);
         	
         	final String lStrMessage = lStrActivationCode;
         	Log.i("--- Str Message ---", lStrMessage);
         	dialogSmsConfirmation(lStrMessage);
         } 

         catch (Exception ex)
         {             
        	 ex.printStackTrace();
         }         
	}
	
	private void dialogSuccessMessage()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Congrats !!!"); 
        builder.setMessage("Your activation code has been activated successfully... \nWelcome to Law Mobile Library.");         
        builder.setIcon(android.R.drawable.ic_dialog_info);

        builder.setPositiveButton("OK", new OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int which) 
            {   
            	dialog.cancel();
            	
            	startActivity(new Intent(getApplicationContext(), ActsActivity.class));
            	finish();
            }
        });
        
        builder.show();
    }
	
	private void dialogErrorCode()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error"); 
        builder.setMessage("Please enter the correct activation code");         
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("Close", new OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int which) 
            {             
            	dialog.cancel();
            }
        });
        
        builder.show();
    }
	
	private void dialogSmsConfirmation(final String strMessage)
    {		
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message");
        
//        if(SharedPersistent.getActivateRequest())
//            builder.setMessage("Already activation request sent.Do U want again request to Vendor via SMS?");            
//        else
            builder.setMessage("In this free version you can access Indian Penal Code only. For the remaining 70 Acts you need activation code. The code cost Rs. 400/- only. It's a one time charge. If you like to buy the code pls click \"OK\" option to send activation code request SMS to vendor and our Customer Care Executive will call back you soon.");
        
        builder.setIcon(android.R.drawable.ic_dialog_email);

        builder.setPositiveButton("OK", new OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int which) 
            {           
                functionSendSMS(strMessage, getResources().getString(R.string.product_owner_no));  
                functionSendSMS(strMessage, getResources().getString(R.string.customer_care_no));  
            }
        });
        
        builder.setNegativeButton("Cancel", new OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int which) 
            {           
            }
        });

        builder.show();
    }
	
	private void functionSendSMS(String reqMessage, String phNo)
    {       	
    	String SENT      = "SMS_SENT";
    	String DELIVERED = "SMS_DELIVERED";
    	
        PendingIntent sentPI      = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);        
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);    	
        
        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context arg0, Intent arg1) 
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:      
					                    							SharedPersistent.setActivateRequest(true);
					                    							break;
					                    							
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
										                            Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
										                            break;
										                            
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
										                            Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
										                            break;
										                            
                    case SmsManager.RESULT_ERROR_NULL_PDU:
										                            Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
										                            break;
										                            
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
										                            Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
										                            break;
                }
            }
        }, new IntentFilter(SENT));        
        
        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context arg0, Intent arg1) 
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
						                            Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
						                            break;
                    case Activity.RESULT_CANCELED:
						                            Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
						                            break;					    
                }
            }
        }, new IntentFilter(DELIVERED));        
    	
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phNo, null, reqMessage, sentPI, deliveredPI);               
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
    
    private int functionSumOfDigits(int n) 
    {
    	int sum = 0;
    	
        if (n <= 0)
              System.out.println("Integer you've entered is nonpositive.");
        else 
        {              
              // algorithm step by step
              // base:  sum = 0, n = 123
              // step1: n % 10 = 3, n / 10 = 12
              //        sum = 3, n = 12
              // step2: n % 10 = 2, n / 10 = 1
              //        sum = 5, n = 1
              // step3: n % 10 = 1, n / 10 = 0
              //        sum = 6, n = 0
              // stop:  (n != 0) is false
              while (n != 0) 
              {
                    // add last digit to the sum
                    sum += n % 10;
                    // cut last digit
                    n /= 10;
              }
              
              System.out.println("Sum of digits: " + sum);
        }
        
        return sum;
    }
}
