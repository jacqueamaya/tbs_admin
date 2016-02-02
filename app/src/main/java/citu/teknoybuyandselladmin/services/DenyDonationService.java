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
public class DenyDonationService extends ConnectionService {

    public static final String TAG = "DenyDonationSvc";
    public static final String ACTION = DenyDonationService.class.getCanonicalName();

    public DenyDonationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "denying item. . .");
        int requestId = intent.getIntExtra("requestId",0);
        int itemId = intent.getIntExtra("itemId", 0);

        TbsService service = ServiceManager.getInstance();
        try{
            Log.e(TAG,"preparing to connect to server");
            Call<ResponseStatus> call = service.denyDonatedItem(requestId, itemId);
            Response<ResponseStatus> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                Log.e(TAG,"connected to server");
                ResponseStatus  responseStatus = response.body();

                if(responseStatus.getStatus() == HttpURLConnection.HTTP_OK){
                    Log.e(TAG,"successful denying");
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
                    Log.e(TAG,"Successfully disapproved item");
                    notifySuccess(ACTION,"disapproved_donation");
                }else{
                    Log.e(TAG,"Missing data");
                    notifyFailure(ACTION,"Missing data");
                }
            }else{
                Log.e(TAG, response.errorBody().string());
                notifyFailure(ACTION,"Cannot connect to server");
            }
        }catch(Exception e){
            Log.e(TAG,e.getMessage(),e);
            notifyFailure(ACTION,"Connection Error");
        }
    }
}
