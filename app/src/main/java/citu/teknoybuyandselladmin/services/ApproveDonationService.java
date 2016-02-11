package citu.teknoybuyandselladmin.services;

import android.content.Intent;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselladmin.ServiceManager;
import citu.teknoybuyandselladmin.models.DonateApproval;
import citu.teknoybuyandselladmin.models.ResponseStatus;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by Batistil on 2/2/2016.
 */
public class ApproveDonationService extends ConnectionService {
    public static final String TAG = "ApproveDonationSvc";
    public static final String ACTION = ApproveDonationService.class.getCanonicalName();

    public ApproveDonationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "approving item. . .");
        int requestId = intent.getIntExtra("requestId", 0);
        int itemId = intent.getIntExtra("itemId", 0);
        int starsRequired = intent.getIntExtra("starsRequired",0);
        String category = intent.getStringExtra("category");


        Log.e(TAG,"request id: " + requestId + " itemid: " + itemId + " Category: " + category + "Stars: " + starsRequired);
        TbsService service = ServiceManager.getInstance();

        try{
            Log.e(TAG,"preparing to connect to server");
            Call<ResponseStatus> call = service.approveDonatedItem(requestId, itemId, category, starsRequired);
            Response<ResponseStatus> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                Log.e(TAG,"connected to server");
                ResponseStatus responseStatus = response.body();
                int statusCode = responseStatus.getStatus();

                if(statusCode == HttpURLConnection.HTTP_OK){
                    Log.e(TAG, "Successfully approved item");
                    Call<List<DonateApproval>> donateCall = service.getDonationRequests();
                    Response<List<DonateApproval>> donateResponse = donateCall.execute();

                    if(donateResponse.code() == HttpURLConnection.HTTP_OK){
                        Log.e(TAG,"successfully fetched updated list");
                        List<DonateApproval> updatedList = donateResponse.body();

                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.where(DonateApproval.class).findAll().clear();
                        realm.copyToRealmOrUpdate(updatedList);
                        realm.commitTransaction();
                        realm.close();

                        Log.e(TAG, "Successfully updated list");
                    }else{
                        Log.e(TAG, "Unable to update list");
                    }
                    notifySuccess(ACTION, "approved_donation");
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
