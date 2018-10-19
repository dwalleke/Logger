package nl.orsit.menu.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import nl.orsit.menu.klanten.KlantAdapter;
import nl.orsit.menu.klanten.KlantenFragment;
import nl.orsit.menu.logs.LogsFragment;
import nl.orsit.menu.objecten.ObjectenFragment;
import nl.orsit.menu.scanner.ScannerFragment;

public class OrsitPagerAdapter extends FragmentPagerAdapter {

    private final ScannerFragment scannerFragment;
    private final KlantenFragment klantenFragment;
    private final ObjectenFragment objectenFragment;
    private final LogsFragment logsFragment;
    private final ViewPager surface;

    private Fragment position0;
    private Fragment position1;
    private Fragment position2;

    public OrsitPagerAdapter(FragmentManager supportFragmentManager, ViewPager vpPager) {
        super(supportFragmentManager);
        this.surface = vpPager;
        this.scannerFragment = new ScannerFragment();
        this.klantenFragment = new KlantenFragment();
        this.objectenFragment = new ObjectenFragment();
        this.logsFragment = new LogsFragment();
        position0 = this.scannerFragment;
        position1 = this.klantenFragment;
        position2 = this.logsFragment;
    }

    public void setScanner() {
        position0 = scannerFragment;
        surface.setCurrentItem(0);
    }

    public void setKlantenFragment() {
        position1 = klantenFragment;
        surface.setCurrentItem(1);
    }

    public void setObjectenFragment() {
        position1 = objectenFragment;
        surface.setCurrentItem(1);
    }

    public void setLogsFragment() {
        position2 = logsFragment;
        surface.setCurrentItem(2);

    }

    @Override
    public Fragment getItem(int position) {
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
    public int getCount() {
        return 3;
    }

    public void setTabPosition(int position) {
        this.surface.setCurrentItem(position);
    }
}
