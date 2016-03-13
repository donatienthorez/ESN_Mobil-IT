package org.esn.mobilit.fragments;

import android.app.Fragment;
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
import org.esn.mobilit.services.AboutService;
import org.esn.mobilit.services.gcm.GCMService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutFragment extends Fragment {

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

        setSection(AboutService.getInstance().getFromCache());

        AboutService.getInstance().getFromSite(new NetworkCallback<Section>() {
            @Override
            public void onSuccess(Section result) {
                setSection(result);
            }

            @Override
            public void onNoAvailableData() {

            }

            @Override
            public void onFailure(String error) {

            }
        });

        return view;
    }

    public void setSection(Section section){
        this.section = section;

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

    }
}
