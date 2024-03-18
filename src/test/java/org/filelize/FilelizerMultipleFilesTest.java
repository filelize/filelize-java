package org.filelize;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FilelizerMultipleFilesTest {

    private final Filelizer filelizer;

    public FilelizerMultipleFilesTest() {
        filelizer = new Filelizer("src/test/resources/something_multiple");
    }

    @Test
    public void testSave() {
        var something = createSomethingMultiple("m1");
        var filename = filelizer.save(something);
        assertEquals("something_multiple_m1.json", filename);

        var response = filelizer.find(SomethingMultiple.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveAll() {
        var somethings = createSomethingMultipleList();
        var filenames = filelizer.saveAll(somethings);
        var response = filelizer.find(SomethingMultiple.class);
        assertNotNull(response);
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
