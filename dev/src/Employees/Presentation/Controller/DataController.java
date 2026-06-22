package Employees.Presentation.Controller;

import Employees.Service.DataService;

public class DataController {
    private final DataService service;

    public DataController(DataService service) {
        this.service = service;
    }
}
