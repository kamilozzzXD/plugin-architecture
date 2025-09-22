package org.example.core;

public class AppContextSingleton {
    private static final AppContext INSTANCE = new AppContext();
    public static AppContext get() {
        return INSTANCE;
    }
}

