package nl.orsit.menu;

public interface MenuDataInterface {
    void userDataChanged(CHANGED kid);
    void tabKlanten();
    void tabObjecten();
    void tabLogs();

    enum CHANGED { BID, MID, KID, OBJ, LOG }

}
