package citu.teknoybuyandselladmin.services;

import android.content.Intent;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselladmin.ServiceManager;
import citu.teknoybuyandselladmin.models.SellApproval;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by Batistil on 1/31/2016.
 */
public class ItemsOnQueueService extends ConnectionService {
    public static final String TAG = "ItemsOnQueueSvc";
    public static final String ACTION = ItemsOnQueueService.class.getCanonicalName();

    public ItemsOnQueueService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG,"getting items on queue. . .");
        TbsService service = ServiceManager.getInstance();
        try{
            Call<List<SellApproval>> call = service.getItemsOnQueue();
            Response<List<SellApproval>> response =  call.execute();

            if(response.code()  == HttpURLConnection.HTTP_OK){
                List<SellApproval> sellApprovals = response.body();

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(SellApproval.class).findAll().clear();
                realm.copyToRealmOrUpdate(sellApprovals);
                realm.commitTransaction();
                realm.close();

                //Log.e(TAG, sellApprovals.get(0).getItem().getName());
                notifySuccess(ACTION, "Successful");
            }else{
                String error = response.errorBody().toString();
                Log.e(TAG, "Error: " + error);
                notifyFailure(ACTION, "Failed");
            }

        }catch(Exception e){
            Log.e(TAG, e.getMessage(), e);
            notifyFailure(ACTION, "Request error");
        }
    }
}
