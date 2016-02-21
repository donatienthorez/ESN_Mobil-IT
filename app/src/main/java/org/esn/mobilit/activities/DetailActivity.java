package org.esn.mobilit.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.esn.mobilit.R;
import org.esn.mobilit.fragments.Satellite.DetailsFragment;

public class DetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setIcon(R.drawable.ic_launcher);
        setContentView(R.layout.detail);

        Bundle bundle = new Bundle();
        bundle.putSerializable("feed", getIntent().getSerializableExtra("feed"));

        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.details_fragment_container, detailsFragment);
        fragmentTransaction.commit();
    }
}
