package com.library.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * PatternLogger — Demo utility.
 *
 * Every time a design pattern does something meaningful, it calls
 * PatternLogger.log(patternName, message). The MainController's log
 * TextArea subscribes here, so the demo audience sees live pattern
 * activity without opening the console.
 *
 * This is a Pure Fabrication (GRASP) — no domain equivalent, exists
 * purely to make the demo clear.
 */
public class PatternLogger {

    private static final List<Consumer<String>> listeners = new ArrayList<>();

    private PatternLogger() {}

    /**
     * Log a pattern event.
     * @param pattern  e.g. "SINGLETON", "FACTORY", "FACADE", "OBSERVER"
     * @param message  what the pattern just did
     */
    public static void log(String pattern, String message) {
        String entry = String.format("[%s]  %s", pattern, message);
        System.out.println(entry);
        listeners.forEach(listener -> listener.accept(entry));
    }

    /**
     * Subscribe to receive log entries (used by the UI TextArea).
     */
    public static void addListener(Consumer<String> listener) {
        listeners.add(listener);
    }

    public static void clearListeners() {
        listeners.clear();
    }
}
