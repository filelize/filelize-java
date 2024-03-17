package org.filelize;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class FilelizerTest {

    private final Filelizer filelizer;

    public FilelizerTest() {
        filelizer = new Filelizer("src/test/resources/something_else/");
    }

    @Test
    public void testSaveWithFilename(){
        var something = createSomething("a1");
        var filename = filelizer.save("a1_custom_filename.json", something);

        var response = filelizer.find(filename, SomethingSingle.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveWithAnnotatedWithJsonFilename() {
        var something = createSomething("a2");
        var filename = filelizer.save(something);

        var response = filelizer.find(filename, SomethingSingle.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveWithoutAnnotation() {
        var somethingElse = createSomethingElse();
        var filename = filelizer.save(somethingElse);

        var response = filelizer.find(filename, SomethingElse.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveAllWithAnnotationJsonFilename() {
        var somethings = new ArrayList<SomethingSingle>();
        somethings.add(createSomething("b1"));
        somethings.add(createSomething("b2"));
        List<String> filenames = filelizer.saveAll(somethings);

        for (var filename : filenames) {
            SomethingSingle response = filelizer.find(filename, SomethingSingle.class);
            assertNotNull(response);
        }
    }

    @Test
    public void testSaveAllWithManuelFilename() {
        var somethings = new ArrayList<>();
        somethings.add(createSomething("b1"));
        somethings.add(createSomething("b2"));
        String filename = filelizer.saveAll("somethings.json", somethings);
        List<SomethingSingle> response = filelizer.find(filename, List.class);
        assertNotNull(response);
    }

    private static SomethingSingle createSomething(String id) {
        var something = new SomethingSingle();
        something.setId(id);
        something.setName("Some Name");
        return something;
    }

    private static SomethingElse createSomethingElse() {
        var somethingElse = new SomethingElse();
        somethingElse.setId("something_else");
        somethingElse.setValue("Some Value");
        return somethingElse;
    }
}
