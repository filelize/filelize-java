package org.filelize;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FilelizerMultipleFilesTest {

    private final Filelizer filelizer;

    public FilelizerMultipleFilesTest() {
        filelizer = new Filelizer("src/test/resources");
    }

    @Test
    public void testSave() {
        var something = createSomethingMultiple("m1");
        var id = filelizer.save(something);
        assertEquals("m1", id);

        var response = filelizer.find(id, SomethingMultiple.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveAll() {
        var somethings = createSomethingMultipleList();
        var ids = filelizer.saveAll(somethings);
        assertNotNull(ids);
        var response = filelizer.findAll(SomethingMultiple.class);
        assertNotNull(response);
    }

    @Test
    public void testDelete() {
        var something = createSomethingMultiple("should_be_deleted");
        var id = filelizer.save(something);
        assertEquals("should_be_deleted", id);

        filelizer.delete(id, SomethingMultiple.class);
        var response = filelizer.find(id, SomethingMultiple.class);
        assertNull(response);
    }

    private static List<SomethingMultiple> createSomethingMultipleList() {
        var somethings = new ArrayList<SomethingMultiple>();
        somethings.add(createSomethingMultiple("m10"));
        somethings.add(createSomethingMultiple("m11"));
        somethings.add(createSomethingMultiple("m12"));
        return somethings;
    }
    private static SomethingMultiple createSomethingMultiple(String id) {
        var something = new SomethingMultiple();
        something.setId(id);
        something.setName("Some Name");
        return something;
    }
}
