package com.simplebus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ConnectSimpleBusServer {
	private static final String CONNECT_PATH = "http://nrl.iis.sinica.edu.tw/VProbe/Collect/SimpleBus/test.php";
	static public JSONObject connectServer(String object) throws ConnectTimeoutException{
		JSONObject feedback = null;
		HttpPost httpRequest = new HttpPost(CONNECT_PATH);
		MultipartEntity entity = new MultipartEntity();
		try {
			entity.addPart("data", new StringBody(object,Charset.forName("UTF-8")));
			httpRequest.setEntity(entity);
			HttpResponse httpResponse;
			httpResponse = new DefaultHttpClient().execute(httpRequest);

			if (entity != null) { entity.consumeContent(); }
			switch(httpResponse.getStatusLine().getStatusCode())
			{
			case 200:
				String result = EntityUtils.toString(httpResponse.getEntity());
				Log.i("feedback",result);
				feedback = new JSONObject(result);						
				break;


			}

		} catch (UnsupportedEncodingException e) { e.printStackTrace();
		} catch (ClientProtocolException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); 
		} catch (NumberFormatException e){ e.printStackTrace(); 
		} catch (JSONException e) { 
			e.printStackTrace();
			return null;
		} catch (RuntimeException e){
			return null;
		}


		return feedback;
	}
}
