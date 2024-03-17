package org.filelize;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class FilelizerSingleFileTest {

    private final Filelizer filelizer;

    public FilelizerSingleFileTest() {
        filelizer = new Filelizer("src/test/resources/single_file");
    }

    @Test
    public void testSave() {
        var something = createSomethingSingle("single_1");
        var filename = filelizer.save(something);

        var response = filelizer.find(filename, SomethingSingle.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveWithCustomFilename() {
        var something = createSomethingSingle("single_1");
        var filename = filelizer.save("something.json", something);

        var response = filelizer.find(filename, SomethingSingle.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveAll() {
        var somethings = createSomethingSingleList();
        var filenames = filelizer.saveAll(somethings);

        for (var filename : filenames) {
            var response = filelizer.find(filename, SomethingSingle.class);
            assertNotNull(response);
        }
    }

    @Test
    public void testSaveAllWithCustomFilename() {
        var somethings = createSomethingSingleList();
        var filename = filelizer.saveAll("somethings.json", somethings);
        var response = filelizer.find(filename, List.class);
        assertNotNull(response);
    }

    private static List<SomethingSingle> createSomethingSingleList() {
        var somethings = new ArrayList<SomethingSingle>();
        somethings.add(createSomethingSingle("single_1"));
        somethings.add(createSomethingSingle("single_2"));
        somethings.add(createSomethingSingle("single_3"));
        return somethings;
    }
    private static SomethingSingle createSomethingSingle(String id) {
        var something = new SomethingSingle();
        something.setId(id);
        something.setName("Some Name");
        return something;
    }
}
