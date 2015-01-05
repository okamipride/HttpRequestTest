package com.example.httprequesttest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class HttpActivity extends Activity {

	static final String TAG = "HttpActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_http);
	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		new GetDataAsyncTask().execute();
	}

	class GetDataAsyncTask extends AsyncTask < Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			//Uri URL = Uri.parse("http://v.youku.com/v_show/id_XNzMwNjI2NDQ0.html");
			
			try {
			//	URI url = new URI("http://www.dailymotion.com/embed/video/xm8teq_the-cardigans-vs-jamelia-erase-the-superstar_music");
				//URI url = new URI("http://api1.flvurl.net/98E206A6-20141008/help.txt");
				
				URI url = new URI("http://www.youtube.com/get_video_info?video_id=7In_ceAByvA");
				
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(new HttpGet(url));
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				    ByteArrayOutputStream out = new ByteArrayOutputStream();
				    response.getEntity().writeTo(out);
				    out.close();
				    String responseString = out.toString();
				    Map<String, List<String>> videoInfo = getUrlParameters(responseString);
				    List<String> encode = videoInfo.get("url_encoded_fmt_stream_map");
				    String[] formats = encode.get(0).split(",");
				    Map<String, List<String>> formatInfo = getUrlParameters(formats[0]);
				    List<String> urlarr = formatInfo.get("url");
				    String urlstr = urlarr.get(0);
				    //Pattern pattern = Pattern.compile('^(?:http:\/\/|www\.|https:\/\/)([^\/]+)');	
				    String[] urlParts = urlstr.split("//?");
				    
				    
				    //String[] formats = videoInfo.get("url_encoded_fmt_stream_map").split(",");
				    
				    Log.d(TAG, "response string " + responseString);
				    //..more logic
				} else{
					    //Closes the connection.
					    response.getEntity().getContent().close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}	
	}
	
	public static Map<String, List<String>> getUrlParameters(String url)
	        throws UnsupportedEncodingException {
	    Map<String, List<String>> params = new HashMap<String, List<String>>();
	  
	        String query = url;
	        for (String param : query.split("&")) {
	            String pair[] = param.split("=");
	            String key = URLDecoder.decode(pair[0], "UTF-8");
	            String value = "";
	            if (pair.length > 1) {
	                value = URLDecoder.decode(pair[1], "UTF-8");
	            }
	            List<String> values = params.get(key);
	            if (values == null) {
	                values = new ArrayList<String>();
	                params.put(key, values);
	            }
	            values.add(value);
	        }
	    return params;
	}
}



