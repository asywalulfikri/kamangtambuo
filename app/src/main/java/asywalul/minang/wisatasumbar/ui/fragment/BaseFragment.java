package asywalul.minang.wisatasumbar.ui.fragment;

import android.database.sqlite.SQLiteDatabase;
import asywalul.minang.wisatasumbar.model.User;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.volley.VolleyError;

import asywalul.minang.wisatasumbar.ui.activity.BaseActivity;

public class BaseFragment extends Fragment {

	private User mUser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	mUser = getUser();
	}

	public SQLiteDatabase getDatabase() {
		return ((BaseActivity) getActivity()).getDatabase();
	}
	


	public void showToast(String text) {
		((BaseActivity) getActivity()).showToast(text);
	}

	
	public void showToast(String text, boolean islong) {
		((BaseActivity) getActivity()).showToast(text, islong);
	}
	
	public void showDialog(String title, String message) {
		((BaseActivity) getActivity()).showDialog(title, message, false);
	}


	

	
/*
	public void showError(String message) {
		((BaseActivity) getActivity()).showError(message);
	}
*/




	
	/*public User getUser() {
		return ((BaseActivity) getActivity()).getUser();
	}*/

	
	public void back() {
		((BaseActivity) getActivity()).back();
	}

	

	
	/*public String getDefaultLanguage() {
		return ((BaseActivity) getActivity()).getDefaultLanguage();
	}*/




	/**
	 * This for create authorization Oauth1
	 */




}
