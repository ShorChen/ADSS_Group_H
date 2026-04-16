package Suppliers.DataAccess;

import Suppliers.Domain.SupplierDL;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileSupplierDAO implements SupplierDAO {
    private final String filePath;

    public FileSupplierDAO(String filePath) {
        this.filePath = filePath;
    }

    @SuppressWarnings("unchecked")
    private Map<String, SupplierDL> readAll() {
        File file = new File(filePath);
        if (!file.exists()) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, SupplierDL>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data from file", e);
        }
    }

    private void writeAll(Map<String, SupplierDL> data) {
        File file = new File(filePath);
        if (file.getParentFile() != null)
            file.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file", e);
        }
    }

    @Override
    public void addSupplier(SupplierDL supplier) {
        Map<String, SupplierDL> data = readAll();
        data.put(supplier.getBusinessNumber(), supplier);
        writeAll(data);
    }

    @Override
    public void updateSupplier(SupplierDL supplier) {
        Map<String, SupplierDL> data = readAll();
        data.put(supplier.getBusinessNumber(), supplier);
        writeAll(data);
    }

    @Override
    public void deleteSupplier(String businessNumber) {
        Map<String, SupplierDL> data = readAll();
        data.remove(businessNumber);
        writeAll(data);
    }

    @Override
    public SupplierDL getSupplier(String businessNumber) {
        return readAll().get(businessNumber);
    }

    @Override
    public Map<String, SupplierDL> getAllSuppliers() {
        return readAll();
    }
}