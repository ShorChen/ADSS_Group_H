package Suppliers.DataAccess;

import Suppliers.Domain.Entities.SupplierDL;

import java.util.Map;

public interface SupplierDAO {
    void addSupplier(SupplierDL supplier);

    void updateSupplier(SupplierDL supplier);

    void deleteSupplier(String businessNumber);

    @SuppressWarnings("unused")
    SupplierDL getSupplier(String businessNumber);

    Map<String, SupplierDL> getAllSuppliers();
}