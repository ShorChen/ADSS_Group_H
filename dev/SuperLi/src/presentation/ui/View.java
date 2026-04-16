package presentation.ui;

import java.util.Scanner;

public abstract class View {
    public static Scanner reader = new Scanner(System.in);

    abstract void display();

    abstract void close();

    public String getNextLine(String message) {
        System.out.println(message);
        return reader.nextLine();
    }

    public int getNextInteger(String message) {
        System.out.println(message);
        int result = reader.nextInt();
        reader.nextLine();
        return result;
    }

    public double getNextDouble(String message) {
        System.out.println(message);
        double result = reader.nextDouble();
        reader.nextLine();
        return result;
    }

    protected void handleSelection(int selection, Runnable... selections) {
        if (selection >= selections.length || selection < 0) {
            System.out.println("An Option With Index " + selection + " Does Not Exist.");
        } else if (selections[selection] != null) {
            selections[selection].run();
        }
    }
}
