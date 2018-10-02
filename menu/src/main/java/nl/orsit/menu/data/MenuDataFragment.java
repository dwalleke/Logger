package nl.orsit.menu.data;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.orsit.base.BackendServiceCall;
import nl.orsit.base.PhpParams;
import nl.orsit.base.PhpResult;
import nl.orsit.base.ServiceCallback;
import nl.orsit.base.SpinnerFragment;
import nl.orsit.base.TabFragment;
import nl.orsit.menu.MenuDataInterface;
import nl.orsit.menu.R;

public class MenuDataFragment extends SpinnerFragment implements ServiceCallback {

    private BackendServiceCall mTask;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDataset(MenuDataInterface.CHANGED.BID);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_data, container, false);
        return rootView;
    }

    @Override
    public View getProgressView() {
        return getActivity().findViewById(R.id.menu_progress);
    }

    @Override
    public View getParentView() {
        return getActivity().findViewById(R.id.appbar);
    }

    public void loadDataset(MenuDataInterface.CHANGED arg) {
        if (getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("UserData", getActivity().MODE_PRIVATE);
            PhpParams params = new PhpParams();
            params.add("bid", prefs.getString("bid", ""));
            params.add("mid", prefs.getString("mid", ""));
            params.add("kid", prefs.getString("kid", ""));
            params.add("obj", prefs.getString("obj", ""));
            switch (arg) {
                case OBJ:
                    params.add("askobj", "yes");
                    break;
                case KID:
                    params.add("askkid", "yes");
                    params.add("askobj", "yes");
                    break;
                case MID:
                    params.add("askmid", "yes");
                    params.add("askkid", "yes");
                    params.add("askobj", "yes");
                case BID:
                    params.add("askbid", "yes");
                    params.add("askmid", "yes");
                    params.add("askkid", "yes");
                    params.add("askobj", "yes");
                    break;
            }
            this.mTask = new BackendServiceCall(this, "javaData", "default", params);
            this.mTask.execute();
        }
    }


    @Override
    public void cancel(PhpResult phpResult) {
        this.mTask = null;
        showProgress(false);
    }

    private void checkField(PhpResult phpResult, String key, int textView) {
        if (phpResult.getResults().containsKey(key)) {
            TextView bedrijf = (TextView) getActivity().findViewById(textView);
            bedrijf.setText(phpResult.getResults().get(key));
        }
    }

    @Override
    public void finish(PhpResult phpResult) {
        this.mTask = null;
        showProgress(false);
        checkField(phpResult, "bedrijf", R.id.menuBedrijf);
        checkField(phpResult, "medewerker", R.id.menuMedewerker);
        checkField(phpResult, "klant", R.id.menuKlant);
        checkField(phpResult, "object", R.id.menuObject);
    }


}
