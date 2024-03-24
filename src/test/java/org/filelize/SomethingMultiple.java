package org.filelize;

@Filelize(name = "myfolder/something_multiple", type = FilelizeType.MULTIPLE_FILES)
public class SomethingMultiple {
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
