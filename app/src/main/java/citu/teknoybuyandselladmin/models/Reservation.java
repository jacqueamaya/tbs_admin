package citu.teknoybuyandselladmin.models;


import citu.teknoybuyandselladmin.Utils;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Reservation extends RealmObject{

    @PrimaryKey
    private int id;
    private UserProfile buyer;
    private Item item;
    private long reserved_date;
    private long reserved_expiration;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserProfile getBuyer() {
        return buyer;
    }

    public void setBuyer(UserProfile buyer) {
        this.buyer = buyer;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public long getReserved_date() {
        return reserved_date;
    }

    public void setReserved_date(long reserved_date) {
        this.reserved_date = reserved_date;
    }

    public long getReserved_expiration() {
        return reserved_expiration;
    }

    public void setReserved_expiration(long reserved_expiration) {
        this.reserved_expiration = reserved_expiration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
