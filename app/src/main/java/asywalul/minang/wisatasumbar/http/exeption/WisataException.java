package asywalul.minang.wisatasumbar.http.exeption;

public class WisataException extends Exception {

	private static final long serialVersionUID = 1L;

	String error = "";
	
	public WisataException(String msg) {
		super(msg);
		
		error = msg;
	}
	
	public String getError() {
		return error;
	}
}
