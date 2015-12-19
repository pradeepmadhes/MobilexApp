package com.mobilex.lawmobilelibrary.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import com.mobilex.lawmobilelibrary.activities.R;

public class IndexAdapter extends BaseAdapter 
{
    private ArrayList<String> listTitle = null, listBody = null;
 private LayoutInflater mInflater;

 public IndexAdapter(Context context, ArrayList<String> list1, ArrayList<String> list2) {
  mInflater = LayoutInflater.from(context);
  listTitle = list1;
  listBody = list2;
 }

 public int getCount() {
  return listTitle.size();
 }

 public Object getItem(int position) {
  return listTitle.get(position);
 }

 public long getItemId(int position) {
  return position;
 }

 public View getView(int position, View convertView, ViewGroup parent) {
  ViewHolder holder;
  if (convertView == null) {
   convertView = mInflater.inflate(R.layout.index_list_items, null);
   holder = new ViewHolder();
   holder.txtTitle = (TextView) convertView.findViewById(R.id.index_title);
   holder.txtBody = (TextView) convertView.findViewById(R.id.index_body);

   convertView.setTag(holder);
  } else {
   holder = (ViewHolder) convertView.getTag();
  }
  
  String strTitle = listTitle.get(position);
  Log.i("#____#", "$$$$ : " + strTitle);
  if(strTitle.equals("no_title"))
  {      
      holder.txtTitle.setTextSize(0);
      holder.txtTitle.setPadding(0,0,0,0);
      holder.txtTitle.setText("");
      Log.i("##", "$$$$ : 1");
  }
  
  else
  {      
      holder.txtTitle.setTextSize(22);
      holder.txtTitle.setPadding(7,7,7,7);
      holder.txtTitle.setText(strTitle);
      Log.i("##", "$$$$ : 2" + " : " + strTitle);
  }
  
  String strBody = listBody.get(position);
  Log.i("#____#", "$$$$ : " + strBody);
  if(strBody.equals("no_body"))
  {      
      holder.txtBody.setTextSize(0);
      holder.txtBody.setPadding(0,0,0,0);
      holder.txtBody.setText("");
      Log.i("##", "$$$$ : 3");
  }
  
  else
  {      
      holder.txtBody.setTextSize(20);
      holder.txtBody.setPadding(7,7,7,7);
      holder.txtBody.setText(strBody);
      Log.i("##", "$$$$ : 4" + " : " + strBody);
  }

  return convertView;
 }

 static class ViewHolder {
  TextView txtTitle;
  TextView txtBody;
 }
}