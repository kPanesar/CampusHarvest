package edu.csun.campusharvest;

public class MarketListItem {
    private String itemName;
    private String itemPrice;
    private String category;

    public String getName() {
        return itemName;
    }

    public void setName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return itemPrice;
    }

    public void setPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}