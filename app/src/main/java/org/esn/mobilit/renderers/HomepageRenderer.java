package org.esn.mobilit.renderers;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.utils.ApplicationConstants;

public class HomepageRenderer {

    public SpannableStringBuilder renderHomepageText(){

        SpannableStringBuilder text = new SpannableStringBuilder();
        text.append(MobilITApplication.getContext().getResources().getString(R.string.chooseyour));
        text.append(" ");

        SpannableString countrySpan = new SpannableString(MobilITApplication.getContext().getResources().getString(R.string.country));
        countrySpan.setSpan(new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB), 0, countrySpan.length(), 0);
        text.append(countrySpan);
        text.append(" ");
        text.append(MobilITApplication.getContext().getResources().getString(R.string.and));
        text.append(" ");

        SpannableString esnSectionSpan = new SpannableString(MobilITApplication.getContext().getResources().getString(R.string.esnsection));
        esnSectionSpan.setSpan(new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB), 0, esnSectionSpan.length(), 0);
        text.append(esnSectionSpan);
        text.append('.');

        return text;
    }
}
