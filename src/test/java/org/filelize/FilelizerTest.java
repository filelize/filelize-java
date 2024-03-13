package org.filelize;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class FilelizerTest {

    private final Filelizer filelizer;

    public FilelizerTest() {
        filelizer = new Filelizer("src/test/resources/");
    }

    @Test
    public void testSaveWithFilename(){
        var something = createSomething("a1");
        String filename = filelizer.save("a1_custom_filename.json", something);

        Something response = filelizer.find(filename, Something.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveWithAnnotatedWithJsonFilename() {
        var something = createSomething("a2");
        String filename = filelizer.save(something);

        Something response = filelizer.find(filename, Something.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveWithoutAnnotation() {
        var somethingElse = createSomethingElse();
        String filename = filelizer.save(somethingElse);

        SomethingElse response = filelizer.find(filename, SomethingElse.class);
        assertNotNull(response);
    }

    @Test
    public void testSaveAllWithAnnotationJsonFilename() {
        var somethings = new ArrayList<Something>();
        somethings.add(createSomething("b1"));
        somethings.add(createSomething("b2"));
        List<String> filenames = filelizer.saveAll(somethings);

        for (var filename : filenames) {
            Something response = filelizer.find(filename, Something.class);
            assertNotNull(response);
        }
    }

    @Test
    public void testSaveAllWithManuelFilename() {
        var somethings = new ArrayList<>();
        somethings.add(createSomething("b1"));
        somethings.add(createSomething("b2"));
        String filename = filelizer.saveAll("somethings.json", somethings);
        List<Something> response = filelizer.find(filename, List.class);
        assertNotNull(response);
    }

    private static Something createSomething(String id) {
        var something = new Something();
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
