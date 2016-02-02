package citu.teknoybuyandselladmin.services;

import android.content.Intent;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.List;

import citu.teknoybuyandselladmin.ServiceManager;
import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.models.ResponseStatus;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by Batistil on 2/1/2016.
 */
public class AddCategoryService extends ConnectionService {
    public static final String TAG =  "AddCategoryService";
    public static final String ACTION = AddCategoryService.class.getCanonicalName();

    public AddCategoryService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String category = intent.getStringExtra("category");

        TbsService service = ServiceManager.getInstance();
        try{
            Call<ResponseStatus> call = service.addCategory(category);
            Response<ResponseStatus> response = call.execute();

            if(response.code() == HttpURLConnection.HTTP_OK){
                ResponseStatus responseStatus = response.body();

                if(responseStatus.getStatus() == HttpURLConnection.HTTP_OK){

                    Call<List<Category>> categoriesCall = service.getCategories();
                    Response<List<Category>> categoriesResponse = categoriesCall.execute();

                    if(categoriesResponse.code() == HttpURLConnection.HTTP_OK){
                        List<Category> updatedCategories = categoriesResponse.body();

                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.where(Category.class).findAll().clear();
                        realm.copyToRealmOrUpdate(updatedCategories);
                        realm.commitTransaction();
                        realm.close();

                        Log.e(TAG, "Successfully fetched updated list of categories");
                    }else{
                        Log.e(TAG, "Unable to fetch all categories");
                    }
                    Log.e(TAG, "Successfully added category");
                    notifySuccess(ACTION, "add_category");
                }else if(responseStatus.getStatus() == 403){
                    Log.e(TAG, "Category already exists");
                    notifyFailure(ACTION, "Category already exists");
                }else{
                    Log.e(TAG, "Missing data");
                    notifyFailure(ACTION, "Missing data");
                }
            }else{
                Log.e(TAG, response.errorBody().string());
                notifyFailure(ACTION, "Cannot connect to server");
            }
        }catch(Exception e){
            Log.e(TAG, e.getMessage(), e);
            notifyFailure(ACTION, "Connection error");
        }
    }
}
