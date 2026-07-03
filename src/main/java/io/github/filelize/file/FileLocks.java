package io.github.filelize.file;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public final class FileLocks {

    private static final ConcurrentHashMap<String, ReentrantLock> LOCKS = new ConcurrentHashMap<>();

    private FileLocks() {
    }

    public static ReentrantLock forPath(String fullPath) {
        return LOCKS.computeIfAbsent(fullPath, path -> new ReentrantLock());
    }
}
