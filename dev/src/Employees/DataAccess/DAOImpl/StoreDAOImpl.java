package Employees.DataAccess.DAOImpl;

import Employees.DataAccess.StoreDAO;

import java.util.List;

public class StoreDAOImpl implements StoreDAO {
    @Override
    public List<String> getClosedDays() {
        return List.of();
    }

    @Override
    public void setClosedDays(List<String> closedDays) {

    }

    @Override
    public boolean isFirstStartUp() {
        return false;
    }

    @Override
    public void setFirstStartUp(boolean firstStartUp) {

    }
}
