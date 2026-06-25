package Employees.DataAccess;
import Employees.Domain.Entities.StoreDetailsDL;

public interface StoreDAO {
    StoreDetailsDL getStoreDetails();
    void updateStoreDetails(StoreDetailsDL details);
}