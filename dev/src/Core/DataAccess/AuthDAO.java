package Core.DataAccess;

import Core.Domain.Managers;

import java.util.Map;

@SuppressWarnings("unused")
public interface AuthDAO {
    void addCode(String code, Managers managers);

    void removeCode(String code);

    void updateCode(String code, Managers managers);

    Managers getRole(String code);

    Map<String, Managers> getAllCodes();
}