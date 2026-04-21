package presentation.util;

import java.util.ArrayList;
import java.util.List;

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

    public static class Builder {
        private String message;
        private final List<Option> options;

        public Builder(String message) {
            options = new ArrayList<>();
            this.message = message;
        }

        public Builder append(String name, Runnable action) {
            options.add(new Option(name, action));
            return this;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Option> getOptions() {
            return options;
        }

        public int size() {
            return options.size();
        }

        public Option get(int i){
            return options.get(i);
        }
    }

}
