package citu.teknoybuyandselladmin.services;

import android.content.Intent;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselladmin.ServiceManager;
import citu.teknoybuyandselladmin.models.Reservation;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by Batistil on 1/31/2016.
 */
public class ReservationService extends ConnectionService {
    public static final String TAG = "ReservationService";
    public static final String ACTION = ReservationService.class.getCanonicalName();

    public ReservationService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "getting reservations. . .");

        TbsService service = ServiceManager.getInstance();
        try{
            Call<List<Reservation>> call = service.getReservedItems();
            Response<List<Reservation>> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<Reservation> reservations = response.body();

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(Reservation.class).findAll().clear();
                realm.copyToRealmOrUpdate(reservations);
                realm.commitTransaction();
                realm.close();

                notifySuccess(ACTION, "Success");
            }else{
                String error = response.errorBody().string();
                Log.e(TAG, "Error: "+ error);
                notifyFailure(ACTION, "Failed");
            }
        }catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
            notifyFailure(ACTION, "Request error");
        }
    }
}
