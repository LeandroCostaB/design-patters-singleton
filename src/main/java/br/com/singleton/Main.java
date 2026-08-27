package br.com.singleton;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        demonstrateConcurrentFirstAccess();

        UserService userService = new UserService();
        ReportService reportService = new ReportService();

        userService.connect();
        reportService.generate();

        System.out.println("Mesma instancia nos dois servicos: "
                + (userService.getConfig() == reportService.getConfig()));

        AppConfig firstAccess = AppConfig.getInstance();
        AppConfig secondAccess = AppConfig.getInstance();
        System.out.println("Mesma instancia em acessos sequenciais: "
                + (firstAccess == secondAccess));
    }

    private static void demonstrateConcurrentFirstAccess() throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        AtomicReference<AppConfig> firstThreadConfig = new AtomicReference<>();
        AtomicReference<AppConfig> secondThreadConfig = new AtomicReference<>();

        Thread firstThread = new Thread(
                () -> accessConfig(startSignal, firstThreadConfig),
                "config-thread-1");
        Thread secondThread = new Thread(
                () -> accessConfig(startSignal, secondThreadConfig),
                "config-thread-2");

        firstThread.start();
        secondThread.start();
        startSignal.countDown();
        firstThread.join();
        secondThread.join();

        System.out.println("Mesma instancia entre threads: "
                + (firstThreadConfig.get() == secondThreadConfig.get()));
    }

    private static void accessConfig(
            CountDownLatch startSignal,
            AtomicReference<AppConfig> destination) {
        try {
            startSignal.await();
            destination.set(AppConfig.getInstance());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }
}
