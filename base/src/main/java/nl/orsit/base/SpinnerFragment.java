package nl.orsit.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import nl.orsit.logger.R;

abstract public class SpinnerFragment extends Fragment {

    public abstract View getProgressView();
    public abstract View getParentView();
    public abstract void loadDataset(Activity activity);
    public abstract void resetData();

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            getParentView().setVisibility(show ? View.GONE : View.VISIBLE);
            getParentView().animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getParentView().setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            getProgressView().setVisibility(show ? View.VISIBLE : View.GONE);
            getProgressView().animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getProgressView().setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            getProgressView().setVisibility(show ? View.VISIBLE : View.GONE);
            getParentView().setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



}
