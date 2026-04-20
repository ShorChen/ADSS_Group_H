package Suppliers.DataAccess.FileImpl;

import Suppliers.DataAccess.OrderDAO;
import Suppliers.Domain.Entities.OrderDL;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ClassCanBeRecord")
public class FileOrderDAO implements OrderDAO {
    private final String filePath;

    public FileOrderDAO(String filePath) {
        this.filePath = filePath;
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, OrderDL> readAll() {
        File file = new File(filePath);
        if (!file.exists()) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<Integer, OrderDL>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data from file", e);
        }
    }

    private void writeAll(Map<Integer, OrderDL> data) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs())
            throw new RuntimeException("Failed to create parent directories for: " + filePath);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file", e);
        }
    }

    @Override
    public void addOrder(OrderDL order) {
        Map<Integer, OrderDL> data = readAll();
        data.put(order.getOrderId(), order);
        writeAll(data);
    }

    @Override
    public Map<Integer, OrderDL> getAllOrders() {
        return readAll();
    }
}