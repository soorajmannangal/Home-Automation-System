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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class login extends Activity {
	EditText edtUsrName,edtPswd,edtServerAddress,edtBattery;
	ImageView imgLogin;
	String batLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		imgLogin=(ImageView)findViewById(R.id.imgLogin);
		edtPswd=(EditText)findViewById(R.id.edtPassword);
		edtUsrName=(EditText)findViewById(R.id.editText1);
		edtServerAddress=(EditText)findViewById(R.id.edtServerAddress);
		edtBattery=(EditText)findViewById(R.id.edtBattery);
		
		
		//
		
		imgLogin.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				imgLogin.setImageResource(R.drawable.login2);
				return false;
			}
		});
		imgLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String addr=null,serveraddr=null,response;
				//server address
				//serveraddr="http://192.168.0.20/has_project/android_get";
				
				serveraddr=edtServerAddress.getText().toString().trim();
				
				response=postData(edtUsrName.getText().toString(),edtPswd.getText().toString(),serveraddr);
				
				
				if(response.equalsIgnoreCase("error connection"))
				{
				Toast.makeText(getBaseContext(),response , Toast.LENGTH_LONG).show();
				}
				else{
				
				//incoming format OK-192.168.0.12 or FAIL in response
				if(response.contains("OK") && response.length() < 100)
				{
					addr=response.substring(3);
					//Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_LONG).show();
					batLevel=edtBattery.getText().toString().trim();
					Intent myInt = new Intent(getBaseContext(),main.class);
					myInt.putExtra("batLevel", batLevel);
					myInt.putExtra("devaddr", addr);
					startActivityForResult(myInt,0 );
					finish();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Invalid password or username", Toast.LENGTH_LONG).show();
				}
				
				}
				imgLogin.setImageResource(R.drawable.login);
			
				
			}
		});
				
		
		
		
	}
	public String postData(String usrname,String pswd, String serveraddr) {
	    // Create a new HttpClient and Post Header
		
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(serveraddr);
	    HttpResponse response=null;
	    HttpEntity entity;
	    String str = null;
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("username", usrname.trim()));
	        nameValuePairs.add(new BasicNameValuePair("pswd", pswd.trim()));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	    	
	       
	        response = httpclient.execute(httppost);
	            
	        entity = response.getEntity();
	        InputStream is = entity.getContent();
 
	        str=convertStreamToString(is);
	       
	        
	    // String    Toast.makeText(getBaseContext(), convertStreamToString(is), 10000).show();
	        
	    } catch (ClientProtocolException e) {
	    	
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	    	
	    	// Toast.makeText(getBaseContext(), "error connection", Toast.LENGTH_SHORT).show();
	    	 return "error connection";
	    	 // TODO Auto-generated catch block
	    }
		return str;
	   
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
	//
	
 
	
}
