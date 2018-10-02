package nl.orsit.menu.data;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.orsit.base.BackendServiceCall;
import nl.orsit.base.PhpParams;
import nl.orsit.base.PhpResult;
import nl.orsit.base.ServiceCallback;
import nl.orsit.menu.MenuDataInterface;
import nl.orsit.menu.logs.UserConfig;

public class LogTypes implements ServiceCallback {

    private BackendServiceCall task;
    private Map<String, MasterInvoer> invoerMap = new HashMap<>();
    private Map<String, MasterStatus> statusMap = new HashMap<>();
    private Map<String, MasterType> typeMap = new HashMap<>();
    private Map<String, Map<String, MasterInvoer>> userInvoer = new HashMap<>(); // invoer by status id, by invoer id
    private Map<String, Map<String, MasterStatus>> userStatus = new HashMap<>(); // status by type id, by status id
    private Map<String, MasterType> userType = new HashMap<>(); // type ids

    public LogTypes(String bid) {
        PhpParams params = new PhpParams().add("bid", bid);
        this.task = new BackendServiceCall(this, "javaGetMasterTables", "default", params);
        this.task.execute();
    }


    @Override
    public void cancel(PhpResult phpResult) {
        this.task = null;
    }

    @Override
    public void finish(PhpResult phpResult) {
        try {
            JSONArray types = new JSONArray(phpResult.getResults().get("type"));
            for (int i = 0; i < types.length(); i++) {
                MasterType mt = new MasterType(types.getJSONObject(i));
                typeMap.put(mt.getId(), mt);
            }
            JSONArray statusses = new JSONArray(phpResult.getResults().get("status"));
            for (int i = 0; i < statusses.length(); i++) {
                MasterStatus ms = new MasterStatus(statusses.getJSONObject(i));
                statusMap.put(ms.getId(), ms);
            }
            JSONArray invoeren = new JSONArray(phpResult.getResults().get("invoer"));
            for (int i = 0; i < invoeren.length(); i++) {
                MasterInvoer mi = new MasterInvoer(invoeren.getJSONObject(i));
                invoerMap.put(mi.getId(), mi);
            }

            JSONArray userconfig = new JSONArray(phpResult.getResults().get("user"));
            for (int i = 0; i < userconfig.length(); i++) {
                UserConfig uc = new UserConfig(userconfig.getJSONObject(i));

                // adding user invoer for this user.
                Map<String, MasterInvoer> mi = new HashMap<>();
                if (userInvoer.containsKey(uc.getUser_status())) {
                    mi = userInvoer.get(uc.getUser_status());
                }
                mi.put(uc.getUser_invoer(), invoerMap.get(uc.getUser_invoer()));
                userInvoer.put(uc.getUser_status(), mi);

                // adding user status for this user.
                Map<String, MasterStatus> ms = new HashMap<>();
                if (userStatus.containsKey(uc.getUser_type())) {
                    ms = userStatus.get(uc.getUser_type());
                }
                ms.put(uc.getUser_status(), statusMap.get(uc.getUser_status()));
                userStatus.put(uc.getUser_type(), ms);

                userType.put(uc.getUser_type(), typeMap.get(uc.getUser_type()));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.task = null;
    }

    public List<MasterType> getUserTypeList() {
        List<MasterType> out = new ArrayList<>();
        for (MasterType type : this.userType.values()) {
            out.add(type);
        }
        return out;
    }

    public Collection<MasterStatus> getUserStatusList(String type) {
        if (this.userStatus.containsKey(type)) {
            List<MasterStatus> tmp = new ArrayList<>();
            tmp.addAll(this.userStatus.get(type).values());
            Collections.sort(tmp, new Comparator<MasterStatus>() {
                @Override
                public int compare(MasterStatus o1, MasterStatus o2) {
                    return o1.getOmschrijving().compareTo(o2.getOmschrijving());
                }
            });
            return tmp;
        }
        return new ArrayList<>();
    }



    public List<MasterInvoer> getUserInvoerList(String status) {
        if (this.userInvoer.containsKey(status)) {
            List<MasterInvoer> tmp = new ArrayList<>();
            tmp.addAll(this.userInvoer.get(status).values());
            Collections.sort(tmp, new Comparator<MasterInvoer>() {
                @Override
                public int compare(MasterInvoer o1, MasterInvoer o2) {
                    return o1.getOmschrijving().compareTo(o2.getOmschrijving());
                }
            });
            return tmp;
        }
        return new ArrayList<>();
    }

    public MenuDataInterface.LOG_TYPE getLogTypeByCode(String code) {
        switch (code) {
            case "I":
                return MenuDataInterface.LOG_TYPE.Image;
            case "B":
                return MenuDataInterface.LOG_TYPE.MultiText;
            case "N":
                return MenuDataInterface.LOG_TYPE.Number;
            case "R1":
                return MenuDataInterface.LOG_TYPE.Choice;
            default:
                return MenuDataInterface.LOG_TYPE.Text;
        }
    }

    public MenuDataInterface.LOG_TYPE getLogType(String id) {
        if (invoerMap.containsKey(id)) {
            return getLogTypeByCode(invoerMap.get(id).getInvoerType());
        } else {
            return MenuDataInterface.LOG_TYPE.Text;
        }
    }
    public Map<String, MasterInvoer> getInvoerMap() {
        return this.invoerMap;
    }

    public MasterStatus getUserStatus(String type, String status) {
        for (MasterStatus ms : this.getUserStatusList(type)) {
            if (ms.getId().equals(status)) {
                return ms;
            }
        }
        return null;
    }

    public MasterType getUserType(String id) {
        for (MasterType mt : this.getUserTypeList()) {
            if (mt.getId().equals(id)) {
                return mt;
            }
        }
        return null;
    }
}
