package org.esn.mobilit.renderers;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import org.esn.mobilit.R;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.inject.ForApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HomepageRenderer {

    Context context;

    @Inject
    public HomepageRenderer(@ForApplication Context context) {
        this.context = context;
    }

    public SpannableStringBuilder renderHomepageText(){

        SpannableStringBuilder text = new SpannableStringBuilder();
        text.append(context.getResources().getString(R.string.chooseyour));
        text.append(" ");

        SpannableString countrySpan = new SpannableString(context.getResources().getString(R.string.country));
        countrySpan.setSpan(new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB), 0, countrySpan.length(), 0);
        text.append(countrySpan);
        text.append(" ");
        text.append(context.getResources().getString(R.string.and));
        text.append(" ");

        SpannableString esnSectionSpan = new SpannableString(context.getResources().getString(R.string.esnsection));
        esnSectionSpan.setSpan(new ForegroundColorSpan(ApplicationConstants.ESNBlueRGB), 0, esnSectionSpan.length(), 0);
        text.append(esnSectionSpan);
        text.append('.');

        return text;
    }
}
