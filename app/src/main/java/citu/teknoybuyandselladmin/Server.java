package citu.teknoybuyandselladmin;

import android.util.Log;

import java.util.Map;

public class Server {
    private static final String URL = "10.0.3.2:8000";
    private static final String URL_LOGIN = "http://"+URL+"/api/admin_login";
    private static final String URL_NOTIFICATION = "http://"+URL+"/api-x/admin_notifications";
    private static final String URL_RESERVED_ITEMS = "http://"+URL+"/api-x/reservation_requests/";
    private static final String URL_SELL_REQUEST = "http://"+URL+"/api-x/sell_requests/";
    private static final String URL_DONATE_REQUEST = "http://"+URL+"/api-x/donate_requests/";
    private static final String URL_TRANSACTIONS = "http://"+URL+"/api-x/transactions/";
    private static final String URL_ITEMS_ON_QUEUE_DETAILS = "http://"+URL+"/api-x/sell_requests/";
    private static final String URL_DONATED_ITEMS_DETAILS = "http://"+URL+"/api-x/donate_requests/";
    private static final String URL_APPROVE_SELL = "http://"+URL+"/api/admin_approveItem";
    private static final String URL_DISAPPROVE_SELL = "http://"+URL+"/api/admin_disapproveItem";
    private static final String URL_APPROVE_DONATION = "http://"+URL+"/api/admin_approveDonation";
    private static final String URL_DISAPPROVE_DONATION = "http://"+URL+"/api/admin_disapproveDonation";
    private static final String URL_ITEM_AVAILABLE = "http://"+URL+"/api/item_available";
    private static final String URL_ITEM_CLAIMED = "http://"+URL+"/api/item_claimed";
    private static final String URL_ADD_CATEGORY = "http://"+URL+"/api/add_category";
    private static final String URL_CATEGORIES = "http://"+URL+"/api-x/categories/";

    private static final String TAG = "Server";


    public static void login (Map<String, String> data, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey(LoginActivity.USERNAME) ||
                ! data.containsKey(LoginActivity.PASSWORD)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_LOGIN, data, callbacks);
    }

    public static void getNotifications (String username, Ajax.Callbacks callbacks) {
        if ( username == null) {
            throw new RuntimeException("Missing data.");
        }
        Ajax.get(URL_NOTIFICATION + "/?username=" + username, callbacks);
    }

    public static void getReservedItems (Ajax.Callbacks callbacks) {
        Ajax.get(URL_RESERVED_ITEMS, callbacks);
    }

    public static void getSellRequests (Ajax.Callbacks callbacks) {
        Ajax.get(URL_SELL_REQUEST, callbacks);
    }

    public static void getDonateRequests (Ajax.Callbacks callbacks) {
        Ajax.get(URL_DONATE_REQUEST, callbacks);
    }

    public static void getTransactions (Ajax.Callbacks callbacks) {
        Ajax.get(URL_TRANSACTIONS, callbacks);
    }

    public static void getQueueItemDetails (Map<String, String> data, Ajax.Callbacks callbacks) {
        String requestId;
        if (  ! data.containsKey("request_id")) {
            throw new RuntimeException("Missing data.");
        }
        else{
            requestId = data.get("request_id");
            Ajax.get(URL_ITEMS_ON_QUEUE_DETAILS+"?request_id="+requestId, callbacks);
        }
    }

    public static void approveQueuedItem(Map<String, String> data, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey("request_id") ||
                !data.containsKey("item_id") ||
                !data.containsKey("activity_category")) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_APPROVE_SELL, data, callbacks);
    }

    public static void denyQueuedItem(Map<String, String> data, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey("request_id") ||
                !data.containsKey("item_id")) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_DISAPPROVE_SELL, data, callbacks);
    }

    public static void getDonatedItemDetails (Map<String, String> data, Ajax.Callbacks callbacks) {
        String requestId;
        if (  ! data.containsKey("request_id")) {
            throw new RuntimeException("Missing data.");
        }
        else{
            requestId = data.get("request_id");
            Ajax.get(URL_DONATED_ITEMS_DETAILS+"?request_id="+requestId, callbacks);
        }
    }

    public static void approveDonatedItem(Map<String, String> data, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey("request_id") ||
                !data.containsKey("item_id") ||
                !data.containsKey("activity_category") ||
                !data.containsKey("stars_required")) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_APPROVE_DONATION,data, callbacks);
    }

    public static void denyDonatedItem(Map<String, String> data, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey("request_id") ||
                !data.containsKey("item_id")) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_DISAPPROVE_DONATION,data, callbacks);
    }

    public static void getReservedItemDetails (Map<String, String> data, Ajax.Callbacks callbacks) {
        String requestId;
        if (  ! data.containsKey("request_id")) {
            throw new RuntimeException("Missing data.");
        }
        else{
            requestId = data.get("request_id");
            Ajax.get(URL_RESERVED_ITEMS+"?request_id="+requestId, callbacks);
        }
    }

    public static void itemAvailable(Map<String, String> data, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey("request_id") ||
                !data.containsKey("item_id")) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_ITEM_AVAILABLE,data, callbacks);
    }

    public static void itemClaimed(Map<String, String> data, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey("request_id") ||
                !data.containsKey("item_id")) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_ITEM_CLAIMED,data, callbacks);
    }

    public static void addCategory (Map<String, String> data, Ajax.Callbacks callbacks) {
        if (  ! data.containsKey(AddCategoryActivity.CATEGORY)) {
            throw new RuntimeException("Missing data.");
        }

        Ajax.post(URL_ADD_CATEGORY, data, callbacks);
    }

    public static void getCategories (Ajax.Callbacks callbacks) {
        Ajax.get(URL_CATEGORIES, callbacks);
    }
}
