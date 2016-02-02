package citu.teknoybuyandselladmin.services;

import android.content.Intent;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselladmin.ServiceManager;
import citu.teknoybuyandselladmin.models.Transaction;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by Batistil on 1/31/2016.
 */
public class TransactionService extends ConnectionService {
    public static final String TAG = "TransactionService";
    public static final String ACTION = TransactionService.class.getCanonicalName();

    public TransactionService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "getting transactions");
        TbsService service = ServiceManager.getInstance();

        try{
            Call<List<Transaction>> call = service.getTransactions();
            Response<List<Transaction>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<Transaction> transactions = response.body();

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(Transaction.class).findAll().clear();
                realm.copyToRealmOrUpdate(transactions);
                realm.commitTransaction();
                realm.close();

                notifySuccess(ACTION,"Successful");
            }else{
                Log.e(TAG,"Error: " + response.errorBody().string());
                notifyFailure(ACTION, "Failed");
            }
        }catch(Exception e){
            Log.e(TAG, e.getMessage(), e);
            notifyFailure(ACTION, "Request error");
        }
    }
}
