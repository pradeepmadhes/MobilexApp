package com.mobilex.lawmobilelibrary.activities;

import java.util.ArrayList;

import com.mobilex.lawmobilelibrary.security.ActivateCrypto;
import com.mobilex.lawmobilelibrary.service.ClientService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.*;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends Activity 
{   
    private String strUname  = "", strPword = "";
    private String strImei = "", strMobusr = "", strPhoneNo = "", strMailId = "", strSendNo = "", strMaster = "", strCode = "";
    
    private TextView txtTitle  = null, txtResult = null;
    private EditText edtMobusr = null, edtPhoneNo = null, edtMailId = null, edtUname = null, edtPword = null;
    private Button   btnLogin  = null, btnActivate = null ;
    private CheckBox cbxLogin  = null;
    
    private Thread         thdSrh = null;
    private Message        msgSrh = null;
    private ProgressDialog diaSrh = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activate);           
        
        strSendNo = getResources().getString(R.string.customer_care_no_1);
        strMaster = getResources().getString(R.string.app_name);
        
        txtTitle    = (TextView) findViewById(R.id.txt_title);
        txtResult   = (TextView) findViewById(R.id.txt_get_result);
        edtMobusr   = (EditText) findViewById(R.id.edt_user_name);
        edtPhoneNo  = (EditText) findViewById(R.id.edt_phone_number);
        edtMailId   = (EditText) findViewById(R.id.edt_mail_id);
        edtUname    = (EditText) findViewById(R.id.edit_username);
        edtPword    = (EditText) findViewById(R.id.edit_password);
        btnActivate = (Button)   findViewById(R.id.btn_get_activate);       
        btnLogin    = (Button)   findViewById(R.id.btn_login_activate);
        cbxLogin    = (CheckBox) findViewById(R.id.chk_login);
        
//        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String getSimSerialNumber=tm.getSimSerialNumber();
//        
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        
//        String str1 = telephonyManager.getDeviceId();
//                String str2 = "", str3 = "";
//        try {
//            str2 = ActivateCrypto.encrypt(strMaster, str1);
//            str3 = ActivateCrypto.decrypt(strMaster, str2);
//        } catch (Exception ex) {
//            Logger.getLogger(UserActivity.class.getName()).log(Level.SEVERE, null, ex);
//        }
//                        
//        try {
//            edtMobusr.setText("A:" + str1);
//            edtPhoneNo.setText("B:" + str2);
//            edtMailId.setText("C:" + str3);
//        } catch (Exception ex) {
//            Logger.getLogger(UserActivity.class.getName()).log(Level.SEVERE, null, ex);
//        }
                        
        edtUname.setEnabled(false); 
        edtPword.setEnabled(false);   
        
        edtUname.addTextChangedListener(new InputValidator(edtUname));
        edtPword.addTextChangedListener(new InputValidator(edtPword));
        
