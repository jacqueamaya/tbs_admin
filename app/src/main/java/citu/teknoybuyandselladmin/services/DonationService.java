package citu.teknoybuyandselladmin.services;

import android.content.Intent;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselladmin.ServiceManager;
import citu.teknoybuyandselladmin.models.DonateApproval;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by Batistil on 1/31/2016.
 */
public class DonationService extends ConnectionService{
    public static final String TAG = "DonationService";
    public static final String ACTION = DonationService.class.getCanonicalName();

    public DonationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "gettting donation requests");

        TbsService service = ServiceManager.getInstance();
        try{
            Call<List<DonateApproval>> call = service.getDonationRequests();
            Response<List<DonateApproval>> response =  call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<DonateApproval> donateApprovals = response.body();

                Realm realm  = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(DonateApproval.class).findAll().clear();
                realm.copyToRealmOrUpdate(donateApprovals);
                realm.commitTransaction();
                realm.close();

                notifySuccess(ACTION, "Successful");
            }else{
                String error = response.errorBody().string();
                Log.e(TAG, "Error: " + error);
                notifyFailure(ACTION, "Failed");
            }

        }catch(Exception e){
            Log.e(TAG, e.getMessage(), e);
            notifyFailure(ACTION, "Request error");
        }
    }
}
