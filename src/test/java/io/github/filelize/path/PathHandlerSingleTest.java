package io.github.filelize.path;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.filelize.FilelizeType;
import io.github.filelize.SomethingSingle;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathHandlerSingleTest {

    private final PathHandler pathHandler;

    public PathHandlerSingleTest() {
        this.pathHandler = new PathHandler("src/test/resources", FilelizeType.SINGLE_FILE, new ObjectMapper());
    }

    @Test
    public void testGetFullPath() {
        var fullPath = pathHandler.getFullPath(new SomethingSingle());
        assertEquals("src/test/resources/something_single/something_single.json", fullPath);
    }

    @Test
    public void testGetFullPathWithId() {
        var fullPath = pathHandler.getFullPath("id1", SomethingSingle.class);
        assertEquals("src/test/resources/something_single/something_single.json", fullPath);
    }

    @Test
    public void testGetFullPathOfMap() {
        Map<String, SomethingSingle> objects = new HashMap<>();
        objects.put("id1", new SomethingSingle());
        objects.put("id2", new SomethingSingle());
        var fullPath = pathHandler.getFullPathOfMap(objects);
        assertEquals("src/test/resources/something_single/something_single.json", fullPath);
    }

    @Test
    public void testGetFullPaths() {
        var fullPaths = pathHandler.getFullPaths(SomethingSingle.class);
        assertEquals("src/test/resources/something_single/something_single.json", fullPaths.values().stream().findFirst().orElse(null));
    }

}
