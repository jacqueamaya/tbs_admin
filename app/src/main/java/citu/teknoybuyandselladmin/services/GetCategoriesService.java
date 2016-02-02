package citu.teknoybuyandselladmin.services;

import android.content.Intent;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselladmin.ServiceManager;
import citu.teknoybuyandselladmin.models.Category;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by Batistil on 2/1/2016.
 */
public class GetCategoriesService extends ConnectionService {
    public static final String TAG = "GetCategoriesService";
    public static final String ACTION = GetCategoriesService.class.getCanonicalName();

    public GetCategoriesService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG,"getting categories. . .");
        TbsService service = ServiceManager.getInstance();

        try{
            Call<List<Category>> call = service.getCategories();
            Response<List<Category>> response  = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                List<Category> categories = response.body();

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.where(Category.class).findAll().clear();
                realm.copyToRealmOrUpdate(categories);
                realm.commitTransaction();
                realm.close();

                notifySuccess(ACTION,"get_categories");
            }else{
                Log.e(TAG,response.errorBody().string());
                notifyFailure(ACTION,"Cannot connect to server");
            }
        }catch(Exception e){
            Log.e(TAG,e.getMessage(), e);
            notifyFailure(ACTION,"Connection error");
        }
    }
}
