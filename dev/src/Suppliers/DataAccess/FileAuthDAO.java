package Suppliers.DataAccess;

import Suppliers.Domain.Role;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileAuthDAO implements AuthDAO {
    private final String filePath;

    public FileAuthDAO(String filePath) {
        this.filePath = filePath;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Role> readAll() {
        File file = new File(filePath);
        if (!file.exists()) {
            Map<String, Role> defaultCodes = new HashMap<>();
            defaultCodes.put("SUP123", Role.SUPPLIER_MANAGER);
            defaultCodes.put("ORD123", Role.ORDER_MANAGER);
            writeAll(defaultCodes);
            return defaultCodes;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Role>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data from file", e);
        }
    }

    private void writeAll(Map<String, Role> data) {
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
    public void addCode(String code, Role role) {
        Map<String, Role> data = readAll();
        data.put(code, role);
        writeAll(data);
    }

    @Override
    public void removeCode(String code) {
        Map<String, Role> data = readAll();
        data.remove(code);
        writeAll(data);
    }

    @Override
    public void updateCode(String code, Role role) {
        Map<String, Role> data = readAll();
        data.put(code, role);
        writeAll(data);
    }

    @Override
    public Role getRole(String code) {
        return readAll().get(code);
    }

    @Override
    public Map<String, Role> getAllCodes() {
        return readAll();
    }
}