package asywalul.minang.wisatasumbar.util;

import android.content.Context;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HttpManager {
	Context context;
	HttpClient client = new DefaultHttpClient();

	public HttpManager(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	// This method for GET
	public String getHttp(String url) {

		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		HttpGet get = new HttpGet(url);
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setUseExpectContinue(params, false);
		get.setParams(params);

		try {

			HttpResponse response = client.execute(get);

			StatusLine statusLine = response.getStatusLine();
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

	// This method for POST with add parameters
	public void postContentParameters(String url, ArrayList<NameValuePair> pairs)
			throws Exception {

		BufferedReader in = null;
		// StringBuilder builder = new StringBuilder();
		HttpPost post = new HttpPost(url);
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setUseExpectContinue(params, false);
		post.setParams(params);

		try {

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(pairs);
			post.setEntity(formEntity);
			
			HttpResponse response = client.execute(post);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			String codeStatus = String.valueOf(statusCode);

			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));

			Log.d("status Code Post", codeStatus);

			StringBuffer sb = new StringBuffer("");
			Log.d("SB 1", sb.toString());

			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			Log.d("SB 2", sb.toString());
			in.close();

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}

	}
	
	public void putContent(String url, ArrayList<NameValuePair> pairs) throws Exception {

		BufferedReader in = null;
		HttpPut put = new HttpPut(url);
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setUseExpectContinue(params, false);
		put.setParams(params);

		try {
			
			HttpResponse response = client.execute(put);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			String codeStatus = String.valueOf(statusCode);

			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));

			Log.d("status Code Post", codeStatus);

			StringBuffer sb = new StringBuffer("");
			Log.d("SB 1", sb.toString());

			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			Log.d("SB 2", sb.toString());
			in.close();

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}

	public void postContent(String url) throws Exception {

		BufferedReader in = null;
		// StringBuilder builder = new StringBuilder();
		HttpPost post = new HttpPost(url);
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setUseExpectContinue(params, false);
		post.setParams(params);

		try {
			
			HttpResponse response = client.execute(post);

			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			String codeStatus = String.valueOf(statusCode);

			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));

			Log.d("status Code Post", codeStatus);

			StringBuffer sb = new StringBuffer("");
			Log.d("SB 1", sb.toString());

			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			Log.d("SB 2", sb.toString());
			in.close();

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}
}
