package org.filelize.path;

import org.filelize.FilelizeType;
import org.filelize.SomethingSingle;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PathHandlerSingleTest {

    private final PathHandler pathHandler;

    public PathHandlerSingleTest() {
        this.pathHandler = new PathHandler("src/test/resources", FilelizeType.SINGLE_FILE);
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
    public void testGetFullPats() {
        var fullPaths = pathHandler.getFullPaths(SomethingSingle.class);
        assertEquals("src/test/resources/something_single/something_single.json", fullPaths.values().stream().findFirst().orElse(null));
    }

}
