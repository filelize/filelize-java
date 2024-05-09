package io.github.filelize;

import java.time.ZonedDateTime;

@Filelize(name = "something_single", type = FilelizeType.SINGLE_FILE, directory = "something_single")
public class SomethingSingle {
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
