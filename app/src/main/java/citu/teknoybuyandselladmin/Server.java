package citu.teknoybuyandselladmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

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

    private static final String URL_CATEGORIES = "http://tbs-admin.herokuapp.com/api-x/categories/";
    private static final String URL_DONATED_ITEMS_DETAILS = "http://tbs-admin.herokuapp.com/api-x/donate_requests/";
    private static final String URL_DONATE_REQUEST = "http://tbs-admin.herokuapp.com/api-x/donate_requests/";
    private static final String URL_ITEMS_ON_QUEUE_DETAILS = "http://tbs-admin.herokuapp.com/api-x/sell_requests/";
    private static final String URL_NOTIFICATION = "http://tbs-admin.herokuapp.com/api-x/admin_notifications";
    private static final String URL_RESERVED_ITEMS = "http://tbs-admin.herokuapp.com/api-x/reservation_requests/";
    private static final String URL_SELL_REQUEST = "http://tbs-admin.herokuapp.com/api-x/sell_requests/";
    private static final String URL_TRANSACTIONS = "http://tbs-admin.herokuapp.com/api-x/transactions/";

    private static final String TAG = "Server";

    public static void login(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey(LoginActivity.USERNAME) || !data.containsKey(LoginActivity.PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_LOGIN, progressDialog, data, callbacks);
    }

    public static void getNotifications(String username, Ajax.Callbacks callbacks) {
        if (username == null) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.get(URL_NOTIFICATION + "/?username=" + username, callbacks);
    }

    public static void getReservedItems(Ajax.Callbacks callbacks) {
        Ajax.get(URL_RESERVED_ITEMS, callbacks);
    }

    public static void getSellRequests(Ajax.Callbacks callbacks) {
        Ajax.get(URL_SELL_REQUEST, callbacks);
    }

    public static void getDonateRequests(Ajax.Callbacks callbacks) {
        Ajax.get(URL_DONATE_REQUEST, callbacks);
    }

    public static void getTransactions(Ajax.Callbacks callbacks) {
        Ajax.get(URL_TRANSACTIONS, callbacks);
    }

    public static void getQueueItemDetails(Map<String, String> data, Ajax.Callbacks callbacks) {
        if (!data.containsKey("request_id")) {
            throw new RuntimeException("Missing data.");
        }

        String requestId = data.get("request_id");
        Ajax.get(URL_ITEMS_ON_QUEUE_DETAILS + "?request_id=" + requestId, callbacks);
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

    public static void getDonatedItemDetails(Map<String, String> data, Ajax.Callbacks callbacks) {
        if (!data.containsKey("request_id")) {
            throw new RuntimeException("Missing data.");
        }

        String requestId = data.get("request_id");
        Ajax.get(URL_DONATED_ITEMS_DETAILS + "?request_id=" + requestId, callbacks);
    }

    public static void approveDonatedItem(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey("request_id") ||
                !data.containsKey("item_id") ||
                !data.containsKey("activity_category") ||
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

    public static void getReservedItemDetails(Map<String, String> data, Ajax.Callbacks callbacks) {
        if (!data.containsKey("request_id")) {
            throw new RuntimeException("Missing data.");
        }

        String requestId = data.get("request_id");
        Ajax.get(URL_RESERVED_ITEMS + "?request_id=" + requestId, callbacks);
    }

    public static void itemAvailable(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey("request_id") ||
                !data.containsKey("item_id")) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_ITEM_AVAILABLE, progressDialog, data, callbacks);
    }

    public static void itemClaimed(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
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

    public static void getCategories(Ajax.Callbacks callbacks) {
        Ajax.get(URL_CATEGORIES, callbacks);
    }

    public static void readNotification(Map<String, String> data, ProgressDialog progressDialog, Ajax.Callbacks callbacks) {
        if (!data.containsKey(NotificationsActivity.NOTIFICATION_ID)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_READ_NOTIFICATION, progressDialog, data, callbacks);
    }
}
