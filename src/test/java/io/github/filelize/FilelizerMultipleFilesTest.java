package io.github.filelize;

import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testSync_WhenLocalRecordIsOlder() {
        var local = createSomethingMultiple("sync_multiple");
        local.setCreated(ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
        local.setName("Local Name");
        filelizer.save(local);

        var response = filelizer.sync(
                "sync_multiple",
                SomethingMultiple.class,
                ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                SomethingMultiple::getCreated,
                id -> {
                    var external = createSomethingMultiple(id);
                    external.setCreated(ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
                    external.setName("External Name");
                    return external;
                }
        );

        assertEquals("External Name", response.getName());
        assertEquals("External Name", filelizer.find("sync_multiple", SomethingMultiple.class).getName());
        filelizer.delete("sync_multiple", SomethingMultiple.class);
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
        something.setCreated(ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
        something.setName("Some Name");
        return something;
    }
}

