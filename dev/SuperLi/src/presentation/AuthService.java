package presentation;

import domain.AuthFacade;
import domain.Employee;

import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private AuthFacade facade;
    private List<Employee> employees;
    public AuthService(AuthFacade facade){
        employees = new ArrayList<>();
        this.facade = facade;
    }
    public boolean login(String username, String password){
        return facade.login(username, password);
    }
    public boolean logout(){
        return facade.logout();
    }
}
