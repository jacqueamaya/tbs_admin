package citu.teknoybuyandselladmin.models;


import citu.teknoybuyandselladmin.Utils;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Reservation extends RealmObject{

    @PrimaryKey
    private int id;
    private User buyer;
    private Item item;

    private long reserved_date;
    private long reserved_expiration;

    private float payment;

    private int quantity;
    private int stars_to_use;


    private String status;
    private String item_code;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
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

    public float getPayment() {
        return payment;
    }

    public void setPayment(float payment) {
        this.payment = payment;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStars_to_use() {
        return stars_to_use;
    }

    public void setStars_to_use(int stars_to_use) {
        this.stars_to_use = stars_to_use;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }
}
