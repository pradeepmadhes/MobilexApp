package com.mobilex.lawmobilelibrary.service;

import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ClientService
{
    final static String SERVER_HOST = "192.168.15.112";
    final static int    SERVER_PORT = 8084;
    final static String SERVER_URL1 = "/SecNsecServer/resources/activate"; //  "/login/pradeep/deepword"
    
    public static String getHttpClient(ArrayList<String> listInputs) 
    {       
        String resultEntity = "";
        
        try
        {
            if(connectServer())
            {
                String SERVER_URL2 = "";
            
                for(int i = 0; i < listInputs.size(); i++)
                {
                    SERVER_URL2 += "/" + listInputs.get(i);
                }

                Log.i("#SecNsecServer#", "---Server URL---" + SERVER_URL1 + SERVER_URL2);

                HttpResponse htpResponse = null;
                HttpEntity   htpEntity   = null;
                HttpClient   htpClient   = new DefaultHttpClient();
                HttpHost     htpHost     = new HttpHost(SERVER_HOST, SERVER_PORT, "http");
                HttpGet      htpGet      = new HttpGet(SERVER_URL1 + SERVER_URL2);

                try
                {  
                    htpResponse  = htpClient.execute(htpHost, htpGet);
                    htpEntity    = htpResponse.getEntity();
                    resultEntity = EntityUtils.toString(htpEntity);

                    Log.i("#SecNsecServer#", "---Result from server---" + resultEntity);
                } 

                catch (Exception e) 
                {
                    resultEntity = "Server Connection Failed";
                    Log.e("#SecNsecServer#", "---Http Error---",e);
                    return resultEntity;
                } 

                finally 
                {
                    if(htpEntity!=null)
                    {
                        try 
                        {
                            htpEntity.consumeContent();
                        } 

                        catch(IOException e)
                        {
                        }
                    }                
                }
            }
            
            else
            {   
                resultEntity = "Server Connection Failed";
            }            
        } 
        
        catch (Exception ex)
        {
        }
        
        return resultEntity;
    }
    
    private static boolean connectServer() throws Exception
    {
        String strUrl = "http://" + SERVER_HOST + ":" + SERVER_PORT + "/SecNsecServer";

        try 
        {
            URL url = new URL(strUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.connect();

            if(urlConn != null)
            {
                Log.i("#SecNsec#", "Connection Success" + urlConn.getResponseCode());
                return true;
            }
               
            else
            {
                Log.i("#SecNsec#", "Connection Failed" + urlConn.getResponseCode());
                return false;
            }               
        } 
        
        catch (IOException ex)
        {
            Log.e("#SecNsec#", "Connection Failed", ex);
            return false;
        }
    }
}