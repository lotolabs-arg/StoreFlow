package infrastructure.ui;

/**
 * Entry point wrapper to avoid module-path issues when running as JAR.
 */
public final class Launcher {

    private Launcher() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        MainApp.main(args);
    }
}
