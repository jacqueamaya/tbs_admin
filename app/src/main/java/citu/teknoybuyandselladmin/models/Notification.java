package citu.teknoybuyandselladmin.models;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notification {

    private static final String TAG = "Notification";

    public static final String ITEM = "item";
    public static final String OWNER = "owner";
    public static final String USER = "user";
    public static final String MAKER = "maker";
    public static final String ID = "id";
    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final String NOTIFICATION_DATE = "notification_date";
    public static final String STATUS = "status";
    public static final String NAME = "name";
    public static final String PICTURE = "picture";
    public static final String USERNAME = "username";
    public static final String PURPOSE = "purpose";

    private String ownerUsername;
    private String makerUsername;
    private String itemName;
    private String itemPurpose;
    private String notificationType;
    private String itemLink;
    private String status;

    private long id;

    public String getItemPurpose() {
        return itemPurpose;
    }

    private long notificationDate;

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public String getMakerUsername() {
        return makerUsername;
    }

    public String getItemName() {
        return itemName;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public long getNotificationDate() {
        return notificationDate;
    }

    public String getItemLink() {
        return itemLink;
    }

    public String getStatus() {
        return status;
    }

    public long getId() {
        return id;
    }

    public static Notification asSingle(JSONObject jsonObject) {
        Notification n = new Notification();
        JSONObject item, owner, ownerStudent, maker;

        try {
            item = jsonObject.getJSONObject(ITEM);
            owner = item.getJSONObject(OWNER);
            ownerStudent = owner.getJSONObject(USER);
            maker = jsonObject.getJSONObject(MAKER);

            n.id = jsonObject.getLong(ID);
            n.notificationType = jsonObject.getString(NOTIFICATION_TYPE);
            n.notificationDate = jsonObject.getLong(NOTIFICATION_DATE);
            n.status = jsonObject.getString(STATUS);
            n.itemName = item.getString(NAME);
            n.itemLink = item.getString(PICTURE);
            n.itemPurpose = item.getString(PURPOSE);
            n.ownerUsername = ownerStudent.getString(USERNAME);
            n.makerUsername = maker.getString(USERNAME);
        } catch (JSONException e) {
            Log.e(TAG, "Cannot parse JSON OR Error getting fields", e);
        }

        return n;
    }

    public static ArrayList<Notification> asList(JSONArray jsonArray) {
        ArrayList<Notification> notifications = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject notificationObject = jsonArray.getJSONObject(i);
                Notification notification = Notification.asSingle(notificationObject);
                notifications.add(notification);
            } catch (JSONException e) {
                Log.e(TAG, "JSONException on item#" + i, e);
            }
        }

        return notifications;
    }
}
