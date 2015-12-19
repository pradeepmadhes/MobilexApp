package com.mobilex.lawmobilelibrary.parser;

import android.util.Log;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XmlParser 
{    
    public static void getParserList(InputSource inpSrc) 
    {    
        try 
        { 
            SAXParserFactory spf = SAXParserFactory.newInstance(); 
            SAXParser sp = spf.newSAXParser(); 
            XMLReader xr = sp.getXMLReader(); 
            XmlParserHandler dataHandler = new XmlParserHandler(); 
            xr.setContentHandler(dataHandler); 
            xr.parse(inpSrc);           
        } 
        
        catch(ParserConfigurationException pce)
        { 
            Log.e("SAX XML", "sax parse error", pce); 
        } 
                
        catch(SAXException se)
        { 
            Log.e("SAX XML", "sax error", se); 
        } 
        
        catch(IOException ioe)
        { 
            Log.e("SAX XML", "sax parse io error", ioe); 
        } 
    }
}