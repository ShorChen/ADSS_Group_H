package presentation.ui_shared;

import presentation.util.Option;

public class CreateStoreUI extends View{

    public CreateStoreUI() {
        super(null);
    }

    @Override
    public void display() {
        displayMenu(new Option.Builder("--- Init System ---")
                .append("", () -> {}), "");


        /*
        * get manager
        * get store working days
        *
        * */
    }

    @Override
    public void close() {

    }
}
