package nl.orsit.menu.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import nl.orsit.base.SpinnerFragment;
import nl.orsit.menu.MenuActivity;
import nl.orsit.menu.klanten.KlantenFragment;
import nl.orsit.menu.logs.LogFragment;
import nl.orsit.menu.logs.LogsFragment;
import nl.orsit.menu.objecten.ObjectenFragment;
import nl.orsit.menu.scanner.ScannerFragment;

public class OrsitPagerAdapter extends FragmentStatePagerAdapter {

    private final SpinnerFragment scannerFragment;
    private final SpinnerFragment klantenFragment;
    private final SpinnerFragment objectenFragment;
    private final SpinnerFragment logsFragment;
    private final SpinnerFragment logFragment;
    private final ViewPager surface;
    private TabLayout tabLayout;
    private Activity activity;

    private Fragment position0;
    private Fragment position1;
    private Fragment position2;


    public OrsitPagerAdapter(FragmentManager supportFragmentManager, ViewPager vpPager, MenuActivity menuActivity) {
        super(supportFragmentManager);
        this.surface = vpPager;
        this.activity = menuActivity;
        this.scannerFragment = new ScannerFragment();
        this.klantenFragment = new KlantenFragment();
        this.objectenFragment = new ObjectenFragment();
        this.logsFragment = new LogsFragment();
        this.logFragment = new LogFragment();
        position0 = this.scannerFragment;
        position1 = this.klantenFragment;
        position2 = this.logsFragment;
    }

    private void setFragment(int position, SpinnerFragment fragment, boolean selectTab, boolean resetData, boolean loadDataset) {
        System.out.println("Setting fragment " + fragment.getClass().getName());
        switch (position) {
            case 0:
                position0 = fragment;
                break;
            case 1:
                position1 = fragment;
                break;
            default:
                position2 = fragment;
        }
        if (selectTab) {
            System.out.println("Setting tab position[" + position + "] for " + fragment.getClass().getName());
            if (tabLayout.getSelectedTabPosition() != position) {
                tabLayout.getTabAt(position).select();
            }
        }
        if (loadDataset) {
            System.out.println("Loading data for " + fragment.getClass().getName());
            if (resetData) {
                System.out.println("Reset data for " + fragment.getClass().getName());
                fragment.resetData();
            }
            fragment.loadDataset(activity);
        }
        surface.setCurrentItem(position);
        this.notifyDataSetChanged();
        System.out.println("Fragment ready" + fragment.getClass().getName());

    }

    public void setScannerFragment() { setScannerFragment(true); }
    public void setScannerFragment(boolean selectTab) {
        System.out.println("Setting scannerFragment");
        setFragment(0, scannerFragment, selectTab, false, false);
    }

    public void setKlantenFragment(boolean resetData, boolean loadDataset) { setKlantenFragment(true, resetData, loadDataset); }
    public void setKlantenFragment(boolean selectTab, boolean resetData, boolean loadDataset) {
        System.out.println("Setting klantFragment");
        setFragment(1, klantenFragment, selectTab, resetData, loadDataset);
    }

    public void setObjectenFragment(boolean resetData, boolean loadDataset) { setObjectenFragment(true, resetData, loadDataset); }
    public void setObjectenFragment(boolean selectTab, boolean resetData, boolean loadDataset) {
        System.out.println("Setting objectFragment");
        setFragment(1, objectenFragment, selectTab, resetData, loadDataset);
    }

    public void setLogsFragment(boolean resetData, boolean loadDataset) { setLogsFragment(true, resetData, loadDataset); }
    public void setLogsFragment(boolean selectTab, boolean resetData, boolean loadDataset) {
        System.out.println("Setting logFragment");
        setFragment(2, logsFragment, selectTab, resetData, loadDataset);
    }

    public void setLogFragment(boolean resetData, boolean loadDataset) { setLogsFragment(true, resetData, loadDataset); }
    public void setLogFragment(boolean selectTab, boolean resetData, boolean loadDataset) {
        System.out.println("Setting logFragment");
        position2 = logFragment;
        setFragment(2, logFragment, selectTab, resetData, loadDataset);
    }

    @Override
    public Fragment getItem(int position) {
        System.out.println("Getting item at " + position);
        switch (position) {
            case 0:
                return position0;
            case 1:
                return position1;
            default:
                return position2;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 3;
    }

    /** From the tablayout, if you click on a tab, perform this action. */
    public void setTabPosition(int position) {
        System.out.println("Setting tab position: " + position);
        switch(position) {
            case 0:
                setScannerFragment(false);
                break;
            case 1:
                setKlantenFragment(false, false, false);
                break;
            default:
                setLogsFragment(false, true, true);
        }
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    public TabLayout getTabLayout() { return this.tabLayout; }
}
