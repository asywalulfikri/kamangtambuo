package asywalul.minang.wisatasumbar.util;

import android.annotation.SuppressLint;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class StringUtil {
	/**
	 * Make a words's first character uppercase.
	 * 
	 * @param word Word string
	 * 
	 * @return Word with first character uppercase.
	 */
	@SuppressLint("DefaultLocale")
	public static String firstUpperCase(String word) {
		StringBuffer sb = new StringBuffer(word);
		
		sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
		
		return sb.toString();
	}
	
	/**
	 * Uppercase first character of each word on a string.
	 * 
	 * @param words Words 
	 * 
	 * @return Words with first character uppercase.
	 */
	@SuppressLint("DefaultLocale")
	public static String ucFirst(String words) {
		if (words == null) return "";
		if (words.equals("")) return "";
		
		words			= words.toLowerCase();
		String[] word 	= words.split(" ");
		StringBuffer sb	= new StringBuffer();
		
		for (int i = 0; i < word.length; i++) {
			sb.append(firstUpperCase(word[i])).append((i == word.length-1) ? "" : " ");
		}
		
		return sb.toString();
	}
	
	/**
	 * Format size from bytes into 'x KB' or 'x MB', for example 1000 bytes will return '1 KB'
	 * 
	 * @param size Size
	 * 
	 * @return Formatted size
	 */
	public static String formatSize(int size) {
		String result = "";
		
		double tmp = (double) size / 1000.0;
		
		if (tmp > 1000) {
			tmp = tmp / 1000.0;
			
			DecimalFormat dF = new DecimalFormat("00");
			
			dF.applyPattern("0.#");
			
			result = dF.format(tmp) + " MB";
		} else {
			result = (int) tmp + " KB";
		}
		
		return result;
	}
	
	/**
	 * Add leading zero to a string of number if less than 10, for example '5' will return '05'
	 * 
	 * @param c Number
	 * 
	 * @return String of number with leading zero. 
	 */
	public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
	
	/**
	 * Convert stream to string.
	 * 
	 * @param is Input stream
	 * 
	 * @return String of input stream
	 * 
	 * @throws IOException If exception occured
	 */
	public static String streamToString(InputStream is) throws IOException {
		String str  = "";
		
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;
			
			try {
				BufferedReader reader 	= new BufferedReader(new InputStreamReader(is));
				
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				
				reader.close();
			} finally {
				is.close();
			}
			
			str = sb.toString();
		}
		
		return str;
	}
	
	public static String stream2String(InputStream is) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

	     int i;
	     
	     try {
	    	 i = is.read();
	    	 
	    	 while (i != -1) {
	    		 byteArrayOutputStream.write(i);
	    		 i = is.read();
	    	 }
	    	 
	    	 is.close();
	     } catch (IOException e) {
	    	 throw e;
	     }
	     
	     return byteArrayOutputStream.toString("ISO8859_1");
	}
	
	/**
	 * Get file name from a url. For example, /path/to/me.xml will return me.xml
	 * 
	 * @param url Url
	 * 
	 * @return File name
	 */
	public static String getFileName(String url) {
		String file = "";
		
		if (!url.equals("")) {
			int idx = url.lastIndexOf("/");
			file    = url;
			
			if (idx != -1) {
				file = url.substring(idx+1, url.length());
			}
		}
		
		return file;
	}
	
	/**
	 * Get file extension.
	 * 
	 * @param
	 * 
	 * @return File extension
	 */
	public static String getFileExtension(String file) {
		String ext = "";
		
		int dot = file.lastIndexOf(".");
		ext		= file.substring(dot+1, file.length());
		
		return ext;
	}
	
	public static final String implode(String delimiter, String[] arr) {
		if (arr.length == 0) {
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
			
			if (i != arr.length-1) {
				sb.append(delimiter);
			}
		}
		
		return sb.toString();
	}
	
	public static final String formatHarga(int value) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
		
		String res 	= formatter.format(value);
		
		res			= res.replace(".00", "");
		res			= res.replace(",", ".");
		
		return res.replace("$", "Rp. ");
	}
	
	public static final String formatHarga(double value) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
		
		String res 	= formatter.format(value);
		
		res			= res.replace(".00", "");
		res			= res.replace(",", ".");
		
		return res.replace("$", "Rp. ");
	}
	
	public static final String formatHargaVoucher(int value) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
		
		String res 	= formatter.format(value);
		
		res			= res.replace(".00", "");
		res			= res.replace(",", ".");
		
		return res.replace("$", "");
	}
	
	public static final String formatCurrency(int value) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
		
		String res 	= formatter.format(value);
		
		res			= res.replace(",", "#");
		res			= res.replace(".", ",");
		res			= res.replace("#", ".");
		
		return (value < 0) ? res.replace("$", "Rp. -") : res.replace("$", "Rp. ");
	}
	
	public static final String formatCurrency(double value) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
		
		String res 	= formatter.format(value);
		
		res			= res.replace(",", "#");
		res			= res.replace(".", ",");
		res			= res.replace("#", ".");
		
		return (value < 0) ? res.replace("$", "Rp. -") : res.replace("$", "Rp. ");
	}
	
	public static final String formatKwh(double value) {
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		
		String res 	= formatter.format(value);
		
		res			= res.replace(",", "#");
		res			= res.replace(".", ",");
		res			= res.replace("#", ".");
		
		return res;
	}
	
	public static String listToString(ArrayList<String> list, String separator) {
		if (list == null) {
			return "";
		}
		
		int size 		= list.size();
		StringBuffer sb	= new StringBuffer();
		
		for (int i = 0; i < size; i++) {
			sb.append(list.get(i));
			
			if (i != size-1) {
				sb.append(separator);
			}
		}
		
		String str = sb.toString();
		
		return (str == null) ? "" : str;
	}
	
	public static ArrayList<String> stringToList(String str, String separator) {
		if (str.equals("")) {
			return null;
		}
		
		ArrayList<String> list = new ArrayList<String>();
		
		if (str.contains(",")) {
			String[] tmp = str.split(separator);
			
			for (int i = 0; i < tmp.length; i++) {
				list.add(tmp[i].trim());
			}
		} else {
			list.add(str);
		}
		
		return list;
	}
}
