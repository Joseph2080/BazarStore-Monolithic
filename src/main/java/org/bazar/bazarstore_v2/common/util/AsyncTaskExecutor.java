package org.bazar.bazarstore_v2.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTaskExecutor {

    private static final Logger logger = LoggerFactory.getLogger(AsyncTaskExecutor.class);
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * Run tasks asynchronously, exceptions are logged but do not stop other tasks.
     * Fire-and-forget style.
     *
     * @param tasks List of Runnable tasks
     */
    public static void runTasksNonBlocking(List<Runnable> tasks) {
        tasks.forEach(task ->
                CompletableFuture.runAsync(() -> {
                    try {
                        task.run();
                    } catch (Exception e) {
                        logger.error("Async task failed (non-blocking): {}", e.getMessage(), e);
                    }
                }, executor)
        );
    }

    /**
     * Run tasks asynchronously, waits for all to complete.
     * Exceptions in tasks are propagated as a combined exception.
     *
     * @param tasks List of Runnable tasks
     * @throws RuntimeException if any task fails
     */
    public static void runTasksBlocking(List<Runnable> tasks) {
        List<CompletableFuture<Void>> futures = tasks.stream()
                .map(task -> CompletableFuture.runAsync(task, executor))
                .toList();

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            // Collect underlying exceptions
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            logger.error("One or more async tasks failed (blocking): {}", cause.getMessage(), cause);
            throw new RuntimeException("Async tasks failed", cause);
        }
    }

    /** Graceful shutdown of executor */
    public static void shutdown() {
        executor.shutdown();
    }
}
