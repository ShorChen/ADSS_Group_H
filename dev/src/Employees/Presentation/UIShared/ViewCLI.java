package Employees.Presentation.UIShared;

import Employees.Context.SessionManager;
import Employees.Presentation.Utils.Option;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public abstract class ViewCLI extends View{
    protected static Scanner reader = new Scanner(System.in);
    public ViewCLI(Runnable onDismiss) {
        super(onDismiss);
    }

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
            System.out.println(message);
            try {
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
            System.out.println(message);
            try {
                double result = reader.nextDouble();
                reader.nextLine();
                return result;
            } catch (InputMismatchException e) {
                System.out.println("Expected a decimal number, try again");
                reader.nextLine();
            }
        }


    }


    public float getNextFloat(String message) {
        while (true) {
            System.out.println(message);
            try {
                float result = reader.nextFloat();
                reader.nextLine();
                return result;
            } catch (InputMismatchException e) {
                System.out.println("Expected a floating point number, try again");
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

    protected void displayMenu(Option.Builder options) {
        if (SessionManager.isDebugMode()) debug(options);
        if (options == null || options.isEmpty())
            throw new IllegalArgumentException("Menu should have at least 1 option");
        System.out.println(options.getMessage());
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i) != null)
                s.append(i).append(". ").append(options.get(i).getName()).append("\n");
        }
        System.out.print(s);
        System.out.println(options.getEndMessage());
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



    public <T, E> T selectionMenuOf(String message, List<E> options, Function<E, String> repr, Function<E, T> f) {
        AtomicReference<T> t = new AtomicReference<>();
        Option.Builder builder = new Option.Builder(message);
        options.forEach(o -> builder.append(repr.apply(o), () -> t.set(f.apply(o))));
        displayMenu(builder);

        return t.get();
    }

    public <T> T selectionMenuOf(String message, List<T> options) {
        return selectionMenuOf(message, options, Objects::toString, o -> o);
    }
}
