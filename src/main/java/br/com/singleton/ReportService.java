package br.com.singleton;

public final class ReportService {

    private final AppConfig config = AppConfig.getInstance();

    public void generate() {
        System.out.println("Environment: " + config.getEnvironment());
    }

    AppConfig getConfig() {
        return config;
    }
}
