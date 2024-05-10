package io.github.filelize;

import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilelizerSingleFileTest {

    private final Filelizer filelizer;

    public FilelizerSingleFileTest() {
        filelizer = new Filelizer("src/test/resources");
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
        var ids = filelizer.saveAll(somethings);
        assertNotNull(ids);
        var response = filelizer.findAll(SomethingSingle.class);
        assertNotNull(response);
    }
    @Test
    public void testDelete() {
        var something = createSomethingSingle("should_be_deleted");
        var id = filelizer.save(something);
        assertEquals("should_be_deleted", id);

        filelizer.delete(id, SomethingSingle.class);
        var response = filelizer.find(id, SomethingSingle.class);
        assertNull(response);
    }
    @Test
    public void testFind_WhenFileIsMissing() {
        var response = filelizer.find("n/a", SomethingElse.class);
        assertNull(response);
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
        something.setCreated(ZonedDateTime.of(2024, 2, 2, 0, 0, 0, 0, ZoneOffset.UTC));
        something.setName("Some Name");
        return something;
    }
}
