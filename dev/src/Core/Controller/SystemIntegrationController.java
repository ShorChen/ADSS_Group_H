package Core.Controller;

import Core.Service.Response;
import Core.Service.SystemIntegrationService;

public class SystemIntegrationController {
    private final SystemIntegrationService integrationService;

    public SystemIntegrationController(SystemIntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    public void executeDailyIntegration() {
        Response<Boolean> response = integrationService.executeDailyIntegration();
        if (!response.isSuccess()) throw new RuntimeException(response.getErrorMessage());
    }
}