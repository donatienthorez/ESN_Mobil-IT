package org.esn.mobilit.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<java.lang.String> {
    public SpinnerAdapter(Context context, int resource, List<java.lang.String> objects) {
        super(context, resource, objects);
    }

    @Override
    public boolean isEnabled(int position){
        return position != 0;
    }
}
