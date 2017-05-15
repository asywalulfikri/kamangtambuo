package asywalul.minang.wisatasumbar.util;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HttpManager1 {


    public static String httpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {
       BufferedReader in = null;
       String statusCode;
       try {
           HttpClient client = new DefaultHttpClient();
           HttpPost request = new HttpPost(url);
           UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
           request.setEntity(formEntity);
           HttpResponse response = client.execute(request);
           statusCode = String.valueOf(response.getStatusLine().getStatusCode());
           in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

           StringBuffer sb = new StringBuffer("");
           String line = "";
           String NL = System.getProperty("line.separator");
           while ((line = in.readLine()) != null) {
               sb.append(line + NL);
           }
           in.close();

           String result = sb.toString()+"`"+statusCode;
           Log.e("RESULT", result);
           return result;
       } finally {
           if (in != null) {
               try {
                   in.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }
   }
   
	public static String httpGet(String Url) throws Exception {
		BufferedReader in = null;
		String statusCode;
	       try {
	           HttpClient client = new DefaultHttpClient();
	           HttpGet request = new HttpGet(Url);
	           Log.e("URL", Url);
	           HttpResponse response = client.execute(request);
	           statusCode = String.valueOf(response.getStatusLine().getStatusCode());
	           Log.e("Status Code", String.valueOf(statusCode));
	           in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	           StringBuffer sb = new StringBuffer("");
	           String line = "";
	           String NL = System.getProperty("line.separator");
	           while ((line = in.readLine()) != null) {
	               sb.append(line + NL);
	           }
	           in.close();

	           String result = sb.toString()+"`"+statusCode;
	           Log.e("RESULT", result);
	           return result;
	       } finally {
	           if (in != null) {
	               try {
	                   in.close();
	               } catch (IOException e) {
	                   e.printStackTrace();
	               }
	           }
	       }

	}
}