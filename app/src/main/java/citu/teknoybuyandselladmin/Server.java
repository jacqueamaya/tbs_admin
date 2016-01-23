package citu.teknoybuyandselladmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.Map;

public final class Server {

    private static final String URL_ADD_CATEGORY = "http://tbs-admin.herokuapp.com/api/add_category";
    private static final String URL_APPROVE_DONATION = "http://tbs-admin.herokuapp.com/api/admin_approveDonation";
    private static final String URL_APPROVE_SELL = "http://tbs-admin.herokuapp.com/api/admin_approveItem";
    private static final String URL_DISAPPROVE_DONATION = "http://tbs-admin.herokuapp.com/api/admin_disapproveDonation";
    private static final String URL_DISAPPROVE_SELL = "http://tbs-admin.herokuapp.com/api/admin_disapproveItem";
    private static final String URL_ITEM_AVAILABLE = "http://tbs-admin.herokuapp.com/api/item_available";
    private static final String URL_ITEM_CLAIMED = "http://tbs-admin.herokuapp.com/api/item_claimed";
    private static final String URL_LOGIN = "http://tbs-admin.herokuapp.com/api/admin_login";
    private static final String URL_READ_NOTIFICATION = "http://tbs-admin.herokuapp.com/api/read_notification";
    private static final String URL_RETURN_ITEM = "http://tbs-admin.herokuapp.com/api/return_rented";
    private static final String URL_NOTIFY_RENTER = "http://tbs-admin.herokuapp.com/api/notify_renter";

    private static final String URL_CATEGORIES = "http://tbs-admin.herokuapp.com/api-x/categories/";
    private static final String URL_DONATED_ITEMS_DETAILS = "http://tbs-admin.herokuapp.com/api-x/donate_requests/";
    private static final String URL_DONATE_REQUEST = "http://tbs-admin.herokuapp.com/api-x/donate_requests/";
    private static final String URL_ITEMS_ON_QUEUE_DETAILS = "http://tbs-admin.herokuapp.com/api-x/sell_requests/";
    private static final String URL_NOTIFICATION = "http://tbs-admin.herokuapp.com/api-x/admin_notifications";
    private static final String URL_RESERVED_ITEMS = "http://tbs-admin.herokuapp.com/api-x/reservation_requests/";
    private static final String URL_SELL_REQUEST = "http://tbs-admin.herokuapp.com/api-x/sell_requests/";
    private static final String URL_TRANSACTIONS = "http://tbs-admin.herokuapp.com/api-x/transactions/";
    private static final String URL_RENTED_ITEMS = "http://tbs-admin.herokuapp.com/api-x/rented_items/";

    private static final String TAG = "Server";

    public static void login(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey(LoginActivity.USERNAME) || !data.containsKey(LoginActivity.PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_LOGIN, progressDialog, data, callbacks);
    }

    public static void getNotifications(String username,ProgressBar progressBar, Ajax.Callbacks callbacks) {
        if (username == null) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.get(URL_NOTIFICATION + "/?username=" + username, progressBar, callbacks);
    }

    public static void getReservedItems(ProgressBar progressBar, Ajax.Callbacks callbacks) {
        Ajax.get(URL_RESERVED_ITEMS, progressBar, callbacks);
    }

    public static void getSellRequests(ProgressBar progressBar, Ajax.Callbacks callbacks) {
        Ajax.get(URL_SELL_REQUEST, progressBar, callbacks);
    }

    public static void getDonateRequests(ProgressBar progressBar, Ajax.Callbacks callbacks) {
        Ajax.get(URL_DONATE_REQUEST, progressBar, callbacks);
    }

    public static void getTransactions(ProgressBar progressBar, Ajax.Callbacks callbacks) {
        Ajax.get(URL_TRANSACTIONS, progressBar, callbacks);
    }

    public static void getQueueItemDetails(Map<String, String> data, ProgressBar progressBar, Ajax.Callbacks callbacks) {
        if (!data.containsKey("request_id")) {
            throw new RuntimeException("Missing data.");
        }

        String requestId = data.get("request_id");
        Ajax.get(URL_ITEMS_ON_QUEUE_DETAILS + "?request_id=" + requestId, progressBar, callbacks);
    }

    public static void approveQueuedItem(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey("request_id") ||
                !data.containsKey("item_id") ||
                !data.containsKey("category")) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_APPROVE_SELL, progressDialog, data, callbacks);
    }

    public static void denyQueuedItem(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey("request_id") ||
                !data.containsKey("item_id")) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_DISAPPROVE_SELL, progressDialog, data, callbacks);
    }

    public static void getDonatedItemDetails(Map<String, String> data, ProgressBar progressBar, Ajax.Callbacks callbacks) {
        if (!data.containsKey("request_id")) {
            throw new RuntimeException("Missing data.");
        }

        String requestId = data.get("request_id");
        Ajax.get(URL_DONATED_ITEMS_DETAILS + "?request_id=" + requestId, progressBar, callbacks);
    }

    public static void approveDonatedItem(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey("request_id") ||
                !data.containsKey("item_id") ||
                !data.containsKey("category") ||
                !data.containsKey("stars_required")) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_APPROVE_DONATION, progressDialog, data, callbacks);
    }

    public static void denyDonatedItem(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey("request_id") || !data.containsKey("item_id")) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_DISAPPROVE_DONATION, progressDialog, data, callbacks);
    }

    public static void getReservedItemDetails(Map<String, String> data, ProgressBar progressBar, Ajax.Callbacks callbacks) {
        if (!data.containsKey("request_id")) {
            throw new RuntimeException("Missing data.");
        }

        String requestId = data.get("request_id");
        Ajax.get(URL_RESERVED_ITEMS + "?request_id=" + requestId, progressBar, callbacks);
    }

    public static void itemAvailable(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey("request_id") ||
                !data.containsKey("item_id")) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_ITEM_AVAILABLE, progressDialog, data, callbacks);
    }

    public static void itemClaimed(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        Log.e(TAG,data.toString());
        if (!data.containsKey("request_id") ||
                !data.containsKey("item_id")) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_ITEM_CLAIMED, progressDialog, data, callbacks);
    }

    public static void addCategory(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey(QueueItemDetailActivity.CATEGORY_ITEM)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_ADD_CATEGORY, progressDialog, data, callbacks);
    }

    public static void getCategories(ProgressBar progressBar, Ajax.Callbacks callbacks) {
        Ajax.get(URL_CATEGORIES, progressBar, callbacks);
    }

    public static void getRentedItems(ProgressBar progressBar, Ajax.Callbacks callbacks) {
        Ajax.get(URL_RENTED_ITEMS, progressBar, callbacks);
    }

    public static void readNotification(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey(NotificationsActivity.NOTIFICATION_ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_READ_NOTIFICATION, progressDialog, data, callbacks);
    }

    public static void returnRentedItem(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey(RentedItemDetailActivity.RENT_ID) &&
                !data.containsKey(RentedItemDetailActivity.ITEM_ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_RETURN_ITEM, progressDialog, data, callbacks);
    }

    public static void notifyRenter(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey(RentedItemDetailActivity.RENT_ID) &&
                !data.containsKey(RentedItemDetailActivity.ITEM_ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_NOTIFY_RENTER, progressDialog, data, callbacks);
    }
}
