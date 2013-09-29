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
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Bundle;
import android.text.InputFilter.LengthFilter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class main extends Activity{
	ImageView imgAllOn,imgAllOff,imgRefresh,imgLogout;
	ImageView imgDev1,imgDev2,imgDev3,imgDev4;
	int dev1=0,dev2=0,dev3=0,dev4=0,allon=0,alloff=0,batIntLevel=0;
	public String address=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		imgAllOn=(ImageView)findViewById(R.id.imgAllOn);
		imgAllOff=(ImageView)findViewById(R.id.imgAllOff);
		imgRefresh=(ImageView)findViewById(R.id.imgRefresh);
		imgLogout=(ImageView)findViewById(R.id.imgLogout);
		imgDev1=(ImageView)findViewById(R.id.imgDev1);
		imgDev2=(ImageView)findViewById(R.id.imgDev2);
		imgDev3=(ImageView)findViewById(R.id.imgDev3);
		imgDev4=(ImageView)findViewById(R.id.imgDev4);
		
		this.registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		
		
		final MediaPlayer mp = MediaPlayer.create(this, R.raw.switch1);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			address=extras.getString("devaddr").trim();
			Integer batLe=Integer.parseInt(extras.getString("batLevel").trim());
			//Toast.makeText(getBaseContext(), extras.getString("batLevel").trim(), Toast.LENGTH_SHORT).show();
			batIntLevel=(int)batLe;
		}
		refreshbulbs();
		//4 buttons
		imgLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent myInt = new Intent(getBaseContext(),login.class);
				startActivityForResult(myInt,0 );
				finish();
				
			}
		});
		imgRefresh.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				imgRefresh.setImageResource(R.drawable.refresh1);
				return false;
			}
		});
		imgRefresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				refreshbulbs();
				
			}
		});
		imgAllOn.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				imgAllOn.setImageResource(R.drawable.allon2);
				return false;
			}
		});
		imgAllOn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mp.start();
				postCommand("allon");
				imgDev1.setImageResource(R.drawable.bulbon);
				dev1=1;
				imgDev2.setImageResource(R.drawable.bulbon);
				dev2=1;
				imgDev3.setImageResource(R.drawable.bulbon);
				dev3=1;
				imgDev4.setImageResource(R.drawable.bulbon);
				dev4=1;
				imgAllOn.setImageResource(R.drawable.allon1);
			}
		});
		imgAllOff.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				imgAllOff.setImageResource(R.drawable.alloff2);
				return false;
			}
		});
		imgAllOff.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mp.start();
				postCommand("alloff");
				imgDev1.setImageResource(R.drawable.bulboff);
				dev1=0;
				imgDev2.setImageResource(R.drawable.bulboff);
				dev2=0;
				imgDev3.setImageResource(R.drawable.bulboff);
				dev3=0;
				imgDev4.setImageResource(R.drawable.bulboff);
				dev4=0;
				imgAllOff.setImageResource(R.drawable.alloff1);
			}
		});
		//close 4buttons
		
		//devices long press
		imgDev1.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Intent myInt =new Intent(getBaseContext(),scheduleActivity.class);
				myInt.putExtra("devname", "dev1");
				myInt.putExtra("addr", address);
				startActivityForResult(myInt, 0);
				return false;
			}
		});
		imgDev2.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Intent myInt =new Intent(getBaseContext(),scheduleActivity.class);
				myInt.putExtra("devname", "dev2");
				myInt.putExtra("addr", address);
				startActivityForResult(myInt, 0);
				return false;
			}
		});
		imgDev3.setOnLongClickListener(new View.OnLongClickListener() {
	
	@Override
	public boolean onLongClick(View v) {
		Intent myInt =new Intent(getBaseContext(),scheduleActivity.class);
		myInt.putExtra("devname", "dev3");
		myInt.putExtra("addr", address);
		startActivityForResult(myInt, 0);
		return false;
	}
		});
		imgDev4.setOnLongClickListener(new View.OnLongClickListener() {
	
	@Override
	public boolean onLongClick(View v) {
		Intent myInt =new Intent(getBaseContext(),scheduleActivity.class);
		myInt.putExtra("devname", "dev4");
		myInt.putExtra("addr", address);
		startActivityForResult(myInt, 0);
		return false;
	}
		});
		//close long press
		//devices
		imgDev1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mp.start();
				if(dev1==0)
					{
					imgDev1.setImageResource(R.drawable.bulbon);
					dev1=1;
					postData("dev1","ON");
					}
				else{
					imgDev1.setImageResource(R.drawable.bulboff);
					dev1=0;
					postData("dev1","OFF");
				}
		
			}
		});
		imgDev2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mp.start();
				if(dev2==0)
					{
					imgDev2.setImageResource(R.drawable.bulbon);
					dev2=1;
					postData("dev2","ON");
					}
				else{
					imgDev2.setImageResource(R.drawable.bulboff);
					dev2=0;
					postData("dev2","OFF");
				}
		
			}
		});
		imgDev3.setOnClickListener(new View.OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
		// TODO Auto-generated method stub
				mp.start();
				if(dev3==0)
			{
			imgDev3.setImageResource(R.drawable.bulbon);
			dev3=1;
			postData("dev3","ON");
			}
				else{
			imgDev3.setImageResource(R.drawable.bulboff);
			dev3=0;
			postData("dev3","OFF");
		}

	}
		});
		imgDev4.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		mp.start();
		if(dev4==0)
			{
			imgDev4.setImageResource(R.drawable.bulbon);
			dev4=1;
			postData("dev4","ON");
			}
		else{
			imgDev4.setImageResource(R.drawable.bulboff);
			dev4=0;
			postData("dev4","OFF");
		}

	}
		});

