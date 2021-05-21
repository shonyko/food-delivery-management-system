package org.alexk.data;

import lombok.Cleanup;
import lombok.val;
import org.alexk.business.models.*;
import org.alexk.business.models.misc.SerializeData;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Serializator {
    private static String path = "./data/serialized.data";

    public static SerializeData deserialize() throws Exception {
        return deserialize(path);
    }

    public static SerializeData deserialize(String path) throws Exception {
        val file = new File(path);
        if(!file.exists()) {
            return new SerializeData();
        }

        @Cleanup val fileInputStream = new FileInputStream(file);
        @Cleanup val in = new ObjectInputStream(fileInputStream);

        val nextId = (int) in.readObject();
        val menuItems = (ArrayList<MenuItem>) in.readObject();
        val orders = (HashMap<Order, List<OrderItem>>) in.readObject();
        val accounts = (ArrayList<User>) in.readObject();

        MenuItem.setNextId(nextId);
        return new SerializeData(menuItems, orders, accounts);
    }

    public static void serialize(SerializeData data) throws Exception {
        serialize(path, data);
    }

    public static void serialize(String path, SerializeData data) throws Exception {
        val file = new File(path);
        if(!file.exists()) {
            file.createNewFile();
        }

        @Cleanup val fileOutputStream = new FileOutputStream(file);
        @Cleanup val out = new ObjectOutputStream(fileOutputStream);

        out.writeObject(MenuItem.getNextId());
        out.writeObject(data.getMenuItems());
        out.writeObject(data.getOrders());
        out.writeObject(data.getAccounts());
    }
}
