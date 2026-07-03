---
name: filelizer-developer-skill
description: Reference for contributing to the filelize-java repository itself (io.github.filelize, a small Java 21 library that persists objects to human-readable JSON files via annotations). Use this whenever editing code under src/main/java/io/github/filelize or src/test/java/io/github/filelize, reviewing a PR against this repo, deciding where a new file-touching feature belongs, or debugging save/find/delete/sync behavior — even if the user doesn't say "skill" or name the architecture explicitly. Not for people consuming filelize-java as a dependency in another project — that's just the README.
---

# filelize-java developer reference

This is a reference for working ON this codebase, not for using the library. It exists so a fresh session doesn't have to re-derive the architecture, the intentional design decisions, and the conventions from scratch.

## Architecture

`Filelizer` ([Filelizer.java](../../../src/main/java/io/github/filelize/Filelizer.java)) is the public facade. Every call dispatches to one of three `IFilelizer` strategy implementations, chosen by the `FilelizeType` on the domain class's `@Filelize` annotation (falling back to the `Filelizer`'s `defaultFilelizeType` when the class has no annotation):

| Strategy | FilelizeType | Storage shape |
|---|---|---|
| `FilelizerObject` | `OBJECT_FILE` | One file per id (`<id>.json`), plus a `<Type>_all.json` for `findAll`. `save(T)` without an explicit id uses the class simple name as id. |
| `FilelizerSingle` | `SINGLE_FILE` | All instances of a type share one JSON file as `Map<id, object>`. `save`/`delete` read the whole file, modify the map, write it back. |
| `FilelizerMultiple` | `MULTIPLE_FILES` | One file per instance, named `<name>_<id>.json`, in a directory. Best for large collections. |

Supporting classes:
- `@Filelize(name=, type=, directory=)` on the domain class drives dispatch, file naming, and destination directory.
- `@Id` on a field drives how a record's filename/map-key is derived. If no field carries `@Id`, `FilelizeUtil.calculateMD5` hashes the serialized JSON to produce a stable id instead — see "Intentional design decisions" below before flagging this as a security issue, it isn't one.
- `FileHandler` (`io.github.filelize.file`) does the actual Jackson read/write/delete against a resolved full path.
- `PathHandler` (`io.github.filelize.path`) resolves `basePath + directory + filename` for a given type/id/FilelizeType.
- `FilesUtil` has small filesystem helpers (`ensureFile`, `getFilenames`).
- `FileLocks` (`io.github.filelize.file.FileLocks`) is a `ConcurrentHashMap<String, ReentrantLock>` keyed by full file path. `FileHandler` takes this lock around every read/write/delete; `FilelizerSingle` additionally wraps its whole read-modify-write sequence in the same lock (keyed by the same path) to make save/delete atomic for the shared file. Locks are reentrant on purpose: an outer lock taken in `FilelizerSingle` and an inner one taken by `FileHandler` on the *same path* in the *same thread* must not deadlock. This is the library's only concurrency control — there's no cross-process/filesystem locking, just in-process mutual exclusion per resolved path. Preserve this if you touch save/delete/write paths.
- `IFilelizer.sync(id, valueType, syncIfOlderThan, dateTimeExtractor, externalLoader)` is a default method: returns the local record if it's fresh enough per `dateTimeExtractor`, otherwise pulls from `externalLoader` and persists the result.

## Intentional design decisions — don't "fix" without asking

- **Fail-soft error handling.** `find`/`findAll` across all three strategies catch `IOException` (including malformed/corrupt JSON), log via slf4j, and return `null`/empty map — indistinguishable from "record doesn't exist yet." This is deliberate: Filelize is meant to behave like a simple map. Confirmed with the maintainer — treat this as a documented behavior, not a bug, unless explicitly asked to change it.
- **MD5 for auto-generated ids.** Used only to derive a stable filename from an object's serialized content when it has no `@Id` field — not a security-sensitive use. Don't raise it as a "weak hash" finding in reviews of this repo.
- **SNAPSHOT version ahead of README.** `pom.xml` version is normally a `-SNAPSHOT` ahead of the last published release documented in `README.md`. That's expected drift between in-progress work and the last Maven Central release, not a bug to reconcile.

## Conventions to preserve

- **Use `LinkedHashMap`, not `HashMap`,** anywhere a collection of records is built from file contents or otherwise needs to preserve insertion/file order. This has regressed twice already (commit `29d9e29`, then again in `FilelizerMultiple.findAll` and `FilelizerObject.findAll` during a 2026-07-03 review) — when adding a new dispatch path or collection-building method, check this explicitly.
- **Route all file I/O through `FileHandler`/`FileLocks`**, never raw `java.nio.file` calls in new code — that's what keeps the per-path locking guarantee intact.
- **Match test coverage across all three strategies.** Any new `IFilelizer` method needs a test in each of `FilelizerSingleFileTest`, `FilelizerMultipleFilesTest`, and `FilelizerSomethingElseTest` (the OBJECT_FILE case) under `src/test/java/io/github/filelize/`. Each is backed by a small POJO fixture (`SomethingSingle`/`SomethingMultiple`/`SomethingElse`) and JSON fixtures under `src/test/resources/`.

## Build, test, release

- Java 21, Maven. Run `mvn test` for the JUnit 5 suite.
- JaCoCo enforces 90% line and branch coverage plus a complexity gate (`pom.xml` jacoco-maven-plugin rule, `PACKAGE` element) — new code needs tests or the build fails the coverage gate, not just the tests.
- Release flows through GitHub Actions (`.github/workflows/maven-publish.yml`) publishing to Maven Central via the `release` Maven profile (`central-publishing-maven-plugin`, GPG signing).
- `README.md` is the user-facing doc for library consumers (usage examples, Spring Boot integration pattern, "Filelize for Test Data Setup" workflow) — keep it in sync when the public API changes.
