package asywalul.minang.wisatasumbar.widget;

import android.content.Context;
import asywalul.minang.wisatasumbar.R;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TabItemLayoutText extends LinearLayout {

    public TabItemLayoutText(Context context, int text) {
        super(context);

        View view = inflate(context, R.layout.item_tab_menu_text, null);

        ((TextView) view.findViewById(R.id.tv_title_resep)).setText(text);

        addView(view);
    }

}
