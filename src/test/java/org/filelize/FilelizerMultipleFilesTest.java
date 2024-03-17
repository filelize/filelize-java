package org.filelize;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class FilelizerMultipleFilesTest {

    private final Filelizer filelizer;

    public FilelizerMultipleFilesTest() {
        filelizer = new Filelizer("src/test/resources/multiple_files/");
    }

    @Test
    public void testSave() {
        var something = createSomethingMultiple("multiple_1");
        var filename = filelizer.save(something);

        var response = filelizer.find(filename, SomethingSingle.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveWithCustomFilename() {
        var something = createSomethingMultiple("multiple_1");
        var filename = filelizer.save("something.json", something);

        var response = filelizer.find(filename, SomethingSingle.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveAll() {
        var somethings = createSomethingMultipleList();
        var filenames = filelizer.saveAll(somethings);
        for (var filename : filenames) {
            var response = filelizer.find(filename, SomethingSingle.class);
            assertNotNull(response);
        }
    }

    @Test
    public void testSaveAllWithCustomFilename() {
        var somethings = createSomethingMultipleList();
        var filename = filelizer.saveAll("somethings.json", somethings);
        var response = filelizer.find(filename, List.class);
        assertNotNull(response);
    }

    private static List<SomethingMultiple> createSomethingMultipleList() {
        var somethings = new ArrayList<SomethingMultiple>();
        somethings.add(createSomethingMultiple("multiple_1"));
        somethings.add(createSomethingMultiple("multiple_2"));
        somethings.add(createSomethingMultiple("multiple_3"));
        return somethings;
    }
    private static SomethingMultiple createSomethingMultiple(String id) {
        var something = new SomethingMultiple();
        something.setId(id);
        something.setName("Some Name");
        return something;
    }
}
