package asywalul.minang.wisatasumbar.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ImageUtil {

	public static  Bitmap getBitmapFromURL(String src) {
		if (src.equals("") || src.equals("-")) return null;
		
		try {
			URL url = new URL(src);
	
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setDoInput(true);
			connection.connect();
	
			InputStream input	 = connection.getInputStream();
			Bitmap myBitmap		 = BitmapFactory.decodeStream(input);
			
			return myBitmap;
		} catch (IOException e) {
		    return null;
		}
	}
	
	public static void DownloadFromUrl(String imageURL, String fileName) {  //this is the downloader method
        try {
                URL url = new URL(imageURL); //you can write here any link
                File file = new File(fileName);

                long startTime = System.currentTimeMillis();
                Log.d("ImageManager", "download begining");
                Log.d("ImageManager", "download url:" + url);
                Log.d("ImageManager", "downloaded file name:" + fileName);
                /* Open a connection to that URL. */
                URLConnection ucon = url.openConnection();

                /*
                 * Define InputStreams to read from the URLConnection.
                 */
                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                /*
                 * Read bytes to the Buffer until there is nothing more to read(-1).
                 */
                ByteArrayBuffer baf = new ByteArrayBuffer(50);
                int current = 0;
                while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                }

                /* Convert the Bytes read to a String. */
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baf.toByteArray());
                fos.close();
                Log.d("ImageManager", "download ready in"
                                + ((System.currentTimeMillis() - startTime) / 1000)
                                + " sec");

        } catch (IOException e) {
                Log.d("ImageManager", "Error: " + e);
        }

}
	
	public static Bitmap getBitmapFromFile(String src) {
		File file = new File(src);
		
		if (file.exists()) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			
			 options.inJustDecodeBounds = false;
			 options.inPreferredConfig = Config.RGB_565;
			 options.inDither = true;
			 
			return BitmapFactory.decodeFile(src, options);
		}
		
		return null;
	}
	
	public static boolean saveBitmapToFile(String path, Bitmap bitmap) {
		File file 	= new File(path);
		boolean res = false;

		try {
			if (file.exists()) {
				file.delete();
			}
			
			FileOutputStream fos = new FileOutputStream(file);
				
			res = bitmap.compress(CompressFormat.JPEG, 90, fos);
				
			fos.close();
		} catch (Exception e) { }
		
		return res;
	}
}
