package presentation;

import java.util.Scanner;
import domain.HRFacade;

public class HRManagerUI {
    private HRFacade facade;
    private Scanner scanner;

    public HRManagerUI(HRFacade facade) {
        this.facade = facade;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        // Initialize system data
    }
}
