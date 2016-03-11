package org.esn.mobilit.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.CacheService;
import org.esn.mobilit.utils.ApplicationConstants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutFragment extends android.support.v4.app.Fragment {

    @Bind(R.id.section_logo) ImageView logo;
    @Bind(R.id.section_name) TextView name;
    @Bind(R.id.section_email) TextView email;
    @Bind(R.id.section_phone) TextView phone;
    @Bind(R.id.section_website) TextView website;
    @Bind(R.id.section_address) TextView address;

    Section section;

    @OnClick(R.id.section_address)
    public void clickAddress(View v)
    {
        String map = "http://maps.google.co.in/maps?q=" + section.getAddress();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(intent);
    }

    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ){
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // Load Butterknife
        ButterKnife.bind(this, view);

        section = (Section) CacheService.getObjectFromCache(ApplicationConstants.CACHE_SECTION);

        Glide.with(MobilITApplication.getContext())
                .load(section.getLogo_url())
                .placeholder(R.drawable.default_list_item)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(logo);

        name.setText(section.getName());
        email.setText(section.getEmail());
        phone.setText(section.getPhone());
        website.setText(section.getWebsite());
        address.setText(section.getAddress());

        return view;
    }
}
