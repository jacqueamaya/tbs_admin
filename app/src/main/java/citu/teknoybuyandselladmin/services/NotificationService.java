package citu.teknoybuyandselladmin.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class NotificationService extends IntentService{
    public static final String TAG = "NotificationService";
    public static final String ACTION = NotificationService.class.getCanonicalName();

    public NotificationService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            URL url = new URL("http://tbs-admin.herokuapp.com/api-x/admin_notifications/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            String responseBody = readStream(connection.getInputStream());
            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                notifySuccess(responseBody);
                Log.e(TAG, "Successful: " + responseBody);

            } else {
                notifyFailure(responseBody);
                Log.e(TAG, "Failed : "+ responseBody);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    protected void notifySuccess(String responseBody){
        Intent intent = new Intent(NotificationService.class.getCanonicalName());
        intent.putExtra("service result", 1);
        intent.putExtra("response", responseBody);
        sendBroadcast(intent);
    }

    protected void notifyFailure(String responseBody){
        Intent intent = new Intent(NotificationService.class.getCanonicalName());
        intent.putExtra("service result", -1);
        intent.putExtra("response",  responseBody);
        sendBroadcast(intent);
    }


    public static String serialize (Map<String, String> data) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (String key : data.keySet()) {
            if (first) {
                builder.append(key).append('=').append(data.get(key));
                first = false;
            } else {
                builder.append('&').append(key).append('=').append(data.get(key));
            }
        }

        return builder.toString();
    }

    public static String readStream (InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            is.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static void writeToStream (OutputStream os, String whatToWrite) {
        PrintWriter writer = new PrintWriter(os);
        try {
            writer.write(whatToWrite);
        } finally {
            writer.flush();
            writer.close();
        }
    }
}
