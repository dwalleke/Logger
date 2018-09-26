package nl.orsit.menu;

import nl.orsit.menu.data.LogTypes;

public interface MenuDataInterface {
    void userDataChanged(CHANGED kid);
    void tabKlanten();
    void tabObjecten();
    void tabLogs();

    enum CHANGED { BID, MID, KID, OBJ, LOG }
    enum LOG_TYPE { MultiText, Number, Text, Image, Choice }

    LogTypes getLogTypes();

}
