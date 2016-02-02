package citu.teknoybuyandselladmin.services;

import android.content.Intent;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselladmin.ServiceManager;
import citu.teknoybuyandselladmin.models.Reservation;
import citu.teknoybuyandselladmin.models.ResponseStatus;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by Batistil on 2/2/2016.
 */
public class ItemAvailableService extends ConnectionService {

    public static final String TAG = "ItemAvailableSvc";
    public static final String ACTION = ItemAvailableService.class.getCanonicalName();

    public ItemAvailableService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "setting item available. . .");
        int requestId = intent.getIntExtra("requestId", 0);
        int itemId = intent.getIntExtra("itemId", 0);

        Log.e(TAG,"request id: " + requestId + " itemid: " + itemId );
        TbsService service = ServiceManager.getInstance();

        try{
            Log.e(TAG,"preparing to connect to server");
            Call<ResponseStatus> call = service.setItemAvailable(requestId, itemId);
            Response<ResponseStatus> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                Log.e(TAG,"connected to server");
                ResponseStatus responseStatus = response.body();
                int statusCode = responseStatus.getStatus();

                if(statusCode == HttpURLConnection.HTTP_OK){
                    Log.e(TAG, "Successfully approved item");
                    Call<List<Reservation>> reserveCall = service.getReservedItems();
                    Response<List<Reservation>> reserveResponse = reserveCall.execute();

                    if(reserveResponse.code() == HttpURLConnection.HTTP_OK){
                        Log.e(TAG,"successfully fetched updated list");
                        List<Reservation> updatedList = reserveResponse.body();

                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.where(Reservation.class).findAll().clear();
                        realm.copyToRealmOrUpdate(updatedList);
                        realm.commitTransaction();
                        realm.close();

                        Log.e(TAG, "Successfully updated list");
                    }else{
                        Log.e(TAG, "Unable to update list");
                    }
                    notifySuccess(ACTION, "item_available");
                }
                else{
                    Log.e(TAG,"Missing data");
                    notifyFailure(ACTION,"Missing data");
                }
            }else{
                Log.e(TAG, response.errorBody().string());
                notifyFailure(ACTION,"Cannot connect to server");
            }

        }catch(Exception e){
            Log.e(TAG, e.getMessage(), e);
            notifyFailure(ACTION,"Connection error");
        }
    }
}
