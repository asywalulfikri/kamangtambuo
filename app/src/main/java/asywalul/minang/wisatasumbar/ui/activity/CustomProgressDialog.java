package asywalul.minang.wisatasumbar.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import asywalul.minang.wisatasumbar.R;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CustomProgressDialog {

	private AlertDialog dialog;

	@SuppressLint("InflateParams")
	public CustomProgressDialog(Context context, String message) {
		AlertDialog.Builder builder;

		LayoutInflater inflater = LayoutInflater.from(context);

		View layout = inflater.inflate(R.layout.dialog_progress, null);
		TextView messageTv = (TextView) layout.findViewById(R.id.tv_message);

		builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setView(layout);

		dialog = builder.create();
	}

	public void show() {
		dialog.setCancelable(false);
		dialog.show();
	}

	public void dismiss() {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

}
