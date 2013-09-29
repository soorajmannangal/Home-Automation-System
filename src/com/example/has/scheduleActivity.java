package com.example.has;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class scheduleActivity extends Activity{
	
	TextView tvDevNo;
	EditText edtOnHh,edtOffHh,edtOnMm,edtOffMm;
	CheckBox chkOn,chkOff;
	ImageView imgDone;
	String address,devno;
	String turnOnTime,turnOffTime;
	public String stTime[],endTime[],scTime,preScTime="dev1-1-08-10>dev1-0-09-10",temp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
		
		tvDevNo=(TextView)findViewById(R.id.tvDevicNumber);
		edtOnHh=(EditText)findViewById(R.id.edtOnHh);
		edtOffHh=(EditText)findViewById(R.id.edtOffHh);
		edtOffMm=(EditText)findViewById(R.id.edtOffMm);
		edtOnMm=(EditText)findViewById(R.id.edtOnMm);
		imgDone=(ImageView)findViewById(R.id.imgDone);
		chkOn=(CheckBox)findViewById(R.id.chkOnEnable);
		chkOff=(CheckBox)findViewById(R.id.chkOffEnable);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			devno=extras.getString("devname").trim();
			tvDevNo.setText(devno);
			address=extras.getString("addr").trim();
			preScTime=postCommand(devno);
			//Toast.makeText(getBaseContext(), temp, Toast.LENGTH_LONG).show();
			
		}
		//   process previous schedule data
		 
		String []schedules =  preScTime.split(">");
		//========= schedule[0] for ON scheduling ==========
		 String [] data = schedules[0].split("-");
		 
		 if(data[1].equals("1")){
			 chkOn.setChecked(true);
			 edtOnHh.setText(data[2]);
			 edtOnMm.setText(data[3]);
		 }else chkOn.setChecked(false);
		//========= schedule[1] for OFF scheduling ==========
		  data = schedules[1].split("-");
		 
		 if(data[1].equals("1")){
			 chkOff.setChecked(true);
			 edtOffHh.setText(data[2]);
			 edtOffMm.setText(data[3]);
		 }else chkOff.setChecked(false);

		//===============================
		
		edtOnHh.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				
				int onhh=Integer.parseInt(edtOnHh.getText().toString().trim());
				if(onhh > 24)
					edtOnHh.setText("12");
				
			}
		});
		edtOffHh.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				
				int onhh=Integer.parseInt(edtOffHh.getText().toString().trim());
				if(onhh > 23)
					edtOffHh.setText("12");
				
			}
		});
edtOnMm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				
				int onmm=Integer.parseInt(edtOnMm.getText().toString().trim());
				if(onmm > 59)
					edtOnMm.setText("00");
				
			}
		});
		edtOffMm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				
				int offmm=Integer.parseInt(edtOffMm.getText().toString().trim());
				if(offmm > 59)
					edtOffMm.setText("00");
				
			}
		});
		imgDone.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				imgDone.setImageResource(R.drawable.done1);
				return false;
			}
		});
		imgDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(chkOn.isChecked())
				{
					turnOnTime=edtOnHh.getText().toString()+'-'+edtOnMm.getText().toString();
				}
				else{
					turnOnTime="disabled";
				}
				if(chkOn.isChecked())
				{
					turnOffTime=edtOffHh.getText().toString()+'-'+edtOffMm.getText().toString();
				}
				else{
					turnOffTime="disabled";
				}
			
				postSchedule(devno,turnOnTime,turnOffTime);
				//
				
				imgDone.setImageResource(R.drawable.done2);
			   finish();
				//Intent myInt = new Intent(scheduleActivity.this,main.class);
				//startActivity(myInt);
				
			}
		});
				
	}
	public void postSchedule(String devno,String turnontime,String turnofftime) {
	    // Create a new HttpClient and Post Header
		
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(address);

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("type", "setsch"));
	        nameValuePairs.add(new BasicNameValuePair("devno", devno));
	        nameValuePairs.add(new BasicNameValuePair("turnontime", turnontime));
	        nameValuePairs.add(new BasicNameValuePair("turnofftime", turnofftime));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	    	
	        HttpResponse response = httpclient.execute(httppost);
	            
	      //  Toast.makeText(getBaseContext(), convertStreamToString(is), Toast.LENGTH_LONG).show();
	        
	    } catch (ClientProtocolException e) {
	    	
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	    	 Toast.makeText(getBaseContext(), "error connection", Toast.LENGTH_SHORT).show();
	        // TODO Auto-generated catch block
	    }
	} 
	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append((line + "\n"));
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	
	public String postCommand(String value) {
	    // Create a new HttpClient and Post Header
		
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(address);
	    InputStream is=null;
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("type", "getsch"));
	        nameValuePairs.add(new BasicNameValuePair("devno", value));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	    	
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	         is = entity.getContent();
     //return convertStreamToString(is);
	        
	    } catch (ClientProtocolException e) {
	    	
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	    	 Toast.makeText(getBaseContext(), "error connection", Toast.LENGTH_SHORT).show();
	    	 return "error connection";
	    	 // TODO Auto-generated catch block
	    }
		return convertStreamToString(is);
	} 
	
	

}
