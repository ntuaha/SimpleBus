package com.simplebus;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.simplebus.data.Information;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class ResultActivity extends Activity {
	private ListView resultView;
	private ResultAdapter resultAdapter;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        initialView();
        String data = getIntent().getBundleExtra("data").getString("data");
        search(data);
    }
    private void initialView(){
    	resultView = (ListView) findViewById(R.id.list);
    }
    private void search(String data){  	
    	try {
			JSONObject result = ConnectSimpleBusServer.connectServer(data);
			JSONArray resultData = result.getJSONArray("data");
			int size = resultData.length();
			if(size!=0){
				Data[] input = new Data[size];
				for(int i=0;i<size;i++){
					JSONObject d = (JSONObject) resultData.get(i);
					input[i] = new Data(d.getString("bus"),d.getString("time"));
				}
				resultAdapter = new ResultAdapter(ResultActivity.this,input);
				resultView.setAdapter(resultAdapter);
			}
			
			
		} catch (ConnectTimeoutException e) {
			Toast.makeText(ResultActivity.this, "Search Error", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (JSONException e) {
			Toast.makeText(ResultActivity.this, "JSON Error", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
    }
 }