//close devices
	}
	public void refreshbulbs()
	{
		String resp;
		resp=postCommand("get");  // Get all status 
		//Toast.makeText(this, resp, Toast.LENGTH_LONG).show();
		//Incoming format  1010  or  11 or 00 or 01
		
		if(Character.toString(resp.charAt(0)).equalsIgnoreCase("0"))
		{
			imgDev1.setImageResource(R.drawable.bulboff);
			dev1=0;
			
		}
		else
		{
			imgDev1.setImageResource(R.drawable.bulbon);
			dev1=1;
			
		}
		if(Character.toString(resp.charAt(1)).equalsIgnoreCase("0"))
		{
			imgDev2.setImageResource(R.drawable.bulboff);
			dev2=0;
		}
		else
		{
			imgDev2.setImageResource(R.drawable.bulbon);
			dev2=1;
		}
		if(Character.toString(resp.charAt(2)).equalsIgnoreCase("0"))
		{
			imgDev3.setImageResource(R.drawable.bulboff);
			dev3=0;
			
		}
		else
		{
			imgDev3.setImageResource(R.drawable.bulbon);
			dev3=1;
			
		}
		if(Character.toString(resp.charAt(3)).equalsIgnoreCase("0"))
		{
			imgDev4.setImageResource(R.drawable.bulboff);
			dev4=0;
		}
		else
		{
			imgDev4.setImageResource(R.drawable.bulbon);
			dev4=1;
		}
		imgRefresh.setImageResource(R.drawable.refresh2);
	}
	public String postCommand(String value) {
	    // Create a new HttpClient and Post Header
		
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(address);
	    InputStream is=null;
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("type", "command"));
	        nameValuePairs.add(new BasicNameValuePair("cmd", value));
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
	
	
	public void postData(String devno,String status) {
	    // Create a new HttpClient and Post Header
		
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(address);

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("type", "data"));
	        nameValuePairs.add(new BasicNameValuePair("devno", devno));
	        nameValuePairs.add(new BasicNameValuePair("status", status));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	    	
	       // HttpResponse response = 
	        		httpclient.execute(httppost);
	       // HttpEntity entity = response.getEntity();
	      //  InputStream is = entity.getContent();
	        
	       
	       
	        
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
	/*public void onStop()
	{
		finish();
		super.onStop();
	}*/
	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			int  health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
			int  icon_small= intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0);
			int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
			int  plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
			boolean  present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT); 
			int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
			int  status= intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
			String  technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
			int  temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
			int  voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
			
			
			
			if(level > batIntLevel && plugged > 0)
			{
				postData("dev1","OFF");
				
			}
			
			/*if(level < 46 ){
				
				postData("dev1","ON");
				if(plugged == 0)
					postData("dev1","OFF");
			}
				*/
			Integer bat=plugged;
			String bl=bat.toString();
			//Toast.makeText(getBaseContext(), bl, Toast.LENGTH_SHORT).show();
			/*
			batteryInfo.setText(
					"Health: "+health+"\n"+
					"Icon Small:"+icon_small+"\n"+
					"Level: "+level+"\n"+
					"Plugged: "+plugged+"\n"+
					"Present: "+present+"\n"+
					"Scale: "+scale+"\n"+
					"Status: "+status+"\n"+
					"Technology: "+technology+"\n"+
					"Temperature: "+temperature+"\n"+
					"Voltage: "+voltage+"\n");
			imageBatteryState.setImageResource(icon_small);
			*/
		}
	};
    
}
