package nl.orsit.base;

import android.os.AsyncTask;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BackendServiceCall extends AsyncTask<Void, Void, Void> {

    private ServiceCallback callback;
    private String phpScript;
    private PhpResult phpResult;
    private String defaultErrorKey;
    private PhpParams params;

    public BackendServiceCall(ServiceCallback callback, String phpScript, String defaultErrorKey, PhpParams params) {
        this.callback = callback;
        this.phpScript = phpScript;
        this.phpResult = null;
        this.defaultErrorKey = defaultErrorKey;
        this.params = params;
    }

    @Override
    protected Void doInBackground(Void... arg) {
        try {
            OkHttpClient client = new OkHttpClient();
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
            System.out.println("firing request: " + phpScript);

            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
            Request request = new Request.Builder()
                    .url("http://www.dwaal.dds.nl/acpt/app/pages/"+phpScript+".php")
                    .post(formBodyBuilder.build())
                    .build();
            Response response = client.newCall(request).execute();
            this.phpResult = new PhpResult(response.body().string());
        } catch (IOException ioException) {
            ioException.printStackTrace();
            this.phpResult = new PhpResult().setError(defaultErrorKey, ioException.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        this.callback.finish(this.phpResult);

    }

    @Override
    protected void onCancelled() {
        this.callback.cancel(new PhpResult().setError(this.defaultErrorKey, "cancelled"));
    }

}
