package Workers.presentation.ui_shared;

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
