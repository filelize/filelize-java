# Filelize for java/kotlin

Filelize is a lightweight Java/Kotlin library designed to simplify the process of saving objects in human-readable files.

One of the biggest advantages is in the annotations and methods provided, eliminating the need for repetitive boilerplate code. Additionally, it offers flexibility with options for both single and multiple file storage, giving an easy way to work with very large collections distributed among multiple files.

### Usange

To integrate Filelize into your project, you have two options:

1. **Using Maven:** Add the following dependency to your pom.xml file and follow the guide for [Working with the Apache Maven registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)
````xml
<dependency>
  <groupId>org.filelize</groupId>
  <artifactId>filelize-java</artifactId>
  <version>1.0.0</version>
</dependency>
````
2. **Manual Build:** Alternatively, you can download this repository and build it locally.

### Lets get started

Initialize a Filelizer with your preferred location of your files. (For subfolders you can add them directly on your domain object)

````java
Filelizer filelizer = new Filelizer("src/test/resources/");
````

### Saving to a single file
To save an object to a single file, annotate your model class with `@Filelize` and set the `type` parameter to `FilelizeType.SINGLE_FILE`. Additionally, mark the identifying attribute with `@Id`. 
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

### Saving to multiple files
For saving objects to multiple files, follow the same steps as for single-file saving, but set the type parameter to `FilelizeType.MULTIPLE_FILES`.
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

### Find a file
````java
var something = filelizer.find("id1", Something.class);
````

### FindAll:
````java
var somethings = filelizer.findAll(Something.class);
````

### Save a file
````java
var id = filelizer.save(something);
````

### SaveAll in one or multiple file(s):
````java
var ids = filelizer.saveAll(somethings);
````

## Contribute
Contributions are welcomed! Feel free to create a pull request to contribute.
