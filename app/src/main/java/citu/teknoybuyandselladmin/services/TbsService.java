package citu.teknoybuyandselladmin.services;

import java.util.List;

import citu.teknoybuyandselladmin.models.Category;
import citu.teknoybuyandselladmin.models.DonateApproval;
import citu.teknoybuyandselladmin.models.Item;
import citu.teknoybuyandselladmin.models.Notification;
import citu.teknoybuyandselladmin.models.RentedItem;
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

    @FormUrlEncoded
    @POST("api/admin_approveItem")
    Call<ResponseStatus> denyQueuedItem(@Field("request_id") int id, @Field("item_id") int itemId);

    @FormUrlEncoded
    @POST("api/admin_approveItem")
    Call<ResponseStatus> approveDonatedItem(@Field("request_id") int id, @Field("item_id") int itemId, @Field("category") String category, @Field("stars_required") int stars);

    @FormUrlEncoded
    @POST("api/admin_approveItem")
    Call<ResponseStatus> denyDonatedItem(@Field("request_id") int id, @Field("item_id") int itemId);

    @FormUrlEncoded
    @POST("api/item_available")
    Call<ResponseStatus> setItemAvailable(@Field("request_id") int id, @Field("item_id") int itemId);

    @FormUrlEncoded
    @POST("api/item_claimed")
    Call<ResponseStatus> setItemClaimed(@Field("request_id") int id, @Field("item_id") int itemId);

    @FormUrlEncoded
    @POST("api/return_rented")
    Call<ResponseStatus> returnRented(@Field("rent_id") int id, @Field("item_id") int itemId);

    @FormUrlEncoded
    @POST("api/notify_renter")
    Call<ResponseStatus> notifyRenter(@Field("rent_id") int id, @Field("item_id") int itemId);

    @FormUrlEncoded
    @POST("api/add_category")
    Call<ResponseStatus> addCategory(@Field("category") String category);

    @FormUrlEncoded
    @POST("api/read_notification")
    Call<ResponseStatus> readNotification(@Field("notification_id") int id);

    @POST("api/admin_check_expiration")
    Call<ResponseStatus> checkExpiration();


    @GET("api-x/admin_notifications")
    Call<List<Notification>> getNotifications();

    @GET("api-x/reservation_requests")
    Call<List<Reservation>> getReservedItems();

    @GET("api-x/sell_requests")
    Call<List<SellApproval>> getItemsOnQueue();

    @GET("api-x/donate_requests")
    Call<List<DonateApproval>> getDonationRequests();

    @GET("api-x/transactions")
    Call<List<Transaction>> getTransactions();

    @GET("api-x/categories")
    Call<List<Category>> getCategories();

    @GET("api-x/rented_items")
    Call<List<RentedItem>> getRentedItems();
}
