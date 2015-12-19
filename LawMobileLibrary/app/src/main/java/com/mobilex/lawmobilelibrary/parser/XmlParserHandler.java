package com.mobilex.lawmobilelibrary.parser;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mobilex.lawmobilelibrary.util.CommonVariables;

public class XmlParserHandler extends DefaultHandler 
{ 
    private boolean flagSection; 
    private boolean flagPart; 
    private boolean flagClass; 
    private boolean flagOffence; 
    private boolean flagPreamble; 
    private boolean flagOrder; 
    private boolean flagRule; 
    private boolean flagContent; 
    
    private boolean flagClassification; 
    private boolean flagOffenceclass; 
    private boolean flagPunishment; 
    private boolean flagCognizable; 
    private boolean flagBailable; 
    private boolean flagTriable; 
    private boolean flagCompoundable; 
    
    private boolean flagForm;
    
    private String strOrderId = "", strTemp1 = "", strTemp2 = "";
    private StringBuffer strBuff = null;

	int count = 1;
    
    @Override 
    public void startDocument() throws SAXException 
    { 
    } 
    
    @Override 
    public void endDocument() throws SAXException 
    { 
    } 
    
    @Override 
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException 
    { 
        if(localName.equals("section")) 
        {         	
            flagSection = true;  
            
            String strId  = atts.getValue("id");
            String strSno = atts.getValue("sno");            
            CommonVariables.MAP_KEY_ID.put(strId, strSno);
            CommonVariables.MAP_KEY_SNO.put(strSno, strId);
            
            String strValue = atts.getValue("value");
            CommonVariables.LIST_SECTION_VALUES.add(strValue);     
        } 
        
        else if(localName.equals("form")) 
        { 
            flagForm = true;  
            
            String strId  = atts.getValue("id");
            String strSno = atts.getValue("sno");            
            CommonVariables.MAP_KEY_ID_RULE.put(strId, strSno);
            CommonVariables.MAP_KEY_SNO_RULE.put(strSno, strId);
            
            String strValue = atts.getValue("value");
            CommonVariables.LIST_RULE_VALUES.add(strValue);     
        } 
        
        else if(localName.equals("part")) 
        { 
            flagPart = true;  
            
            String strId  = atts.getValue("id");
            String strSno = atts.getValue("sno");            
            CommonVariables.PART_KEY_ID.put(strId, strSno);         
        } 
                
        else if(localName.equals("order")) 
        { 
            flagOrder = true;       
            
            String strId  = atts.getValue("id");
            
            if(!CommonVariables.RULE_ORDER_FLAG)
            {
                String strSno = atts.getValue("sno");            
                CommonVariables.MAP_KEY_ID.put(strId, strSno);
                CommonVariables.MAP_KEY_SNO.put(strSno, strId);

                String strValue = atts.getValue("value");
                CommonVariables.LIST_SECTION_VALUES.add(strValue);
            }
            
            strOrderId = strId;
        } 
                        
        else if(localName.equals("rule")) 
        {             
            if(CommonVariables.RULE_ORDER_FLAG && CommonVariables.RULE_ORDER_ID.equalsIgnoreCase(strOrderId))
            {
                flagRule = true;
                String strId  = atts.getValue("id");
                String strSno = atts.getValue("sno");            
                CommonVariables.MAP_KEY_ID_RULE.put(strId, strSno);
                CommonVariables.MAP_KEY_SNO_RULE.put(strSno, strId);

                String strValue = atts.getValue("value");
                CommonVariables.LIST_RULE_VALUES.add(strValue);
            }            
        } 
                
        else if(localName.equals("class")) 
        { 
            flagClass = true;                       
        } 
                        
        else if(localName.equals("offence")) 
        { 
            flagOffence = true;  
            
            String strSno = atts.getValue("sno");            
            CommonVariables.MAP_KEY_ID.put(strSno, strSno);
            
            String strValue = atts.getValue("value");
            CommonVariables.LIST_SECTION_VALUES.add(strValue);  
        } 
                
        else if(localName.equals("preamble")) 
        { 
            flagPreamble = true;       
        } 
        
        else if(localName.equals("content")) 
        { 
            flagContent = true;      
            
            strBuff = new StringBuffer("");
        } 
        
        else if(localName.equals("classification")) 
        { 
            flagClassification = true;   
            
            String strId = atts.getValue("id");            
            strOrderId   = strId;
            
            if(CommonVariables.IPC_FLAG)
            {
                strBuff.append("<p><hr></p><center><b><font color=\"blue\">").append("CODE OF CRIMINAL PROCEDURE/FIRST SCHEDULE/CLASSIFICATION OF OFFENCES").append("</font></b></center>");
            }             
            
            else
            {
                Log.i("SecNsec","IPC_FLAG : " + CommonVariables.IPC_FLAG);
                Log.i("SecNsec","Order Id : " + strId);
                strBuff = new StringBuffer("");
            }
        } 
        
        else if(localName.equals("offenceclass")) 
        { 
            flagOffenceclass = true;       
        } 
        
        else if(localName.equals("punishment")) 
        { 
            flagPunishment = true;       
        } 
        
        else if(localName.equals("cognizable")) 
        { 
            flagCognizable = true;       
        } 
        
        else if(localName.equals("bailable")) 
        { 
            flagBailable = true;       
        } 
        
        else if(localName.equals("triable")) 
        { 
            flagTriable = true;       
        } 
        
        else if(localName.equals("compoundable")) 
        { 
            flagCompoundable = true;       
        } 
    } 
    
