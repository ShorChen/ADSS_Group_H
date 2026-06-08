package Core.App;

import java.io.OutputStream;
import java.io.PrintStream;

public class Launcher {

    public static void main(String[] args) {
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {}
        }) {
            @Override
            public void println(String x) {
                if (x != null && x.contains("Unsupported JavaFX configuration")) return;
                originalErr.println(x);
            }
            @Override
            public void print(String x) {
                if (x != null && x.contains("Unsupported JavaFX configuration")) return;
                originalErr.print(x);
            }
        });
        MainApp.main(args);
    }
}