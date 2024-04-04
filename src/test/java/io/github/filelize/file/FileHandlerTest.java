package io.github.filelize.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.filelize.SomethingElse;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNull;

public class FileHandlerTest {

    private final FileHandler fileHandler;

    public FileHandlerTest() {
        this.fileHandler = new FileHandler(new ObjectMapper());
    }

    @Test
    public void testReadFile_ShouldHandleNoSuchFileException() throws IOException {
        var something = fileHandler.readFile("tmp/not_existing_folder.json", SomethingElse.class);
        assertNull(something);
    }

}
