package citu.teknoybuyandselladmin.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Batistil on 1/28/2016.
 */
public abstract class ConnectionService extends IntentService{
    public ConnectionService(String name) {
        super(name);
    }

    protected void notifySuccess(String className, String responseBody){
        Intent intent = new Intent(className);
        intent.putExtra("result", 1);
        intent.putExtra("response","Response : " + responseBody);
        sendBroadcast(intent);
    }

    protected void notifyFailure(String className, String responseBody){
        Intent intent = new Intent(className);
        intent.putExtra("result", -1);
        intent.putExtra("response", "Response: " + responseBody);
        sendBroadcast(intent);
    }
}
