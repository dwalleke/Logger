package nl.orsit.menu;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

import nl.orsit.base.BackendServiceCall;
import nl.orsit.base.PhpParams;
import nl.orsit.base.PhpResult;
import nl.orsit.base.ServiceCallback;
import nl.orsit.base.SpinnerActivity;
import nl.orsit.menu.klanten.KlantenFragment;
import nl.orsit.menu.logs.LogsFragment;
import nl.orsit.menu.objecten.ObjectenFragment;

public class MenuActivity extends SpinnerActivity implements ServiceCallback {

    private BackendServiceCall mMenuTask;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        reloadUserData();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton scan = (FloatingActionButton) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
    public View getProgressView() {
        return findViewById(R.id.menu_progress);
    }

    @Override
    public View getParentView() {
        return findViewById(R.id.appbar);
    }

    private void reloadUserData() {
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        PhpParams params = new PhpParams();
        params.add("bid", prefs.getString("bid", ""));
        params.add("mid", prefs.getString("mid", ""));
        params.add("kid", prefs.getString("kid", ""));
        params.add("obj", prefs.getString("obj", ""));
        this.mMenuTask = new BackendServiceCall(this, "javaData", "default", params);
        this.mMenuTask.execute();
    }

    @Override
    public void cancel(PhpResult phpResult) {
        this.mMenuTask = null;
        showProgress(false);
    }

    @Override
    public void finish(PhpResult phpResult) {
        this.mMenuTask = null;
        showProgress(false);
        TextView bedrijf = (TextView) findViewById(R.id.menuBedrijf);
        bedrijf.setText(phpResult.getResults().get("bedrijf"));
        TextView medewerker = (TextView) findViewById(R.id.menuMedewerker);
        medewerker.setText(phpResult.getResults().get("medewerker"));
        TextView klant = (TextView) findViewById(R.id.menuKlant);
        klant.setText(phpResult.getResults().get("listItemObject"));
        TextView object = (TextView) findViewById(R.id.menuObject);
        object.setText(phpResult.getResults().get("object"));
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private KlantenFragment klantenFragment;
        private ObjectenFragment objectenFragment;
        private LogsFragment logsFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.klantenFragment = new KlantenFragment();
            this.objectenFragment = new ObjectenFragment();
            this.logsFragment = new LogsFragment();
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: return this.klantenFragment;
                case 1: return this.objectenFragment;
                default: return this.logsFragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

}
