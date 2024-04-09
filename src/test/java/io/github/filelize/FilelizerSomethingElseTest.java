package io.github.filelize;

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
        var id = filelizer.save(somethingElse);
        assertEquals("SomethingElse", id);

        var response = filelizer.find(id, SomethingElse.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveWithId() {
        var somethingElse = createSomethingElse("e2");
        var id = filelizer.save("my_something_else", somethingElse);
        assertEquals("my_something_else", id);

        var response = filelizer.find(id, SomethingElse.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveAll() {
        var somethings = createSomethingElseList();
        List<String> ids = filelizer.saveAll(somethings);
        assertEquals("SomethingElse_all", ids.stream().findFirst().orElse(null));
        var response = filelizer.findAll(SomethingElse.class);
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
