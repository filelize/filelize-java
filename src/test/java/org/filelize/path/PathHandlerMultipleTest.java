package org.filelize.path;

import org.filelize.FilelizeType;
import org.filelize.SomethingMultiple;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PathHandlerMultipleTest {

    private final PathHandler pathHandler;

    public PathHandlerMultipleTest() {
        this.pathHandler = new PathHandler("src/test/resources", FilelizeType.MULTIPLE_FILES);
    }

    @Test
    public void testGetFullPath() {
        var something = new SomethingMultiple();
        something.setId("myId1");
        var fullPath = pathHandler.getFullPath(something);
        assertEquals("src/test/resources/something_multiple/myFolder/something_multiple_myId1.json", fullPath);
    }

    @Test
    public void testGetFullPathWithId() {
        var fullPath = pathHandler.getFullPath("myId1", SomethingMultiple.class);
        assertEquals("src/test/resources/something_multiple/myFolder/something_multiple_myId1.json", fullPath);
    }

    @Test
    public void testGetFullPathOfMap() {
        Map<String, SomethingMultiple> objects = new HashMap<>();
        objects.put("id1", new SomethingMultiple());
        objects.put("id2", new SomethingMultiple());
        var fullPath = pathHandler.getFullPathOfMap(objects);
        assertEquals("src/test/resources/something_multiple/myFolder/something_multiple.json", fullPath);
    }

    @Test
    public void testGetFullPats() {
        var fullPaths = pathHandler.getFullPaths(SomethingMultiple.class);
        assertEquals("src/test/resources/something_multiple/myFolder/something_multiple_m1.json", fullPaths.values().stream().findFirst().orElse(null));
    }

}
