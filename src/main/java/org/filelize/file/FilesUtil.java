package org.filelize.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FilesUtil {

    public static void ensureFile(String fullPath) throws IOException {
        var path = Paths.get(fullPath);
        Files.createDirectories(path.getParent());
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    public static List<String> getFilenames(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        List<String> filenames = new ArrayList<>();
        if(files !=null) {
            for (File file : files) {
                if (file.isFile()) {
                    filenames.add(file.getName());
                }
            }
        }
        return filenames;
    }
}
