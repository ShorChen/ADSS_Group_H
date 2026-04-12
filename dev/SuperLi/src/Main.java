public class Main {
import domain.HRFacade;
import presentation.HRManagerUI;
    public static void main(String[] args) {
        HRFacade hrSystem = new HRFacade();

        hrSystem.initSystemData();
        
        HRManagerUI ui = new HRManagerUI(hrSystem);
        ui.start();
        
    }
}