package presentation.control;

import domain.services.FirstScreenService;

public class FirstScreenController {
    private final FirstScreenService service;

    public FirstScreenController() {
        service = new FirstScreenService();
    }

    public boolean isFirstStarUp(){
        return service.isFirstStartUp();
    }

    public void setIsFirstStartUpAsFalse() {
        service.setFirstStartUpAsFalse();
    }
}
