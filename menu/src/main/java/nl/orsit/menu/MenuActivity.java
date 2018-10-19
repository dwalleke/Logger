package nl.orsit.menu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import nl.orsit.base.CameraPermission;
import nl.orsit.menu.data.LogTypes;
import nl.orsit.menu.data.MenuDataFragment;
import nl.orsit.menu.util.MenuInfoReloader;
import nl.orsit.menu.util.OrsitPagerAdapter;

public class MenuActivity extends AppCompatActivity implements MenuDataInterface {

    private OrsitPagerAdapter adapterViewPager;
    private LogTypes logTypes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // we controleren nu eerst de camera permissies, zodat we dit niet straks hoeven uit te voeren.
        CameraPermission.getInstance().checkCameraPermission(this);
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        // get the logtype information for the current company
        logTypes = new LogTypes(prefs.getString("bid", ""));
        LEVEL level = determineLevel(prefs);

        setContentView(R.layout.activity_menu);
        // maak het informatie menu
        createInfoMenu(savedInstanceState, level);
        // maak het actie menu
        createTabMenu();
        // altijd eerst de scanner tonen.
        getTabAdapter().setScanner();
    }

    private LEVEL determineLevel(SharedPreferences prefs) {
        String lev = prefs.getString("lev", "normaal");
        if ("beheerder".equals(lev)) return LEVEL.BEHEERDER;
        if ("manager".equals(lev)) return LEVEL.MANAGER;
        if ("admin".equals(lev)) return LEVEL.ADMIN;
        return LEVEL.NORMAAL;
    }

    @Override
    public OrsitPagerAdapter getTabAdapter() { return adapterViewPager; }

    @Override
    public LogTypes getLogTypes() { return this.logTypes; }

    private void createInfoMenu(Bundle savedInstanceState, LEVEL level) {
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MenuDataFragment fragment = new MenuDataFragment();
            MenuInfoReloader.createInstance(fragment, this, level);
            MenuInfoReloader.reload();
            transaction.replace(R.id.menu_content_fragment, fragment);
            transaction.commit();
        }
    }

    private void createTabMenu() {
        // create the four tabs
        final ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new OrsitPagerAdapter(getSupportFragmentManager(), vpPager);
        vpPager.setAdapter(adapterViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Scan"));
        tabLayout.addTab(tabLayout.newTab().setText("Zoek"));
        tabLayout.addTab(tabLayout.newTab().setText("Logs"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapterViewPager.setTabPosition(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                adapterViewPager.setTabPosition(tab.getPosition());
            }

        });
    }

}
