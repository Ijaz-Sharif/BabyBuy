package com.example.babybuy.Model;

public class Item {
    String name,quantity,price,itemId,userId,status;
    byte[] image;

    public Item(String quantity,String name ,String price, byte[] image,String userId,String status,String itemId) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
        this.itemId=itemId;
        this.userId=userId;
        this.status=status;
    }
    public Item(String name, String quantity, String price,String userId) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.userId=userId;
    }

    public String getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getItemId() {
        return itemId;
    }

    public byte[] getImage() {
        return image;
    }
}
