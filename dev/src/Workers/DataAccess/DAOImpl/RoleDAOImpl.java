package Workers.DataAccess.DAOImpl;

import Workers.DataAccess.DAO.RoleDAO;

import java.util.List;

public class RoleDAOImpl implements RoleDAO {
    @Override
    public boolean containsRole(String role) {
        return false;
    }

    @Override
    public void addRole(String role) {

    }

    @Override
    public List<String> getAllRoles() {
        return List.of();
    }
}
