package Suppliers.DataAccess;

import Suppliers.Domain.SupplierDL;
import java.util.Map;

public interface SupplierDAO {
    void addSupplier(SupplierDL supplier);
    void updateSupplier(SupplierDL supplier);
    void deleteSupplier(String businessNumber);
    SupplierDL getSupplier(String businessNumber);
    Map<String, SupplierDL> getAllSuppliers();
}