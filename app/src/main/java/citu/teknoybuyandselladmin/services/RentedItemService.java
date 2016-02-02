package citu.teknoybuyandselladmin.services;

import android.content.Intent;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselladmin.ServiceManager;
import citu.teknoybuyandselladmin.models.RentedItem;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by Batistil on 1/31/2016.
 */
public class RentedItemService extends ConnectionService {
    public static final String TAG = "RentedItemService";
    public static final String ACTION = RentedItemService.class.getCanonicalName();

    public RentedItemService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG,"getting rented items. . .");

        TbsService service = ServiceManager.getInstance();
        try{
            Call<List<RentedItem>> call = service.getRentedItems();
            Response<List<RentedItem>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<RentedItem> rentedItems = response.body();

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(RentedItem.class).findAll().clear();
                realm.copyToRealmOrUpdate(rentedItems);
                realm.commitTransaction();
                realm.close();

                notifySuccess(ACTION, "Successful");
            }else{
                String error = response.errorBody().string();
                Log.e(TAG,"Error: " + error);
                notifyFailure(ACTION, "Failed");
            }
        }catch(Exception e){
            Log.e(TAG, e.getMessage(), e);
            notifyFailure(ACTION, "Failed");
        }
    }
}
