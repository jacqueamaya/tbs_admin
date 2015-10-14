package citu.teknoybuyandselladmin.models;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notification {
    private static final String TAG = "Notification";
    private String ownerUsername;
    private String makerUsername;
    private String itemName;
    private String notification_type;
    private String notification_date;
    private String itemLink;
    private String status;

    private long id;

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public String getMakerUsername() {
        return makerUsername;
    }

    public String getItemName() {
        return itemName;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public String getNotification_date() {
        return notification_date;
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

    public static Notification getNotification(JSONObject jsonObject){
        Notification n = new Notification();
        JSONObject item,owner,owner_student,maker;

        try {
            n.id =  jsonObject.getInt("id");
            n.notification_type=jsonObject.getString("notification_type");
            n.notification_date = jsonObject.getString("notification_date");
            n.status = jsonObject.getString("status");

            if(!jsonObject.isNull("item")){
                item = jsonObject.getJSONObject("item");

                n.itemName = item.getString("name");
                n.itemLink = item.getString("picture");
                if(!item.isNull("owner")){
                    owner = item.getJSONObject("owner");

                    if(!owner.isNull("user")){
                        owner_student = owner.getJSONObject("user");

                        n.ownerUsername = owner_student.getString("username");
                    }
                }
            }
            if(!jsonObject.isNull("maker")){
                maker = jsonObject.getJSONObject("maker");
                n.makerUsername = maker.getString("username");
                Log.v(TAG,maker.getString("username"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return n;
    }

    public static ArrayList<Notification> allNotifications(JSONArray jsonArray) {
        ArrayList<Notification> notifications = new ArrayList<Notification>(jsonArray.length());
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject notificationObject = null;
            try {
                notificationObject = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Notification notification = Notification.getNotification(notificationObject);
            if (notification != null) {
                notifications.add(notification);
            }
        }

        return notifications;
    }
}
