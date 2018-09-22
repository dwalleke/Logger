package nl.orsit.menu;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.TextView;

import nl.orsit.base.BackendServiceCall;
import nl.orsit.base.PhpParams;
import nl.orsit.base.PhpResult;
import nl.orsit.base.ServiceCallback;
import nl.orsit.base.SpinnerActivity;
import nl.orsit.menu.data.MenuDataFragment;
import nl.orsit.menu.klanten.KlantenFragment;
import nl.orsit.menu.logs.LogsFragment;
import nl.orsit.menu.objecten.ObjectenFragment;

public class MenuActivity extends AppCompatActivity implements MenuDataInterface {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private MenuDataFragment menuDataFragment;
    private KlantenFragment klantenFragment;
    private ObjectenFragment objectenFragment;
    private LogsFragment logsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Make our menu data fragment
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            menuDataFragment = new MenuDataFragment();
            menuDataFragment.loadDataset(CHANGED.BID);
            transaction.replace(R.id.menu_content_fragment, menuDataFragment);
            transaction.commit();
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        this.klantenFragment = new KlantenFragment();
        this.objectenFragment = new ObjectenFragment();
        this.logsFragment = new LogsFragment();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // create the three tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // the scan button
        FloatingActionButton scan = (FloatingActionButton) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // the add button (does something different for each page (klantAdd, objectAdd, logAdd)
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public void userDataChanged(CHANGED arg) {
        switch (arg) {
            case KID:
                objectenFragment.loadDataset();
                logsFragment.loadDataset();
                break;
            case OBJ:
                logsFragment.loadDataset();
        }
        menuDataFragment.loadDataset(arg);
    }

    @Override
    public void tabKlanten() {
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void tabObjecten() {
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void tabLogs() {
        mViewPager.setCurrentItem(2);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: return klantenFragment;
                case 1: return objectenFragment;
                default: return logsFragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

}
