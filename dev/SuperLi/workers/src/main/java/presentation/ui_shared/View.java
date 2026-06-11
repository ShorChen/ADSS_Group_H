package presentation.ui_shared;

import context.SessionManager;
import javafx.scene.Scene;
import presentation.util.Option;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public abstract class View {
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
}
