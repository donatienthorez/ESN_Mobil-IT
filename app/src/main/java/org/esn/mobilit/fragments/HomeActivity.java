package org.esn.mobilit.fragments;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import org.esn.mobilit.R;
import org.esn.mobilit.utils.MyFragmentPagerAdapter;

public class HomeActivity extends FragmentActivity implements ActionBar.TabListener {

    ViewPager               myPager;
    MyFragmentPagerAdapter  myAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),4);


        final ActionBar actionBar = getActionBar();

        actionBar.setHomeButtonEnabled(false);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        myPager = (ViewPager) findViewById(R.id.pager);
        myPager.setAdapter(myAdapter);
        myPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                actionBar.setSelectedNavigationItem(position);
            }
        });

        actionBar.addTab(actionBar.newTab().setText("Events").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("News").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Partners").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Guide").setTabListener(this));
    }

    @Override
    public void onTabSelected(Tab tab, android.app.FragmentTransaction ft){
        myPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(Tab tab, android.app.FragmentTransaction ft){
        // TODO Auto-generated method stub
    }

    @Override
    public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft){
        // TODO Auto-generated method stub
    }
}
