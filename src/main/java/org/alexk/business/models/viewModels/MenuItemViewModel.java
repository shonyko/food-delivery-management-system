package org.alexk.business.models.viewModels;

import lombok.Data;
import org.alexk.business.models.MenuItem;
import org.alexk.business.models.OrderItem;

@Data
public class MenuItemViewModel {
    private int id;

    private String title;
    private double rating;
    private double calories;
    private double protein;
    private double fat;
    private double sodium;
    private double price;

    private double basePrice;
    private int quantity;

    public MenuItemViewModel(int id, MenuItem item) {
        mapFromItem(item);
        this.id = id;
    }

    public MenuItemViewModel(OrderItem orderItem) {
        var item = orderItem.getItem();
        mapFromItem(item);
        this.quantity = orderItem.getQuantity();
        updatePrice();
    }

    private void mapFromItem(MenuItem item) {
        this.id = item.getId();
        quantity = 1;
        basePrice = item.computePrice();

        title = item.getTitle();
        rating = item.getRating();
        calories = item.getCalories();
        protein = item.getProtein();
        fat = item.getFat();
        sodium = item.getSodium();
        price = item.computePrice();
    }

    public void updatePrice() {
        price = basePrice * quantity;
    }
}
