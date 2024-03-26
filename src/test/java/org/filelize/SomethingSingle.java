package org.filelize;

@Filelize(name = "something_single", type = FilelizeType.SINGLE_FILE, directory = "something_single")
public class SomethingSingle {
    @Id
    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
