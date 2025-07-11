Ostwind
=======

> My sincere thanks to [yahoo/fili] & [yahoo/elide], which gave tremendous amount of guidance on design and development
> of [Ostwind], and to my former employer, Yahoo, who taught me to love software engineering and fundamentally
> influenced my tech career.

![Java Version Badge][Java Version Badge]
[![Maven Central Version][Maven Central Version Badge]][Maven Central Version URL]
[![GitHub Workflow Status][GitHub Workflow Status badge]][GitHub Workflow Status URL]
[![Apache License Badge][Apache License Badge]][Apache License, Version 2.0]

Ostwind is a Java library that lets you set up object storage webservice with minimal effort. Ostwind is meant to be
specialized on managing **files**, such as books, videos, and photos. It supports object storage through two variants of
APIs:

- A [JSON API] for uploading and downloading files
- A [GraphQL] API for reading file metadata, including

    - File name
    - File type
    - etc.

Ostwind has **first-class support for [OpenStack Swift][OpenStack Swift] and [Hadoop HDFS][Hadoop HDFS]** file storage
back-ends, but Ostwind's flexible pipeline-style architecture can handle nearly any back-end for data storage, such as
[S3][S3 File Store].

Object storage (also known as object-based storage) is a computer data storage architecture that manages data as
objects, as opposed to other storage architectures like file systems which manages data as a file hierarchy, and block
storage which manages data as blocks within sectors and tracks.

Each object (i.e. file), in Ostwind, typically includes:

- **the data itself**,
- **a variable amount of metadata**, and
- **a globally unique identifier**

Ostwind allow retention of massive amounts of unstructured data in which data is _written once and read once (or many
times)_. It is used for purposes such as storing objects like videos and photos.

Ostwind, however, is not intended for transactional data and _ does not support the locking and sharing mechanisms
needed to maintain a single, accurately updated version of a file_.

Quick Start
-----------

Ostwind comes with a pre-configured [example application][example application] to help you get started and serve as a
jumping-off-point for building your own web service using Ostwind. The example application lets you upload and download
books you love to read, and picks up where [Swift's quick-start tutorial][OpenStack Swift's quick-start tutorial]
leaves off.

Features
--------

### Storage Abstraction

**One of the design principles of Ostwind is to abstract lower layers of storage away from the administrators and
applications**. Thus, data is exposed and managed as objects instead of files or blocks. They do not have to perform
lower-level storage functions like constructing and managing logical volumes to utilize disk capacity or setting RAID
levels to deal with disk failure.

Ostwind also allows the addressing and identification of individual objects by more than just file name and file
path. Ostwind adds **a unique identifier** across the entire system, to support much larger namespaces and eliminate name
collisions.

### Inclusion of Rich Custom Metadata within the Object

Ostwind explicitly **separates file metadata from data** to support additional capabilities. As opposed to fixed
metadata in file systems (filename, creation date, type, etc.), Ostwind provides for full function, custom,
object-level metadata in order to:

- Capture application-specific or user-specific information for better indexing purposes
- Support data-management policies (e.g. a policy to drive object movement from one storage tier to another)
- Centralize management of storage across many individual nodes and clusters
- Optimize metadata storage (e.g. encapsulated, database or key value storage) and caching/indexing (when authoritative
  metadata is encapsulated with the metadata inside the object) independently from the data storage (e.g. unstructured
  binary storage)

### Programmatic Data Management

Ostwind provides programmatic interfaces to allow applications to manipulate data. At the base level, this includes
create, read, and delete (CRUD) functions for basic read, write and delete operations. The API implementations are
REST-based, allowing the use of many standard HTTP calls.

Documentation
-------------

More information about Ostwind can be found [here][Ostwind Documentation]

Contributing to Ostwind
-----------------------

Besides contributing high-quality code and tests, there are many other things that we could naturally be undertaking as
an Ostwind contributor:

- help out users and other developers on the [GitHub issues](https://github.com/QubitPi/Ostwind/issues)
- review and test the [pull requests](https://github.com/QubitPi/Ostwind/pulls) submitted by others; this can help to
  offload the burden on existing committers, who will definitely appreciate your efforts
- participate in discussions about releases, roadmaps, architecture, and long-term plans
- help improve the [website](https://ostwind.qubitpi.org/)
- participate in (or even initiate) real-world events such as user/developer meetups
- improve project infrastructure in order to increase the efficiency of committers and other contributors
- help raise the project's quality bar (e.g. by setting up code coverage analysis)

License
-------

The use and distribution terms for [Ostwind] are covered by the [Apache License, Version 2.0].

[Apache License Badge]: https://img.shields.io/badge/Apache%202.0-F25910.svg?style=for-the-badge&logo=Apache&logoColor=white
[Apache License, Version 2.0]: https://www.apache.org/licenses/LICENSE-2.0
[Ostwind]: https://ostwind.qubitpi.org/
[Ostwind Documentation]: https://ostwind.qubitpi.org/

[example application]: https://ostwind.qubitpi.org/docs/intro

[GitHub Workflow Status badge]: https://img.shields.io/github/actions/workflow/status/QubitPi/ostwind/ci-cd.yaml?branch=master&logo=github&style=for-the-badge
[GitHub Workflow Status URL]: https://github.com/QubitPi/Ostwind/actions/workflows/ci-cd.yaml
[GraphQL]: https://graphql.org/

[Hadoop HDFS]: https://qubitpi.github.io/hadoop/

[Java Version Badge]: https://img.shields.io/badge/Java-17-brightgreen?style=for-the-badge&logo=OpenJDK&logoColor=white
[JSON API]: https://qubitpi.github.io/json-api/

[Maven Central Version Badge]: https://img.shields.io/maven-central/v/io.github.qubitpi.ostwind/ostwind-parent-pom?style=for-the-badge&logo=apachemaven&labelColor=1B1C30&color=4D9FEA
[Maven Central Version URL]: https://central.sonatype.com/namespace/io.github.qubitpi.ostwind

[OpenStack Swift]: https://qubitpi.github.io/openstack-swift/
[OpenStack Swift's quick-start tutorial]: https://ostwind.qubitpi.org/docs/filestores/local-swift

[S3 File Store]: https://ostwind.qubitpi.org/docs/filestore#custom-stores

[yahoo/elide]: https://github.com/yahoo/elide
[yahoo/fili]: https://github.com/yahoo/fili
