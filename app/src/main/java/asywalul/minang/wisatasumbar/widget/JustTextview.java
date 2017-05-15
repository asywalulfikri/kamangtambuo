package asywalul.minang.wisatasumbar.widget;

/**
 * Created by ferissadjohan on 5/28/16.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import asywalul.minang.wisatasumbar.R;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;

public class JustTextview extends WebView {
    public JustTextview(final Context context) {
        this(context, null, 0);
    }

    public JustTextview(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JustTextview(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        if (attrs != null) {
            final TypedValue tv = new TypedValue();
            final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.JustifiedTextView, defStyle, 0);
            if (ta != null) {
                ta.getValue(R.styleable.JustifiedTextView_text, tv);

                if (tv.resourceId > 0) {
                    final String text = context.getString(tv.resourceId).replace("\n", "<br />");
                    loadDataWithBaseURL("file:///android_asset/",
                            "<html><head>" +
                                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"justified_textview.css\" />" +
                                    "</head><body>" + text + "</body></html>",

                            "text/html", "UTF8", null);
                    setTransparentBackground();
                }
            }
        }
    }

    public void setTransparentBackground() {
        try {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } catch (final NoSuchMethodError e) {
        }

        setBackgroundColor(Color.TRANSPARENT);
        setBackgroundDrawable(null);
        setBackgroundResource(0);
    }
}