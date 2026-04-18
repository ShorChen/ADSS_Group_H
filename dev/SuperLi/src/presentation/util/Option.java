package presentation.util;

public class Option {
    private String name;
    private Runnable action;

    public Option(String name, Runnable action) {
        this.name = name;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Runnable getAction() {
        return action;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }
}
