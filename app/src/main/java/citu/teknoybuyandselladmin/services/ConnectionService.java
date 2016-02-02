package citu.teknoybuyandselladmin.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Batistil on 1/28/2016.
 */
public abstract class ConnectionService extends IntentService{
    private static final String TAG = "ConnectionService";

    public ConnectionService(String name) {
        super(name);
    }

    protected void notifySuccess(String className, String responseBody){
        Log.e(TAG,"sending intent broadcast");
        Intent intent = new Intent(className);
        intent.putExtra("result", 1);
        intent.putExtra("response","" + responseBody);
        sendBroadcast(intent);
        Log.e(TAG, "Response: " + responseBody);
    }

    protected void notifyFailure(String className, String responseBody){
        Log.e(TAG,"sending intent broadcast");
        Intent intent = new Intent(className);
        intent.putExtra("result", -1);
        intent.putExtra("response", "" + responseBody);
        sendBroadcast(intent);
        Log.e(TAG, "Response: " + responseBody);
    }
}
