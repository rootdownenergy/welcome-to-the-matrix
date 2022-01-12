package com.rootdown.dev.paging_v3_1.repo;

import java.util.concurrent.Executor;

public class JavaMapRepository {

    private final Executor executor;

    public JavaMapRepository(Executor executor) {
        this.executor = executor;
    }

    public void makeGeoMatrixRequest(final String jsonBody) {
        executor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
