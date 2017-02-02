package org.esn.mobilit.fragments;

import android.app.Fragment;
import android.content.Context;
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

import org.esn.mobilit.R;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.AboutService;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.inject.ForApplication;
import org.esn.mobilit.utils.inject.InjectUtil;
import org.esn.mobilit.widgets.InfoCard;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutFragment extends Fragment {

    @Bind(R.id.section_logo) ImageView logo;
    @Bind(R.id.section_name) TextView name;
    @Bind(R.id.first_info_card) InfoCard firstInfoCard;
    @Bind(R.id.second_info_card) InfoCard secondInfoCard;

    @Inject
    AboutService aboutService;

    @ForApplication
    @Inject
    Context context;

    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ){
        View view = inflater.inflate(R.layout.fragment_about_re, container, false);

        InjectUtil.component().inject(this);

        ButterKnife.bind(this, view);

        setSection(aboutService.getFromCache());

        aboutService.getFromSite(new NetworkCallback<Section>() {
            @Override
            public void onNoConnection(Section section) {
            }

            @Override
            public void onSuccess(Section result) {
                setSection(result);
            }

            @Override
            public void onNoAvailableData() {
                //TODO manage onNoAvailableData error.
            }

            @Override
            public void onFailure(String error) {
                //TODO manager onFailure error.
            }
        });

        return view;
    }

    public void setSection(final Section section){
        Glide.with(context)
                .load(section.getLogo_url())
                .placeholder(R.drawable.logo_small_2_25)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(logo);

        name.setText(section.getName());
        firstInfoCard.addInfo(R.drawable.ic_email_black_24dp, section.getEmail(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", section.getEmail(), null)));
            }
        });

        firstInfoCard.addInfo(R.drawable.ic_local_phone_black_24dp, section.getPhone(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", section.getPhone(), null)));
            }
        });

        secondInfoCard.addInfo(R.drawable.ic_public_black_24dp, section.getWebsite(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(section.getWebsite())));
            }
        });

        secondInfoCard.addInfo(R.drawable.ic_directions_black_24dp, section.getAddress(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.co.in/maps?q=" + section.getAddress())));
            }
        });
    }
}
