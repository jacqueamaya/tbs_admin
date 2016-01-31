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
import java.util.List;
import java.util.Map;

import citu.teknoybuyandselladmin.ServiceManager;
import citu.teknoybuyandselladmin.models.Notification;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit.Call;
import retrofit.Response;

public class NotificationService extends ConnectionService{
    public static final String TAG = "NotificationService";
    public static final String ACTION = NotificationService.class.getCanonicalName();


    public NotificationService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG,"getting notifications. . . ");
        TbsService service = ServiceManager.getInstance();
        try {
            Call<List<Notification>> call = service.getNotifications();
            Response<List<Notification>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<Notification> notifications = response.body();

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(Notification.class).findAll().clear();
                realm.copyToRealmOrUpdate(notifications);
                realm.commitTransaction();
                realm.close();

                //Log.e(TAG, notifications.get(0).getMessage());
                notifySuccess(ACTION, "Successful");
            }else{
                String error = response.errorBody().string();
                Log.e(TAG, "Error: " + error);
                notifyFailure(ACTION, "Error");
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            notifyFailure(ACTION, "Request Error");
        }
    }

}
