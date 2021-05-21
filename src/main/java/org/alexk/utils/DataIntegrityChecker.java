package org.alexk.utils;

import lombok.val;
import org.alexk.business.models.MenuItem;
import org.alexk.business.models.Order;
import org.alexk.business.models.OrderItem;
import org.alexk.business.models.User;

import java.util.List;
import java.util.Map;

public class DataIntegrityChecker {
    private DataIntegrityChecker() {}

    private static boolean menuItemsWellFormed(List<MenuItem> menuItems) {
        if(menuItems == null) {
            return false;
        }
        for(val item : menuItems) {
            if(item == null || !item.isWellFormed()) {
                return false;
            }
        }

        return true;
    }
    private static boolean ordersWellFormed(Map<Order, List<OrderItem>> orders) {
        if(orders == null) {
            return false;
        }
        for(val entry : orders.entrySet()) {
            val order = entry.getKey();
            if(order == null || !order.isWellFormed()) {
                return false;
            }
            val itemList = entry.getValue();
            if(itemList == null || itemList.size() <= 0) {
                return false;
            }
            for(val item : itemList) {
                if(item == null || !item.isWellFormed()) {
                    return false;
                }
            }
        }

        return true;
    }
    private static boolean accountsWellFormed(List<User> accounts) {
        if(accounts == null) {
            return false;
        }
        for(val acc : accounts) {
            if(acc == null || !acc.isWellFormed()) {
                return false;
            }
        }
        return true;
    }
    public static boolean isWellFormed(List<MenuItem> menuItems, Map<Order, List<OrderItem>> orders, List<User> accounts) {
        return  menuItemsWellFormed(menuItems) &&
                ordersWellFormed(orders) &&
                accountsWellFormed(accounts);
    }
}
