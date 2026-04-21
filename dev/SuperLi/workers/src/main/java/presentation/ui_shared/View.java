package presentation.ui_shared;

import context.SessionManager;
import presentation.util.Option;

import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class View {
    protected static Scanner reader = new Scanner(System.in);
    protected Runnable onDismiss;

    public View(Runnable onDismiss) {
        this.onDismiss = () -> {
            close();
            if (onDismiss != null)
                onDismiss.run();
        };
    }


    public abstract void display();

    public abstract void close();

    public String getNextLine(String message) {
        while (true) {
            System.out.println(message);
            String s = reader.nextLine();
            if (!s.trim().isEmpty()) return s;
            System.out.println("Input must be non empty, try again");
        }
    }

    public int getNextInteger(String message) {
        while (true) {
            try {
                System.out.println(message);
                int result = reader.nextInt();
                reader.nextLine();
                return result;
            } catch (InputMismatchException e) {
                System.out.println("Expected an integer number, try again");
                reader.nextLine();
            }
        }

    }

    public double getNextDouble(String message) {
        while (true) {
            try {
                System.out.println(message);
                double result = reader.nextDouble();
                reader.nextLine();
                return result;
            } catch (InputMismatchException e) {
                System.out.println("Expected a decimal number, try again");
                reader.nextLine();
            }
        }


    }

    public boolean getNextBoolean(String message) {
        while (true) {
            System.out.println(message);
            String bool = reader.nextLine();
            if (bool.equalsIgnoreCase("y")) return true;
            if (bool.equalsIgnoreCase("n")) return false;
            System.out.println("Answer must be y or n, try again");
        }
    }

    protected void displayMenu(Option.Builder options, String endMessage) {
        if (SessionManager.isDebugMode()) debug(options);
        if (options == null || options.size() == 0)
            throw new IllegalArgumentException("Menu should have at least 1 option");
        System.out.println(options.getMessage());
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i) != null)
                s.append(i).append(". ").append(options.get(i).getName()).append("\n");
        }
        System.out.print(s);
        if (!endMessage.isEmpty())
            System.out.println(endMessage);
        int selection = getNextInteger("Select Option (number): ");
        while (selection >= options.size() || selection < 0) {
            System.out.println("An Option With Index " + selection + " Does Not Exist. try again");
            selection = getNextInteger("Select Option (number): ");
        }
        if (options.get(selection).getAction() != null)
            options.get(selection).getAction().run();
    }


    private void debug(Option.Builder options) {
        options.append("Move time (DEBUGGING)", () -> {
            int hours = getNextInteger("Move hours forward");
            SessionManager.debug(hours);
            System.out.println("Set time to " + SessionManager.now());
        });
    }

}
