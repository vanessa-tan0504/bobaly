//order class
package com.example.bobaly;

public class Order {
    private String title,id;
    private double price;
    private int sweetLvl,qty;
    private boolean boba,redbean,grass,pudding,order_status;
    private String user;

    public Order(){
        //empty constructor do not delete
    }

    public Order(String title, double price, int sweetLvl, int qty, boolean boba, boolean redbean, boolean grass, boolean pudding,String id,String user,boolean order_status) {
        this.title = title;
        this.price = price;
        this.sweetLvl = sweetLvl;
        this.qty = qty;
        this.boba = boba;
        this.redbean = redbean;
        this.grass = grass;
        this.pudding = pudding;
        this.id=id;
        this.user=user;
        this.order_status=order_status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSweetLvl() {
        return sweetLvl;
    }

    public void setSweetLvl(int sweetLvl) {
        this.sweetLvl = sweetLvl;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isBoba() {
        return boba;
    }

    public void setBoba(boolean boba) {
        this.boba = boba;
    }

    public boolean isRedbean() {
        return redbean;
    }

    public void setRedbean(boolean redbean) {
        this.redbean = redbean;
    }

    public boolean isGrass() {
        return grass;
    }

    public void setGrass(boolean grass) {
        this.grass = grass;
    }

    public boolean isPudding() {
        return pudding;
    }

    public void setPudding(boolean pudding) {
        this.pudding = pudding;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean getOrder_status() {
        return order_status;
    }

    public void setOrder_status(boolean order_status) {
        this.order_status = order_status;
    }
}
