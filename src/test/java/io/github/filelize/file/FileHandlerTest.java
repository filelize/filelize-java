package io.github.filelize.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.filelize.SomethingElse;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

public class FileHandlerTest {

    private final FileHandler fileHandler;

    public FileHandlerTest() {
        this.fileHandler = new FileHandler(new ObjectMapper());
    }

    @Test
    public void testReadFile_ShouldHandleNoSuchFileException() {
        try {
            var something = fileHandler.readFile("tmp/not_existing_folder.json", SomethingElse.class);
            assertNull(something);
        } catch(IOException e) {
            fail(e);
        }
    }

}
