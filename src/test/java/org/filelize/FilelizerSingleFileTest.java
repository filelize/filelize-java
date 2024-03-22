package org.filelize;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FilelizerSingleFileTest {

    private final Filelizer filelizer;

    public FilelizerSingleFileTest() {
        filelizer = new Filelizer("src/test/resources/something_single");
    }

    @Test
    public void testSave() {
        var something = createSomethingSingle("s1");
        var id = filelizer.save(something);
        assertEquals("s1", id);

        var response = filelizer.find(id, SomethingSingle.class);
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

    private static List<SomethingSingle> createSomethingSingleList() {
        var somethings = new ArrayList<SomethingSingle>();
        somethings.add(createSomethingSingle("s10"));
        somethings.add(createSomethingSingle("s11"));
        somethings.add(createSomethingSingle("s12"));
        return somethings;
    }
    private static SomethingSingle createSomethingSingle(String id) {
        var something = new SomethingSingle();
        something.setId(id);
        something.setName("Some Name");
        return something;
    }
}
