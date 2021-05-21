package org.alexk.utils;

import lombok.val;
import org.alexk.business.models.MenuItem;
import org.alexk.business.models.Order;
import org.alexk.business.models.OrderItem;
import org.alexk.business.models.User;
import org.alexk.business.models.enums.UserType;
import org.alexk.business.models.misc.Wrapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportGenerator {
    private ReportGenerator() {
    }

    public static String generateReportBetweenHours(List<Order> orders, LocalTime startHour, LocalTime endHour) {
        val sb = new StringBuilder();

        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
        orders.stream().filter(x -> {
            val time = LocalDateTime.parse(x.getDate(), formatter).toLocalTime();
            return !(time.isBefore(startHour) || time.isAfter(endHour));
        }).forEach(sb::append);

        return sb.toString();
    }
    private static int productQuantity(List<Order> orders, Map<Order, List<OrderItem>> ordersMap, MenuItem item) {
        val mockItem = new OrderItem(null, 0);
        return orders.stream()
                .map(ordersMap::get)
                .mapToInt(list -> list.stream()
                        .filter(x -> x.getItem().equals(item))
                        .findFirst().orElse(mockItem)
                        .getQuantity()
                ).sum();
    }
    private static int productQuantity(Map<Order, List<OrderItem>> ordersMap, MenuItem item) {
        return productQuantity(new ArrayList<>(ordersMap.keySet()), ordersMap, item);
    }
    public static String generateReportProducts(List<MenuItem> menuItems, Map<Order, List<OrderItem>> orders, int times) {
        val sb = new StringBuilder();

        menuItems.stream()
        .map(item -> {
            val quantity = productQuantity(orders, item);
            return new Wrapper(item, quantity);
        })
        .filter(x -> x.getFrequency() >= times)
        .forEach(sb::append);

        return sb.toString();
    }
    public static String  generateReportClientSum(List<User> accounts, Map<Order, List<OrderItem>> ordersMap, int minNoOrders, double minSum) {
        val sb = new StringBuilder();

        val clientsStream = accounts.stream()
                .filter(x -> x.getType().equals(UserType.CUSTOMER))
                .filter(client -> ordersMap.keySet().stream()
                        .mapToInt(x -> x.getClientId() == accounts.indexOf(client) ? 1 : 0)
                        .sum() >= minNoOrders
                ).collect(Collectors.toList());

        ordersMap.keySet().stream()
                .filter(order -> ordersMap.get(order).stream()
                        .mapToDouble(OrderItem::computePrice)
                        .sum() >= minSum
                ).filter(order -> clientsStream.stream()
                .anyMatch(client -> client.equals(accounts.get(order.getClientId())))
        ).forEach(sb::append);

        return sb.toString();
    }
    public static String generateReportProductsInDay(List<MenuItem> menuItems, Map<Order, List<OrderItem>> ordersMap, LocalDate date) {
        val sb = new StringBuilder();

        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a");
        val ordersFiltered = ordersMap.keySet().stream().filter(x -> {
            val orderDate = LocalDateTime.parse(x.getDate(), formatter).toLocalDate();
            return orderDate.equals(date);
        }).collect(Collectors.toList());

        menuItems.stream().map(item -> {
            val frequency = productQuantity(ordersFiltered, ordersMap, item);
            return new Wrapper(item, frequency);
        })
        .filter(x -> x.getFrequency() > 0)
        .forEach(sb::append);

        return sb.toString();
    }
}