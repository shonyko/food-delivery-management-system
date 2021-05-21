package org.alexk.business.models.misc;

import lombok.Getter;
import org.alexk.business.models.MenuItem;
import org.alexk.business.models.Order;
import org.alexk.business.models.OrderItem;
import org.alexk.business.models.User;
import org.alexk.business.models.enums.UserType;

import java.util.*;

@Getter
public class SerializeData {
    private List<MenuItem> menuItems;
    private Map<Order, List<OrderItem>> orders;
    private List<User> accounts;

    public SerializeData() {
        this(   new ArrayList<>(),
                new HashMap<>(),
                new ArrayList<>() {{
                        add(new User("admin", "admin", UserType.ADMIN));
                        add(new User("employee", "employee", UserType.EMPLOYEE));
                }}
        );
    }

    public SerializeData(List<MenuItem> menuItems, Map<Order, List<OrderItem>> orders, List<User> accounts) {
        this.menuItems = menuItems;
        this.orders = orders;
        this.accounts = accounts;
    }

    public static SerializeData getClonedData(List<MenuItem> menuItems, Map<Order, List<OrderItem>> orders, List<User> accounts) {
        return new SerializeData(new ArrayList<>(menuItems), new HashMap<>(orders), new ArrayList<>(accounts));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SerializeData that = (SerializeData) o;

        if (!Objects.equals(menuItems, that.menuItems)) return false;
        if (!Objects.equals(orders, that.orders)) return false;
        return Objects.equals(accounts, that.accounts);
    }

    @Override
    public int hashCode() {
        int result = menuItems != null ? menuItems.hashCode() : 0;
        result = 31 * result + (orders != null ? orders.hashCode() : 0);
        result = 31 * result + (accounts != null ? accounts.hashCode() : 0);
        return result;
    }
}
