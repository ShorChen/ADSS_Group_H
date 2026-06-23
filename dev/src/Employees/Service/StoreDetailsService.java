package Employees.Service;

import Employees.DataAccess.StoreDAO;
import Employees.DataAccess.SqlImpl.SqlStoreDAO;
import Employees.Presentation.DTO.StoreDetailsPL;
import Employees.Shared.Enums.WeekDay;

import java.util.ArrayList;
import java.util.List;

public class StoreDetailsService {
    private final StoreDAO storeDAO;

    public StoreDetailsService() {
        this.storeDAO = new SqlStoreDAO();
    }

    public void addUpdateStoreDetails(StoreDetailsPL storeDetailsPL) {
        List<String> closedDays = new ArrayList<>();
        storeDetailsPL.getClosedDays().forEach(day ->
                closedDays.add(day.toString())
        );
        storeDAO.setClosedDays(closedDays);
    }

    public void setClosedDays(List<String> closed) {
        storeDAO.setClosedDays(closed);
    }

    public List<WeekDay> getClosedDays() {
        List<WeekDay> closedDays = new ArrayList<>();
        storeDAO.getClosedDays().forEach(s
                -> closedDays.add(WeekDay.fromArgs(s)));
        return closedDays;
    }
}