package presentation.ui_shared;

import presentation.util.Option;

import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class View {
    protected static Scanner reader = new Scanner(System.in);

    public abstract void display();

    public abstract void close();

    public String getNextLine(String message) {
        System.out.println(message);
        return reader.nextLine();
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

    protected void displayMenu(String message, String endMessage, Option... options) {
        if (options == null || options.length == 0)
            throw new IllegalArgumentException("Menu should have at least 1 option");
        System.out.println(message);
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < options.length; i++) {
            if (options[i] != null)
                s.append(i).append(". ").append(options[i].getName()).append("\n");
        }
        System.out.print(s);
        if (!endMessage.isEmpty())
            System.out.println(endMessage);
        int selection = getNextInteger("Select Option (number): ");
        if (selection >= options.length || selection < 0)
            System.out.println("An Option With Index " + selection + " Does Not Exist.");
        else if (options[selection].getAction() != null)
            options[selection].getAction().run();
    }
}
