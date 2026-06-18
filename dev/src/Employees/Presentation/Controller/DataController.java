package Employees.Presentation.Controller;

import Employees.Domain.Service.DataService;

public class DataController {
    private final DataService service;

    public DataController(DataService service) {
        this.service = service;
    }
}
