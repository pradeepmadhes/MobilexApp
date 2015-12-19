package com.mobilex.lawmobilelibrary.activities; 

import com.mobilex.lawmobilelibrary.persistent.SharedPersistent;

import android.os.*;
import android.app.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.graphics.Color;
import android.util.Log;

public class SplashActivity extends Activity
{
    private boolean flagActivate = false;
    private int width = 0, height = 0;
    private Display   display = null;
    private ImageView imgView = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        SharedPreferences shared2Common = getSharedPreferences(getResources().getString(R.string.shared_pref), MODE_PRIVATE); 
        SharedPersistent.sharedPersistent(shared2Common);
        flagActivate = SharedPersistent.getActivate();
                
        display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        width   = display.getWidth();
        height  = display.getHeight();

        imgView = new ImageView(this);
        imgView.setMaxWidth(width);
        imgView.setMaxHeight(height);
        imgView.setImageResource(R.drawable.lawlogo);
        imgView.setBackgroundColor(Color.WHITE);     
        setContentView(imgView);
        Log.i("#SecNsec_App#","---Welcome to First Screen---");

        final int welcomeScreenDisplay = 3000;

        Thread welcomeThread = new Thread()
        {
            int wait = 0;

            @Override
            public void run()
            {
                try
                {
                    super.run();
                    while (wait < welcomeScreenDisplay)
                    {
                        sleep(100);
                        wait += 100;
                    }
                }

                catch(Exception ex)
                {
                    Log.d("#SecNsec_App#","---First Screen Bug---"  + ex);
                }

                finally
                {
                    if(flagActivate)
                        startActivity(new Intent(getApplicationContext(), ActsActivity.class));
                    
                    else
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    
                    finish();
                }
            }
        };

        welcomeThread.start();
    }
}
