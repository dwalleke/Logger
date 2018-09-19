package nl.orsit.logger.logon.task;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


import nl.orsit.logger.logon.LoginActivity;
import nl.orsit.logger.logon.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private final String mPassword;
    private final LoginActivity mParent;

    public UserLoginTask(LoginActivity parent, String email, String password) {
        mEmail = email;
        mPassword = password;
        mParent = parent;
    }

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            JSONObject json = new JSONObject();
            json.put("email", mEmail);
            json.put("password", mPassword);
            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request = new Request.Builder()
                    .url("http://www.dwaal.dds.nl/itrs/app/pages/javaLogin.php")
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        mParent.clearUserLoginTask();

        if (success) {
            mParent.finish();
        } else {
            mParent.error();
        }
    }

    @Override
    protected void onCancelled() {
        mParent.clearUserLoginTask();
    }


}
