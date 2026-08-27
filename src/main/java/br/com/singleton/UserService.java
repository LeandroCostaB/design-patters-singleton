package br.com.singleton;

public final class UserService {

    private final AppConfig config = AppConfig.getInstance();

    public void connect() {
        System.out.println("Connecting to " + config.getDatabaseUrl());
    }

    AppConfig getConfig() {
        return config;
    }
}