    @Override 
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException 
    {         
        if(localName.equals("section"))
        { 
            flagSection = false; 
        }  
        
        else if(localName.equals("form"))
        { 
            flagForm = false; 
        }  
        
        else if(localName.equals("part"))
        { 
            flagPart = false; 
        }   
        
        else if(localName.equals("order")) 
        { 
            flagOrder = false;                       
        } 
                        
        else if(localName.equals("rule")) 
        { 
            flagRule = false;       
        } 
                
         if(localName.equals("class"))
        { 
            flagClass = false; 
        }  
        
        else if(localName.equals("offence"))
        { 
            flagOffence = false; 
        }   
                
        else if(localName.equals("preamble")) 
        { 
            flagPreamble = false;       
        } 
        
        else if(localName.equals("content"))
        { 
            flagContent = false; 
            
            String strContent = strBuff.toString().trim();
            
            if(flagForm)
                CommonVariables.LIST_RULE_CONTENT.add(strContent); 
            
            else if(flagRule)
                CommonVariables.LIST_RULE_CONTENT.add(strContent); 
                    
            else if(flagPart)
                CommonVariables.LIST_PART_CONTENT.add(strContent);  
                    
            else
                CommonVariables.LIST_SECTION_CONTENT.add(strContent); 
        }             
         
        else if(localName.equals("classification")) 
        { 
            flagClassification = false;       
            
            if(CommonVariables.RULE_ORDER_ID.equalsIgnoreCase(strOrderId))
            {
                String strContent = strBuff.toString();
                CommonVariables.LIST_RULE_CONTENT.add(strContent); 
            }
        } 
        
        else if(localName.equals("offenceclass")) 
        { 
            flagOffenceclass = false;       
        } 
        
        else if(localName.equals("punishment")) 
        { 
            flagPunishment = false;       
        } 
        
        else if(localName.equals("cognizable")) 
        { 
            flagCognizable = false;       
        } 
        
        else if(localName.equals("bailable")) 
        { 
            flagBailable = false;       
        } 
        
        else if(localName.equals("triable")) 
        { 
            flagTriable = false;       
        } 
         
        else if(localName.equals("compoundable")) 
        { 
            flagCompoundable = false;       
        } 
    } 
    
    @Override 
    public void characters(char ch[], int start, int length)
    {    
        if(flagOffenceclass 
        || flagPunishment 
        || flagCognizable 
        || flagBailable 
        || flagTriable
        || flagCompoundable) 
        {         
            strBuff.append("<p><font color=\"blue\">").append(ch, start, length).append("</font></p>");            
        } 
        
        else if((flagContent && flagSection)
        || (flagContent && flagForm)       
        || (flagContent && flagRule)
        || (flagContent && flagOffence)) 
        {         
            strBuff.append("<p>").append(ch, start, length).append("</p>");
        } 
        
        else if((flagContent && flagPart)              
             || (flagContent && flagClass)              
             || (flagContent && flagPreamble))
        {         
            strBuff.append(ch, start, length);
        }                         
    } 
} 