package citu.teknoybuyandselladmin.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import citu.teknoybuyandselladmin.ServiceManager;
import citu.teknoybuyandselladmin.models.ResponseStatus;
import retrofit.Call;
import retrofit.Response;

public class ExpirationCheckerService extends IntentService {

    public static final String TAG = "ExpirationCheckerSvc";
    public static final String ACTION = ExpirationCheckerService.class.getCanonicalName();

    public ExpirationCheckerService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TbsService service = ServiceManager.getInstance();
        try {
            Call<ResponseStatus> call = service.checkExpiration();
            Response<ResponseStatus> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                Log.e(TAG, "Successfully checked expiration.");
            } else{
                Log.e(TAG, "Error: " + response.errorBody().string());
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