//        btnActivate.setEnabled(false); 
        btnLogin.setEnabled(false); 
        
        btnActivate.setOnClickListener(new InputButtons(btnActivate));
        btnLogin.setOnClickListener(new InputButtons(btnLogin));
        
        cbxLogin.setOnCheckedChangeListener(new InputChecked());
    }
    
     private String getMyPhoneNumber(){
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE); 
        return mTelephonyMgr.getLine1Number();
        }

        private String getMy10DigitPhoneNumber(){
                String s = getMyPhoneNumber();
                return s.substring(2);
        }
    
    @Override
    protected Dialog onCreateDialog(int id) 
    {
        switch (id) 
        {
            case 0:                                  
                     diaSrh = new ProgressDialog(this);
                     diaSrh.setMessage("Please wait while connecting...");
                     diaSrh.setIndeterminate(true);
                     diaSrh.setCancelable(true);

                     return diaSrh;                                
        }
        
        return null;
    }
    
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            String handleMsg = (String) msg.obj;

            if(handleMsg.startsWith("Login Successfully")) 
            {            
                txtResult.setText(handleMsg);
                removeDialog(0);
            }

            else if(handleMsg.startsWith("Login Failed") 
            || handleMsg.startsWith("Server Connection Failed")
            || handleMsg.startsWith("Internet Connection Failed")) 
            {           
                txtResult.setText(handleMsg);
                removeDialog(0);               
            }
            
            else
            {
                txtResult.setText(handleMsg);
                removeDialog(0);    
            }
        }
    };       
    
    private boolean checkInternetConnection()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if(cm.getActiveNetworkInfo() != null
        && cm.getActiveNetworkInfo().isAvailable()
        && cm.getActiveNetworkInfo().isConnected()) 
        {
            return true;
        } 
        
        else 
        {            
            return false;
        }
    }
    
    private class InputChecked implements OnCheckedChangeListener
    {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
        {
            if(buttonView.isChecked()) 
            { 
//                txtTitle.setBackgroundColor(R.color.dark_gray);
                edtUname.setEnabled(true); 
                edtPword.setEnabled(true); 
            } 
            
            else 
            { 
//                txtTitle.setBackgroundColor(R.color.dim_gray);
                edtUname.setEnabled(false); 
                edtPword.setEnabled(false); 
            } 
        }
    }
    
    private class InputValidator implements TextWatcher
    {
        private EditText edtVal = null;

        private InputValidator(EditText editText) 
        {
            this.edtVal = editText;
        }
        
        public void afterTextChanged(Editable edb)
        {
            String edbChrs = edb.toString();

            if(edbChrs.length() == 0)
                btnLogin.setEnabled(false);  
            
            else if(edtVal == edtUname)
            {
                strPword = edtPword.getText().toString().trim();

                if(! "".equals(strPword) && strPword != null && strPword.length() > 0)
                    btnLogin.setEnabled(true);
            }
                    
            else if(edtVal == edtPword)
            {
                strUname = edtUname.getText().toString().trim();
                
                if(! "".equals(strUname) && strUname != null && strUname.length() > 0)
                    btnLogin.setEnabled(true);
            }
        }
        
        public void beforeTextChanged(CharSequence s, int start, int count,int after)
        {
        }

        public void onTextChanged(CharSequence s, int start, int before,int count)
        {            
        }
    }  
    
    private class InputButtons implements View.OnClickListener
    {
        private Button btnClick = null;
        
        private InputButtons(Button btnLocal)
        {
            this.btnClick = btnLocal;
        }
        
        public void onClick(View v)
        {
            if(this.btnClick == btnActivate)
            {
                strMobusr  = edtMobusr.getText().toString();
                strMailId  = edtMailId.getText().toString();   
            	strPhoneNo = edtPhoneNo.getText().toString();            	
                
                if((! "".equals(strMobusr)  && strMobusr  != null && strMobusr.length()  > 0)                
                && (! "".equals(strMailId)  && strMailId  != null && strMailId.length()  > 0)
                && (! "".equals(strPhoneNo) && strPhoneNo != null && strPhoneNo.length() > 0))
                {
                    strMobusr  = "UN:" + edtMobusr.getText().toString()+ ",";
                    strPhoneNo = "PN:" + edtPhoneNo.getText().toString()+ ",";
                    strMailId  = "MI:" + edtMailId.getText().toString();   
                    
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);      
                    
                    try 
                    {
                        strCode = "AC:" + ActivateCrypto.encrypt(strMaster, telephonyManager.getDeviceId()) + ",";
                    } 
                    
                    catch (Exception ex)
                    {                        
                    }
                    
                    strCode += strMobusr + strPhoneNo + strMailId;                    
                    getAlert();  
                }                      
                
                else
                    Toast.makeText(getBaseContext(),"Please enter username, phone number and message.", Toast.LENGTH_SHORT).show();           
            }
            
            else if(this.btnClick == btnLogin)
            {              
                showDialog(0);   
                thdSrh = new Thread()
                {    
                    @Override
                    public void run()
                    {
                        strUname = edtUname.getText().toString();
                        strPword = edtPword.getText().toString(); 
                        String strResult = "";
                        
                        if(checkInternetConnection())
                        {
                            ArrayList<String> listInputs = new ArrayList<String>();
                            listInputs.add("login");
                            listInputs.add(strUname);
                            listInputs.add(strPword);
                            strResult = ClientService.getHttpClient(listInputs);  
                        }
                        
                        else
                            strResult = "Internet Connection Failed";                       
                        
                        msgSrh = new Message();
                        msgSrh.obj = strResult;  
                        handler.sendMessage(msgSrh); 
                    }
                };                
                thdSrh.start();          
            }
        }
    }
    
    private void getAlert()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message Confirmation");
        builder.setMessage(strMobusr + strPhoneNo + strMailId);
        builder.setIcon(android.R.drawable.ic_dialog_info);

        builder.setPositiveButton("SMS", new OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int which) 
            {           
                sendSMS("8754542929");  
                sendSMS("8754542929");  
            }
        });

        builder.setNegativeButton("Direct", new OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                showDialog(0);   
                thdSrh = new Thread()
                {    
                    @Override
                    public void run()
                    {
                        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                                                
                        strImei    = telephonyManager.getDeviceId();
                        strMobusr  = edtMobusr.getText().toString();
                        strMailId  = edtMailId.getText().toString(); 
                        strPhoneNo = edtPhoneNo.getText().toString();                         
                        
                        String strResult = "";
                        
                        if(checkInternetConnection())
                        {
                            ArrayList<String> listInputs = new ArrayList<String>();
                            listInputs.add("actcode");
                            listInputs.add(strImei);
                            listInputs.add(strMobusr);
                            listInputs.add(strMailId);
                            listInputs.add(strPhoneNo);                            
                            strResult = ClientService.getHttpClient(listInputs);  
                        }
                        
                        else
                            strResult = "Internet Connection Failed";                       
                        
                        msgSrh = new Message();
                        msgSrh.obj = strResult;  
                        handler.sendMessage(msgSrh); 
                    }
                };                
                thdSrh.start(); 
            }
        });
        
        builder.setOnCancelListener(new OnCancelListener() 
        {
            public void onCancel(DialogInterface dialog)
            {          
                edtMobusr.setText("");
                edtPhoneNo.setText("");
                edtMailId.setText("");
            }
        });

        builder.show();
    }
    
    private void setConform()
    {      
        setResult(1);
        finish();
    }
    
    private void sendSMS(String phNo)
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
//                            Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
                            setConform();
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
        sms.sendTextMessage(phNo, null, strCode, sentPI, deliveredPI);               
    }    
}