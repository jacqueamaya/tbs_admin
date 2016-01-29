package citu.teknoybuyandselladmin.services;

import java.util.List;

import citu.teknoybuyandselladmin.models.Notification;
import citu.teknoybuyandselladmin.models.Reservation;
import citu.teknoybuyandselladmin.models.ResponseStatus;
import citu.teknoybuyandselladmin.models.SellApproval;
import citu.teknoybuyandselladmin.models.Transaction;
import citu.teknoybuyandselladmin.models.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by Batistil on 1/28/2016.
 */
public interface TbsService {

    String BASE_URL = "http://tbs-admin.herokuapp.com/";

    @FormUrlEncoded
    @POST("api/admin_login")
    Call<ResponseStatus> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("api/admin_approveItem")
    Call<ResponseStatus> approveQueuedItem(@Field("request_id") int id, @Field("item_id") int itemId, @Field("category") String category);

    @GET("api-x/admin_notifications")
    Call<List<Notification>> getNotifications();

    @GET("api-x/reservation_requests")
    Call<List<Reservation>> getReservedItems();

    @GET("api-x/sell_requests")
    Call<List<SellApproval>> getSellRequests();


    @GET("api-x/transactions")
    Call<List<Transaction>> getTransactions();
}
