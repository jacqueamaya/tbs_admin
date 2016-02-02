package citu.teknoybuyandselladmin.services;

import android.content.Intent;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselladmin.ServiceManager;
import citu.teknoybuyandselladmin.models.ResponseStatus;
import citu.teknoybuyandselladmin.models.SellApproval;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

public class ApproveItemService extends ConnectionService {
    public static final String TAG = "ApproveItemService";
    public static final String ACTION = ApproveItemService.class.getCanonicalName();

    public ApproveItemService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG,"approving item. . .");
        int requestId = intent.getIntExtra("requestId", 0);
        int itemId = intent.getIntExtra("itemId", 0);
        String category = intent.getStringExtra("category");
        Log.e(TAG,"request id: " + requestId + " itemid: " + itemId + " Category: " + category);
        TbsService service = ServiceManager.getInstance();

        try{
            Log.e(TAG,"preparing to connect to server");
            Call<ResponseStatus> call = service.approveQueuedItem(requestId,itemId,category);
            Response<ResponseStatus> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                Log.e(TAG,"connected to server");
                ResponseStatus responseStatus = response.body();
                int statusCode = responseStatus.getStatus();

                if(statusCode == HttpURLConnection.HTTP_OK){
                    Log.e(TAG, "Successfully approved item");
                    Call<List<SellApproval>> sellCall = service.getItemsOnQueue();
                    Response<List<SellApproval>> sellResponse = sellCall.execute();

                    if(sellResponse.code() == HttpURLConnection.HTTP_OK){
                        Log.e(TAG,"successfully fetched updated list");
                        List<SellApproval> updatedList = sellResponse.body();

                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.where(SellApproval.class).findAll().clear();
                        realm.copyToRealmOrUpdate(updatedList);
                        realm.commitTransaction();
                        realm.close();

                        Log.e(TAG, "Successfully updated list");
                    }else{
                        Log.e(TAG, "Unable to update list");
                    }
                    notifySuccess(ACTION, "approved_item");
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
