package org.filelize;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FilelizerSomethingElseTest {

    private final Filelizer filelizer;

    public FilelizerSomethingElseTest() {
        filelizer = new Filelizer("src/test/resources/something_else");
    }

    @Test
    public void testSave() {
        var somethingElse = createSomethingElse("e1");
        var filename = filelizer.save(somethingElse);
        assertEquals("SomethingElse.json", filename);

        var response = filelizer.find(filename, SomethingElse.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveAll() {
        var somethings = createSomethingElseList();
        List<String> filenames = filelizer.saveAll(somethings);
        assertEquals("SomethingElse.json", filenames.get(0));

        var folder = filelizer.getPath();
        var response = filelizer.findAll(folder, SomethingElse.class);
        assertNotNull(response);

    }

    private static List<SomethingElse> createSomethingElseList() {
        var somethings = new ArrayList<SomethingElse>();
        somethings.add(createSomethingElse("e10"));
        somethings.add(createSomethingElse("e11"));
        somethings.add(createSomethingElse("e12"));
        return somethings;
    }

    private static SomethingElse createSomethingElse(String id) {
        var somethingElse = new SomethingElse();
        somethingElse.setId(id);
        somethingElse.setValue("Some Value");
        return somethingElse;
    }
}
