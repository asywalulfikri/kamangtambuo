package asywalul.minang.wisatasumbar.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import asywalul.minang.wisatasumbar.R;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by vellaafiolisa on 14/03/16.
 */
public class SimpleDividerItem extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public SimpleDividerItem(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.line_divider_trans);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}