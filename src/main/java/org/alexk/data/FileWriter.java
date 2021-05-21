package org.alexk.data;

import lombok.Cleanup;
import lombok.val;
import org.alexk.business.DeliveryService;
import org.alexk.business.models.Order;
import org.alexk.business.models.OrderItem;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public class FileWriter {
    public static void generateBill(Order order) {
        try {
            @Cleanup val out = new PrintWriter(new FileOutputStream(String.format("./bills/Bill_%d.txt", order.getId())));
            val items = DeliveryService.instance.getOrderItems(order);
            val sum = items.stream().mapToDouble(OrderItem::computePrice).sum();

            out.printf("Order No. %d\n", order.getId());
            out.printf("Client: %d, Total price: %f\n\n", order.getClientId(), sum);
            out.printf("Products:\n");
            items.forEach(item -> out.printf("%s, Quantity: %d, Price: %f\n",
                    item.getItem().getTitle(),
                    item.getQuantity(),
                    item.computePrice()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveReport(String path, String content) {
        try {
            @Cleanup val out = new PrintWriter(new FileOutputStream(path));

            if (content.isBlank() || content.isEmpty() || content == null) {
                out.printf("No items found!");
            }
            else {
                out.printf(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
