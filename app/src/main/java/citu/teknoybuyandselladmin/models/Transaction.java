package citu.teknoybuyandselladmin.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Transaction extends RealmObject{

    @PrimaryKey
    private int id;
    private String item_name;
    private String item_code;
    private String buyer;
    private String seller;
    private String transaction_type;
    private long date_claimed;
    private float total_payment;
    private float user_share;
    private float tbs_share;

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public long getDate_claimed() {
        return date_claimed;
    }

    public void setDate_claimed(long date_claimed) {
        this.date_claimed = date_claimed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public float getTotal_payment() {
        return total_payment;
    }

    public void setTotal_payment(float total_payment) {
        this.total_payment = total_payment;
    }

    public float getUser_share() {
        return user_share;
    }

    public void setUser_share(float user_share) {
        this.user_share = user_share;
    }

    public float getTbs_share() {
        return tbs_share;
    }

    public void setTbs_share(float tbs_share) {
        this.tbs_share = tbs_share;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }
}
