package nl.orsit.menu;

import nl.orsit.menu.data.LogTypes;
import nl.orsit.menu.util.OrsitPagerAdapter;

public interface MenuDataInterface {

    OrsitPagerAdapter getTabAdapter();

    enum LOG_TYPE { MultiText, Number, Text, Image, Choice }
    enum LEVEL { NORMAAL, MANAGER, BEHEERDER, ADMIN }

    LogTypes getLogTypes();

}

