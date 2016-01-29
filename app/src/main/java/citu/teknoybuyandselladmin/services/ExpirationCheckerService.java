package citu.teknoybuyandselladmin.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExpirationCheckerService extends IntentService {

    public static final String TAG = "ExpirationCheckerSvc";
    public static final String ACTION = ExpirationCheckerService.class.getCanonicalName();

    public ExpirationCheckerService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            URL url = new URL("http://tbs-admin.herokuapp.com/api/admin_check_expiration");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                Log.e(TAG, "Successful");
            } else {
                Log.e(TAG, "Failed : "+ connection.getResponseMessage());
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
