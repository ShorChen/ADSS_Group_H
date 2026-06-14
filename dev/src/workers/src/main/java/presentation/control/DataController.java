package presentation.control;

import domain.services.DataService;

public class DataController {
    private final DataService service;

    public DataController(DataService service) {
        this.service = service;
    }
}
