package edu.csun.campusharvest;

public class GroceryListItem {
    private MarketListItem marketItem;
    private float price;
    private int quantity;
    private boolean checked = false;

    public String getName() {
        return marketItem.getName();
    }

    public void setName(String itemName) {
        marketItem.setName(itemName);
    }

    public float getPrice() {
        return quantity*toInt(marketItem.getPrice());
    }

    private float toInt(String unitPrice) {
        String subString = unitPrice.substring(1,unitPrice.indexOf("/"));
        price = Float.parseFloat(subString);
        return price;
    }

    public void setPrice(int itemPrice) {
       price = itemPrice;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public String getCategory() {
        return marketItem.getCategory();
    }

    public void setCategory(String category) {
        marketItem.setCategory(category);
    }

    public MarketListItem getMarketItem(){
        return marketItem;
    }
    public void setMarketItem(MarketListItem marketItem){
        this.marketItem = marketItem;
    }

    public String getUnits() {
        String strPrice = marketItem.getPrice();
        String units = strPrice.substring(strPrice.indexOf("/") + 1, strPrice.length());

        return units;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}