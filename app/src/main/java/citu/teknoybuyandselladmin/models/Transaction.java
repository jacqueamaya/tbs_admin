package citu.teknoybuyandselladmin.models;

public class Transaction {

    private int id;
    private Item item;
    private UserProfile buyer;
    private UserProfile seller;
    private long date_claimed;

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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public UserProfile getBuyer() {
        return buyer;
    }

    public void setBuyer(UserProfile buyer) {
        this.buyer = buyer;
    }

    public UserProfile getSeller() {
        return seller;
    }

    public void setSeller(UserProfile seller) {
        this.seller = seller;
    }





}
