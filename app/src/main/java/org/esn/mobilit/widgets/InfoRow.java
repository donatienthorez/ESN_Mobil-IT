package org.esn.mobilit.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.esn.mobilit.R;

public class InfoRow extends RelativeLayout {

    private ImageView icon;
    private TextView info;

    public InfoRow(Context context) {
        super(context);
        init();
    }

    public InfoRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfoRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InfoRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_info_row, this, true);
        icon = (ImageView) findViewById(R.id.info_icon);
        info = (TextView) findViewById(R.id.info);
    }

    public void setIcon(int iconId) {
        icon.setImageResource(iconId);
    }

    public void setInfo(String text) {
        info.setText(text);
    }
}
