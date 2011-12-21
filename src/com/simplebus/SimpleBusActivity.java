package com.simplebus;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SimpleBusActivity extends Activity {
	private EditText editView;
	private Button submitButton;
	private ListView resultView;
	private ResultAdapter resultAdapter;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initialView();
    }
    private void initialView(){
    	editView = (EditText) findViewById(R.id.editText1);
    	submitButton = (Button) findViewById(R.id.submit);
    	submitButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Editable search = editView.getText();
				search(search.toString());
				
			}
    		
    	});
    	resultView = (ListView) findViewById(R.id.list);
    }
    private void search(String s){  	
    	try {
    		JSONObject data = new JSONObject();
        	data.put("data", s);
			JSONObject result = ConnectSimpleBusServer.connectServer(data.toString());
			JSONArray resultData = result.getJSONArray("data");
			int size = resultData.length();
			if(size!=0){
				Data[] input = new Data[size];
				for(int i=0;i<size;i++){
					JSONObject d = (JSONObject) resultData.get(i);
					input[i] = new Data(d.getString("bus"),d.getString("time"));
				}
				resultAdapter = new ResultAdapter(SimpleBusActivity.this,input);
				resultView.setAdapter(resultAdapter);
			}
			
			
		} catch (ConnectTimeoutException e) {
			Toast.makeText(SimpleBusActivity.this, "Search Error", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (JSONException e) {
			Toast.makeText(SimpleBusActivity.this, "JSON Error", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
    }
}