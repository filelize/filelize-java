package io.github.filelize;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FilelizerSomethingElseTest {

    private final Filelizer filelizer;

    public FilelizerSomethingElseTest() {
        filelizer = new Filelizer("src/test/resources/something_else");
    }

    @Test
    void testSave() {
        var somethingElse = createSomethingElse("e1");
        var id = filelizer.save(somethingElse);
        assertEquals("SomethingElse", id);

        var response = filelizer.find(id, SomethingElse.class);
        assertNotNull(response);
    }

    @Test
    void testSaveWithId() {
        var somethingElse = createSomethingElse("e2");
        var id = filelizer.save("my_something_else", somethingElse);
        assertEquals("my_something_else", id);

        var response = filelizer.find(id, SomethingElse.class);
        assertNotNull(response);
    }

    @Test
    void testSaveAll() {
        var somethings = createSomethingElseList();
        List<String> ids = filelizer.saveAll(somethings);
        assertEquals("SomethingElse_all", ids.stream().findFirst().orElse(null));
        var response = filelizer.findAll(SomethingElse.class);
        assertNotNull(response);
    }

    @Test
    void testFind_WithTypeReferenceList() {
        var somethings = createSomethingElseList();
        filelizer.saveAll(somethings);

        var response = filelizer.find("SomethingElse_all", new TypeReference<List<SomethingElse>>() {});

        assertNotNull(response);
        assertEquals(3, response.size());
    }

    @Test
    void testSync_WhenLocalRecordIsOlder() {
        var local = createSomethingElse("sync_object");
        local.setCreated(ZonedDateTime.of(2024, 3, 3, 0, 0, 0, 0, ZoneOffset.UTC));
        local.setValue("Local Value");
        filelizer.save("sync_object", local);

        var response = filelizer.sync(
                "sync_object",
                SomethingElse.class,
                ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                SomethingElse::getCreated,
                id -> {
                    var external = createSomethingElse(id);
                    external.setCreated(ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC));
                    external.setValue("External Value");
                    return external;
                }
        );

        assertEquals("External Value", response.getValue());
        assertEquals("External Value", filelizer.find("sync_object", SomethingElse.class).getValue());
        filelizer.delete("sync_object", SomethingElse.class);
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
        somethingElse.setCreated(ZonedDateTime.of(2024, 3, 3, 0, 0, 0, 0, ZoneOffset.UTC));
        somethingElse.setValue("Some Value");
        return somethingElse;
    }
}
