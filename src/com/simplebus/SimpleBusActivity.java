package com.simplebus;


import org.json.JSONException;
import org.json.JSONObject;

import com.simplebus.data.Information;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class SimpleBusActivity extends Activity {
	private EditText editView;
	private Button submitButton;
	private ListView informationView;
	private InformationAdapter InformationAdapter;
	private Information information;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initialView();
	}
	private void initialView(){
		//editView = (EditText) findViewById(R.id.editText1);
		submitButton = (Button) findViewById(R.id.submit);
		submitButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				search(localRequest());
			}

		});
		informationView = (ListView) findViewById(R.id.list);
		information = new Information();
		InformationAdapter = new InformationAdapter(SimpleBusActivity.this,information.getData());
		informationView.setAdapter(InformationAdapter);
		informationView.setOnItemClickListener(new ListView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				switch(arg2){
				case 0:
					information.setBusId(SimpleBusActivity.this,InformationAdapter);
					break;
				case 1:
					information.setDate(SimpleBusActivity.this,InformationAdapter);
					break;
				case 2:
					information.setTime(SimpleBusActivity.this,InformationAdapter);
					break;
				}	
				
			}
			
	    });

	}
	//Send to Server
	private JSONObject localRequest(){
		JSONObject data = new JSONObject();
		try {
			data.put("bus", information.busid);
			data.put("year",information.year);
			data.put("month",information.month+1);
			data.put("day",information.day);
			data.put("hour",information.hour);
			data.put("min",information.min);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;    	
	}

	private void search(JSONObject s){  	

		try {
			JSONObject data = new JSONObject();
			data.put("data", s);
			Bundle bundle = new Bundle();
			bundle.putString("data", data.toString());
			Intent intent = new Intent();
			intent.putExtra("data", bundle);
			intent.setClass(SimpleBusActivity.this,ResultActivity.class);
			startActivity(intent);

		}catch (JSONException e) {
			Toast.makeText(SimpleBusActivity.this, "JSON Error", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}