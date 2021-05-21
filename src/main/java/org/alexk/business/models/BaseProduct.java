package org.alexk.business.models;

import lombok.Data;


@Data
public class BaseProduct extends MenuItem {
    private static final long serialVersionUID = 1L;

    private double calories;
    private double protein;
    private double fat;
    private double sodium;
    private double price;

    public BaseProduct() {}

    @Override
    public double computePrice() {
        return price;
    }

    @Override
    public boolean isWellFormed() {
        if(getTitle().isBlank() || getTitle().isEmpty()) {
            return false;
        }
        if(getRating() < 0 || calories < 0 || protein < 0 || fat < 0 || sodium < 0 || price < 0) {
            return false;
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseProduct that = (BaseProduct) o;
        return  super.equals(that) &&
                Double.compare(that.calories, calories) == 0 &&
                Double.compare(that.protein, protein) == 0 &&
                Double.compare(that.fat, fat) == 0 &&
                Double.compare(that.sodium, sodium) == 0 &&
                Double.compare(that.price, price) == 0;
    }
}
