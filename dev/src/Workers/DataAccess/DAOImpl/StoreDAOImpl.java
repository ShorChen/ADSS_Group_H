package Workers.DataAccess.DAOImpl;

import Workers.DataAccess.DAO.StoreDAO;
import Workers.DataAccess.Entities.Keys.WeekKey;

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
