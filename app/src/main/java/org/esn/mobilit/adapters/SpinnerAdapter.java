package org.esn.mobilit.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<String> {

    public SpinnerAdapter(Context context, ArrayList<String> datas) {
        super(context, android.R.layout.simple_list_item_1, datas);
    }

    public View getView(int position, View convertView,ViewGroup parent) {

        View v = super.getView(position, convertView, parent);
        ((TextView) v).setTextSize(16);

        return v;
    }

    public View getDropDownView(int position, View convertView,ViewGroup parent) {

        View v = super.getDropDownView(position, convertView,parent);
        return v;
    }
}
