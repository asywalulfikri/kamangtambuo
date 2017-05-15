package asywalul.minang.wisatasumbar.widget;

import android.content.Context;
import asywalul.minang.wisatasumbar.R;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class TabItemLayout extends LinearLayout {

	public TabItemLayout(Context context, int icon) {
		super(context);

		View view = inflate(context, R.layout.item_tab_menu, null);

		((ImageView) view.findViewById(R.id.iv_icon)).setImageResource(icon);

		addView(view);
	}

}
