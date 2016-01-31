package citu.teknoybuyandselladmin.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Batistil on 1/28/2016.
 */
public abstract class ConnectionService extends IntentService{
    public ConnectionService(String name) {
        super(name);
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
}
