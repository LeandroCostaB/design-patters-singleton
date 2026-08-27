package br.com.singleton;

/**
 * Configuracoes compartilhadas por toda a aplicacao.
 */
public final class AppConfig {

    private final String databaseUrl;
    private final String environment;

    private AppConfig() {
        this.databaseUrl = "jdbc:mysql://localhost/app";
        this.environment = "development";
    }

    /**
     * A JVM inicializa classes de forma segura entre threads. Por isso, a
     * instancia e criada uma unica vez quando este holder e acessado.
     */
    private static class InstanceHolder {
        private static final AppConfig INSTANCE = new AppConfig();
    }

    public static AppConfig getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getEnvironment() {
        return environment;
    }
}
