package Workers.DataAccess.DAO;

import java.util.List;

public interface StoreDAO {
    List<String> getClosedDays();

    void setClosedDays(List<String> closedDays);

    boolean isFirstStartUp();

    void setFirstStartUp(boolean firstStartUp);
}
