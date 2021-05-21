package org.alexk.business.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private final MenuItem item;
    private final int quantity;

    public OrderItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public double computePrice() {
        return item.computePrice() * quantity;
    }

    public boolean isWellFormed() {
        if(item == null || !item.isWellFormed()) {
            return false;
        }
        if(quantity <= 0) {
            return false;
        }

        return true;
    }
}
