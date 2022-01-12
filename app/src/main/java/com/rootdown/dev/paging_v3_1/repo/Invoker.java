package com.rootdown.dev.paging_v3_1.repo;

import java.util.concurrent.Executor;

public class Invoker implements Executor {
    @Override
    public void execute(Runnable r) {
        r.run();
    }
}
