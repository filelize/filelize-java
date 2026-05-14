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

    @Test
    public void testSync_WhenLocalRecordIsOlder() {
        var local = createSomethingSingle("sync_single");
        local.setCreated(ZonedDateTime.of(2024, 2, 2, 0, 0, 0, 0, ZoneOffset.UTC));
        local.setName("Local Name");
        filelizer.save(local);

        var response = filelizer.sync(
                "sync_single",
                SomethingSingle.class,
                ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                SomethingSingle::getCreated,
                id -> {
                    var external = createSomethingSingle(id);
                    external.setCreated(ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
                    external.setName("External Name");
                    return external;
                }
        );

        assertEquals("External Name", response.getName());
        assertEquals("External Name", filelizer.find("sync_single", SomethingSingle.class).getName());
        filelizer.delete("sync_single", SomethingSingle.class);
    }

    @Test
    public void testSync_WhenLocalRecordIsFresh() {
        var local = createSomethingSingle("sync_single_fresh");
        local.setCreated(ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
        local.setName("Local Name");
        filelizer.save(local);

        var response = filelizer.sync(
                "sync_single_fresh",
                SomethingSingle.class,
                ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                SomethingSingle::getCreated,
                id -> fail("External loader should not be called for a fresh local record")
        );

        assertEquals("Local Name", response.getName());
        filelizer.delete("sync_single_fresh", SomethingSingle.class);
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
