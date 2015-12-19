package com.mobilex.lawmobilelibrary.persistent;

import android.content.SharedPreferences;

public class SharedPersistent 
{
    private static SharedPreferences sharedPreferences = null;
    
    public static void sharedPersistent(SharedPreferences shdLocal)
    {
        sharedPreferences = shdLocal;
    }
    
    public static void setActivate(boolean valActivate)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ACTIVATE_STATUS", valActivate);
        editor.commit();
    } 
    
    public static boolean getActivate()
    {        
        boolean valActivate = sharedPreferences.getBoolean("ACTIVATE_STATUS", false);        
        
        return valActivate;
    }
    
    public static void setActivateRequest(boolean valActivate)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ACTIVATE_REQUEST", valActivate);
        editor.commit();
    } 
    
    public static boolean getActivateRequest()
    {        
        boolean valActivate = sharedPreferences.getBoolean("ACTIVATE_REQUEST", false);        
        
        return valActivate;
    }    
    
    public static void setRawID(int valRawID)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("FILE_RAW_ID", valRawID);
        editor.commit();
    } 
    
    public static int getRawID()
    {        
        int valRawID = sharedPreferences.getInt("FILE_RAW_ID", 0);        
        
        return valRawID;
    }
    
    public static void setActivationCode(String valCustID)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("CUST_ID", valCustID);
        editor.commit();
    } 
    
    public static String getActivationCode()
    {        
        String valCustID = sharedPreferences.getString("CUST_ID", "");        
        
        return valCustID;
    }
    
    public static void setRandomNo(String valRandomNo)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("RANDOM_NO", valRandomNo);
        editor.commit();
    } 
    
    public static String getRandomNo()
    {        
        String valRandomNo = sharedPreferences.getString("RANDOM_NO", "");        
        
        return valRandomNo;
    }
}