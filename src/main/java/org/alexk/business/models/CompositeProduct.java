package org.alexk.business.models;

import lombok.Data;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompositeProduct extends MenuItem {
    private static final long serialVersionUID = 1L;

    private List<MenuItem> items;

    public CompositeProduct() {
        items = new ArrayList<>();
    }

    public CompositeProduct(String title, double rating, List<MenuItem> menuItems) {
        super(title, rating);
        items = menuItems;
    }

    @Override
    public double getCalories() {
        return items.stream().mapToDouble(MenuItem::getCalories).sum();
    }

    @Override
    public double getProtein() {
        return items.stream().mapToDouble(MenuItem::getProtein).sum();
    }

    @Override
    public double getFat() {
        return items.stream().mapToDouble(MenuItem::getFat).sum();
    }

    @Override
    public double getSodium() {
        return items.stream().mapToDouble(MenuItem::getSodium).sum();
    }

    @Override
    public double computePrice() {
        return items.stream().mapToDouble(MenuItem::computePrice).sum();
    }

    @Override
    public boolean isWellFormed() {
        if(getTitle().isBlank() || getTitle().isEmpty()) {
            return false;
        }
        if(getRating() < 0) {
            return false;
        }
        if(items == null || items.size() == 0) {
            return false;
        }
        for(val item : items) {
            if(item == null || !item.isWellFormed()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeProduct that = (CompositeProduct) o;

        for(int i = 0; i < items.size(); i++) {
            if(!that.getItems().get(i).equals(items.get(i))) {
                return false;
            }
        }

        return  super.equals(that);
    }
}
