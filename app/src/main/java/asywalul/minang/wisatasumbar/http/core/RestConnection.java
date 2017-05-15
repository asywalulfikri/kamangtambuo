package asywalul.minang.wisatasumbar.http.core;

import asywalul.minang.wisatasumbar.util.Cons;
import asywalul.minang.wisatasumbar.util.Debug;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class RestConnection {

//	This function for GET
	public InputStream connectGet(String url) throws Exception {
		
		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");
		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		InputStream stream = null;
		
		Debug.i(Cons.TAG, "GET " + url);
		
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			HttpEntity httpEntity = httpResponse.getEntity();
			
			if (httpEntity == null) throw new Exception();
			
			stream = httpEntity.getContent();
			
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		return stream;
	}
	
//	This constructor for POST 
	public InputStream connectPost(String url, List<NameValuePair> params) throws Exception {
		return connectPost(url, params, 0);
	}

//	This function for POST with Parameters
	public InputStream connectPost(String url, List<NameValuePair> params, int timeout) throws Exception {
		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");
		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		InputStream stream = null;
		
		Debug.i(Cons.TAG, "POST " + url);
		
		try {
			timeout = (timeout == 0) ? Cons.HTTP_CONNECTION_TIMEOUT : timeout;
			
			Debug.i("Timeout set to " + String.valueOf(timeout) + " miliseconds");
			
			HttpParams httpParams = httpClient.getParams();
			
			HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
			HttpConnectionParams.setSoTimeout(httpParams, timeout);
			
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			HttpEntity httpEntity = httpResponse.getEntity();
			
			if (httpEntity == null) throw new Exception();
			
			stream = httpEntity.getContent();
			
		} catch (Exception e) {
			throw e;
		}
		return stream;
	}
	
//	This funtion for CHECK
	public InputStream connectCheck(String url, List<NameValuePair> params) throws Exception {
		HttpClient httpClient 	= new DefaultHttpClient();
		HttpPost httpPost 		= new HttpPost(url); 
		InputStream stream		= null;
		
		Debug.i(Cons.TAG, "POST " + url);
		
		try {
			HttpParams hparams = httpClient.getParams();
			
			HttpConnectionParams.setConnectionTimeout(hparams, 15000);
			HttpConnectionParams.setSoTimeout(hparams, 15000);
			
	        httpPost.setEntity(new UrlEncodedFormEntity(params));
	        
	        HttpResponse httpResponse = httpClient.execute(httpPost);
			
			HttpEntity httpEntity = httpResponse.getEntity();
			
			if (httpEntity == null) throw new Exception("");
			
			stream = httpEntity.getContent();			
		} catch (Exception e) {
			throw e;
		}
		
		return stream;
	}
	
//	This function for PUT
	public InputStream connectPut(String url, List<NameValuePair> params) throws Exception {
		HttpClient httpClient 	= new DefaultHttpClient();
		HttpPut httpPut 		= new HttpPut(url); 
		InputStream stream		= null;
		
		Debug.i(Cons.TAG, "POST " + url);
		
		try {
			httpPut.setEntity(new UrlEncodedFormEntity(params));
	        
			HttpResponse httpResponse = httpClient.execute(httpPut);
			
			HttpEntity httpEntity = httpResponse.getEntity();
			
			if (httpEntity == null) throw new Exception("");
			
			stream = httpEntity.getContent();			
		} catch (Exception e) {
			throw e;
		}
		
		return stream;
	}
	
//	This function for POST without Parameter
	public InputStream connectPost(String url) throws Exception {
		HttpClient httpClient 	= new DefaultHttpClient();
		HttpPost httpPost 		= new HttpPost(url); 
		InputStream stream		= null;
		
		Debug.i(Cons.TAG, "POST " + url);
		
		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			HttpEntity httpEntity = httpResponse.getEntity();
			
			if (httpEntity == null) throw new Exception("");
			
			stream = httpEntity.getContent();			
		} catch (Exception e) {
			throw e;
		}
		
		return stream;
	}
	
	public String getCodePassword(String url) throws Exception {
		HttpClient client 	= new DefaultHttpClient();
		
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		HttpGet get = new HttpGet(url);
//		HttpParams params = new BasicHttpParams();
//		HttpProtocolParams.setUseExpectContinue(params, false);
//		get.setParams(params);

		try {

			HttpResponse response = client.execute(get);

			StatusLine statusLine = (StatusLine) response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			String codeStatus = String.valueOf(statusCode);

			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			reader = new BufferedReader(new InputStreamReader(content));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			Log.d("Status Code", codeStatus);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		}
		return builder.toString();
	}
}
