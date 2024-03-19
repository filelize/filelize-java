package org.filelize.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesUtil {

    public static void ensureFile(String fullPath) throws IOException {
        var path = Paths.get(fullPath);
        Files.createDirectories(path.getParent());
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }
}
