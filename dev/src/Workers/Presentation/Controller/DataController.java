package Workers.Presentation.Controller;

import Workers.Service.DataService;

public class DataController {
    private final DataService service;

    public DataController(DataService service) {
        this.service = service;
    }
}
