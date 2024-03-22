# Filelize for java/kotlin

When you just need a simple way to save an object as json to a human-readable file

### Usange

Add this to your pom.xml
````xml
<dependency>
  <groupId>org.filelize</groupId>
  <artifactId>filelize-java</artifactId>
  <version>1.0.0</version>
</dependency>
````


#### Saving to a single file

Add Filelize to your model file with FilelizeType.SINGLE_FILE
````java
import org.filelize.Filelize;
import org.filelize.Id;

@Filelize(name = "my_something", type = FilelizeType.SINGLE_FILE)
public class Something {
    @Id
    private String id;
    private String name;
    ...
}
````

#### Saving to multiple files

Add Filelize to your model file with FilelizeType.MULTIPLE_FILES

````java
import org.filelize.Filelize;
import org.filelize.Id;

@Filelize(name = "my_something", type = FilelizeType.MULTIPLE_FILES)
public class Something {
    @Id
    private String id;
    private String name;
    ...
}
````

Find a file:
````java
var something = filelizer.find("id1", Something.class);
````

Save a file:
````java
var id = filelizer.save(something);
````

SaveAll in one file:
````java
var ids = filelizer.saveAll(somethings);
````

FindAll:
````java
var somethings = filelizer.findAll(Something.class);
````

