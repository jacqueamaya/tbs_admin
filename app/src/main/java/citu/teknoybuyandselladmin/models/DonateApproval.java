package citu.teknoybuyandselladmin.models;

import android.util.Log;

import com.bumptech.glide.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import citu.teknoybuyandselladmin.Utils;

/**
 * Created by Batistil on 9/20/2015.
 */
public class DonateApproval {

    private int id;
    private UserProfile donor;
    private Item item;
    private long request_date;
    private long request_expiration;
    private String str_request_date = Utils.parseDate(request_date);

    public String getStr_request_date() {
        return str_request_date;
    }

   /* public void setStr_request_date(long request_date) {
        str_request_date = Utils.parseDate(request_date);
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserProfile getDonor() {
        return donor;
    }

    public void setDonor(UserProfile donor) {
        this.donor = donor;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public long getRequest_date() {
        return request_date;
    }

    public void setRequest_date(long request_date) {
        this.request_date = request_date;
    }

    public long getRequest_expiration() {
        return request_expiration;
    }

    public void setRequest_expiration(long request_expiration) {
        this.request_expiration = request_expiration;
    }
}
