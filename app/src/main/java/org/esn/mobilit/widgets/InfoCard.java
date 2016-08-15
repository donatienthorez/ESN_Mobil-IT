package org.esn.mobilit.widgets;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import org.esn.mobilit.R;

import java.util.ArrayList;
import java.util.List;

public class InfoCard extends LinearLayout {

    private LinearLayout infoRowsContainer;
    private List<String> infoList;

    public InfoCard(Context context) {
        super(context);
        init();
    }

    public InfoCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfoCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_info_card, this, true);
        infoRowsContainer = (LinearLayout) findViewById(R.id.info_rows_container);
        setVisibility(GONE);
        infoList = new ArrayList<>();
    }

    public void addInfo(int iconId, String info, OnClickListener onClickListener) {
        if (!TextUtils.isEmpty(info) && !infoList.contains(info)) {
            setVisibility(VISIBLE);
            InfoRow infoRow = new InfoRow(getContext());
            infoRow.setIcon(iconId);
            infoRow.setInfo(info);
            infoRow.setOnClickListener(onClickListener);
            infoRowsContainer.addView(infoRow);
            infoList.add(info);
        }
    }
}
