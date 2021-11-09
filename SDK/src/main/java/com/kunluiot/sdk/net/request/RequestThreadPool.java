package com.kunluiot.sdk.net.request;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestThreadPool {

    private static ExecutorService executor = Executors.newFixedThreadPool(3);

    public static void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
