# Filelize for java/kotlin

When you just need a simple way to save some json data to a human-readable file

### Usange

Add this to your pom.xml
````xml
<dependency>
  <groupId>org.filelize</groupId>
  <artifactId>filelize-java</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
````

Find a file:
````java
Something something = filelizer.find("something.json", Something.class);
````

Save a file:
````java
String filename = filelizer.save(something);
````

SaveAll in one file:
````java
String filename = filelizer.saveAll("somethings.json", somethings);
````

SaveAll in multiple files:
````java
List<String> filenames = filelizer.saveAll(somethings);
````

