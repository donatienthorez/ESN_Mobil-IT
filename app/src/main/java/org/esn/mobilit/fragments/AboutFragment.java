package org.esn.mobilit.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.esn.mobilit.MobilITApplication;
import org.esn.mobilit.R;
import org.esn.mobilit.models.Abouts;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.services.AboutService;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.callbacks.NetworkCallback;
import org.esn.mobilit.utils.image.ImageLoader;

import retrofit.RetrofitError;

public class AboutFragment extends android.support.v4.app.Fragment {

    private Section section;
    private ImageLoader imageLoader;
    private ImageView logo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        section = (Section) Utils.getObjectFromCache(getActivity().getBaseContext(), "section");
        imageLoader = new ImageLoader(getActivity().getApplicationContext());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);

        logo = (ImageView) view.findViewById(R.id.section_logo);
        final String url = ApplicationConstants.LOGOINSERTER_URL + "assets/img/logos/" + section.getLogo_url();

        if (section.getLogo_url() == null || section.getLogo_url().equalsIgnoreCase("")) {
            AboutService.getAbout(new NetworkCallback<Abouts>() {
                @Override
                public void onSuccess(Abouts result) {
                    section.setLogo_url(result.getAbout().getLogoPath());
                    Utils.saveObjectToCache(getActivity(), "section", section);
                    Glide.with(MobilITApplication.getContext())
                         .load(url)
                         .placeholder(R.drawable.logo_small)
                         .diskCacheStrategy(DiskCacheStrategy.RESULT)
                         .into(logo);
                }

                @Override
                public void onFailure(RetrofitError error) {
                    logo.setImageResource(R.drawable.logo_small);
                }
            });
        } else {
            Glide.with(MobilITApplication.getContext())
                    .load(url)
                    .placeholder(R.drawable.logo_small)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(logo);
        }

        TextView name  = (TextView)  view.findViewById(R.id.section_name);
        name.setText(section.getName());
        TextView email = (TextView)  view.findViewById(R.id.section_email);
        email.setText(section.getEmail());
        TextView phone = (TextView)  view.findViewById(R.id.section_phone);
        phone.setText(section.getPhone());
        TextView website = (TextView)  view.findViewById(R.id.section_website);
        website.setText(section.getWebsite());
        TextView address = (TextView)  view.findViewById(R.id.section_address);
        address.setText(section.getAddress());
        address.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String map = "http://maps.google.co.in/maps?q=" + section.getAddress();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(intent);
            }
        });

        return view;
    }
}
