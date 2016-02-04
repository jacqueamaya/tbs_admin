package citu.teknoybuyandselladmin.models;

import citu.teknoybuyandselladmin.Utils;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SellApproval extends RealmObject{

    @PrimaryKey
    private int id;

    private UserProfile seller;
    private Item item;
    private long request_date;
    private long request_expiration;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserProfile getSeller() {
        return seller;
    }

    public void setSeller(UserProfile seller) {
        this.seller = seller;
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
