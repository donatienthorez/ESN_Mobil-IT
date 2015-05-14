package org.esn.mobilit.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.esn.mobilit.R;
import org.esn.mobilit.models.Section;
import org.esn.mobilit.network.JSONfunctions;
import org.esn.mobilit.utils.ApplicationConstants;
import org.esn.mobilit.utils.Utils;
import org.esn.mobilit.utils.image.ImageLoader;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Spider on 13/05/15.
 */
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

        if (section.getLogo_url().equalsIgnoreCase(""))
            new DownloadSectionLogo().execute();
        else
            imageLoader.DisplayImage(ApplicationConstants.LOGOINSERTER_URL + "assets/img/logos_large/" + section.getLogo_url(), logo);

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

    private class DownloadSectionLogo extends AsyncTask<Void, Void, Boolean> {

        protected Boolean doInBackground(Void... params) {
            String url = ApplicationConstants.LOGOINSERTER_URL + "rest/getPath.php?code_section=" + section.getCode_section();
            JSONObject jsonobject = JSONfunctions.getJSONfromURL(url);

            try {
                JSONArray jsonArray = jsonobject.getJSONArray("section");
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                Log.d("About :", jsonObject.optString("logopath"));
                section.setLogo_url(jsonObject.optString("logopath"));

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                Utils.saveObjectToCache(getActivity(), "section", section);
                imageLoader.DisplayImage(ApplicationConstants.LOGOINSERTER_URL + "assets/img/logos_large/" + section.getLogo_url(), logo);
            }else{
                logo.setImageResource(R.drawable.logo_small);
            }
        }
    }
}
