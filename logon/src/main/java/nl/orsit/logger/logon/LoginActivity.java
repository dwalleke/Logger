package nl.orsit.logger.logon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import nl.orsit.base.BackendServiceCall;
import nl.orsit.base.PhpParams;
import nl.orsit.base.PhpResult;
import nl.orsit.base.ServiceCallback;
import nl.orsit.base.SpinnerActivity;
import nl.orsit.menu.MenuActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends SpinnerActivity implements ServiceCallback {

    /** Keep track of the login task to ensure we can cancel it if requested. */
    private BackendServiceCall mAuthTask = null;

    // UI references.
    private TextInputEditText mEmailView;
    private TextInputEditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (TextInputEditText) findViewById(R.id.email);
        mPasswordView = (TextInputEditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    @Override
    public View getProgressView() {
        return findViewById(R.id.login_progress);
    }

    @Override
    public View getParentView() {
        return findViewById(R.id.login_form);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (email.equals("a")) {
            email = "a.reudink@gmail.com";
        } else {
            email = "mibosman75@gmail.com";
        }
        password = "techniek";

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            PhpParams params = new PhpParams().add("email", email).add("password", password);
            this.mAuthTask = new BackendServiceCall(this, "javaLogin", "email", params);
            this.mAuthTask.execute();
        }
    }


    @Override
    public void cancel(PhpResult phpResult) {
        this.mAuthTask = null;
        showProgress(false);
    }

    @Override
    public void finish(PhpResult phpResult) {
        this.mAuthTask = null;
        showProgress(false);
        if (phpResult.isOk()) {
            SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
            editor.clear();
            editor.putString("bid", phpResult.getResults().get("bid"));
            editor.putString("mid", phpResult.getResults().get("mid"));
            editor.putString("lev", phpResult.getResults().get("lev"));
            editor.apply();
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        } else {
            if (phpResult.getErrors().containsKey("password")) {
                String value = phpResult.getErrors().get("password");
                switch (value) {
                    case "error_field_required":
                        mPasswordView.setError(getString(R.string.error_field_required));
                        break;
                    case "error_invalid_password":
                        mPasswordView.setError(getString(R.string.error_invalid_password));
                        break;
                    default:
                        mPasswordView.setError(getString(R.string.error_unknown));
                }
                mPasswordView.requestFocus();
            }
            if (phpResult.getErrors().containsKey("email")) {
                String value = phpResult.getErrors().get("email");
                switch (value) {
                    case "error_field_required":
                        mEmailView.setError(getString(R.string.error_field_required));
                        break;
                    case "error_invalid_email":
                        mEmailView.setError(getString(R.string.error_invalid_email));
                        break;
                    default:
                        mEmailView.setError(getString(R.string.error_unknown));
                }
                mEmailView.requestFocus();
            }
        }

    }

}

