package io.github.filelize;

import java.time.ZonedDateTime;

@Filelize(name = "something_multiple", type = FilelizeType.MULTIPLE_FILES, directory = "something_multiple/mydirectory")
public class SomethingMultiple {
    @Id
    private String id;
    private ZonedDateTime created;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
