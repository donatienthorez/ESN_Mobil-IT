package org.esn.mobilit.fragments;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import org.esn.mobilit.R;
import org.esn.mobilit.utils.parser.RSSFeed;

public class DetailActivity extends FragmentActivity {

    RSSFeed feed;
    int pos;
    private ViewPager pager;
    private static final long serialVersionUID = 1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        /** Getting the fragment manager for fragment related operations */
        android.app.FragmentManager fragmentManager = getFragmentManager();

        /** Getting the fragmenttransaction object, which can be used to add, remove or replace a fragment */
        FragmentTransaction fragmentTransacton = fragmentManager.beginTransaction();

        /** Instantiating the fragment CountryDetailsFragment */
        DetailsFragment detailsFragment = new DetailsFragment();

        /** Creating a bundle object to pass the data(the clicked item's position) from the activity to the fragment */
        Bundle b = new Bundle();

        /** Setting the data to the bundle object from the Intent*/
        b.putInt("pos", getIntent().getIntExtra("pos", 0));
        b.putSerializable("feed", getIntent().getSerializableExtra("feed"));

        /** Setting the bundle object to the fragment */
        detailsFragment.setArguments(b);

        /** Adding the fragment to the fragment transaction */
        fragmentTransacton.add(R.id.details_fragment_container, detailsFragment);


        /** Making this transaction in effect */
        fragmentTransacton.commit();
    }
}