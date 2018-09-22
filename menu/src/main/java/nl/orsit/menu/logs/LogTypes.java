package nl.orsit.menu.logs;

import nl.orsit.base.BackendServiceCall;
import nl.orsit.base.PhpParams;
import nl.orsit.base.PhpResult;
import nl.orsit.base.ServiceCallback;

public class LogTypes implements ServiceCallback {

    private BackendServiceCall task;


    public void LogTypes() {
        this.task = new BackendServiceCall(this, "javaGetMasterTables", "default", new PhpParams());
        this.task.execute();
    }


    @Override
    public void cancel(PhpResult phpResult) {
        this.task = null;
    }

    @Override
    public void finish(PhpResult phpResult) {
        this.task = null;
    }
}
