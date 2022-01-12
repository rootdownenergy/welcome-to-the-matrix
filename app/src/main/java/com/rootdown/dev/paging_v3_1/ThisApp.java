package com.rootdown.dev.paging_v3_1;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThisApp extends Application {
    ExecutorService executorService = Executors.newFixedThreadPool(4);
}
