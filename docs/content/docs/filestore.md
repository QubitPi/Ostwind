---
title: "File Stores"
date: 2025-06-26T04:01:38+05:30
draft: false
aliases: [/filestore.html]
---

<!--
Copyright 2025 Jiaqi Liu. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

A file store is responsible for:

1. reading and writing files (.mp3, .pdf, etc.) to/from an object store. Files of the following types are supported by
   Ostwind

   - PDF
   - MP4
   - .txt file

2. providing "transactions" that make all file operations atomic in a single request.
3. declaring the native object store client it delegates persistence operations to.

If a file store implementation is unable to handle a file InputStream, Ostwind pushes these responsibilities to the
object store.

Included Stores
---------------

Ostwind comes bundled with a number of file stores:

1. Swift Store - A file store that can map operations on a file to an underlying OpenStack Swift API. Ostwind has
   explicit support for Swift
2. HDFS Store - File is persisted on Hadoop HDFS.

{{<admonition bg-color="#00ee88">}}

It is assumed that the "HDFS Store" means a **single-cluster** HDFS. However, the Ostwind architecture does not preclude
implementing a multi-cluster HDFS store

{{</admonition>}}

Stores can be included through the following artifact dependencies:

### Swift Store

```xml

<dependency>
    <groupId>io.github.qubitpi.ostwindio.github.qubitpi.ostwind</groupId>
    <artifactId>ostwind-filestore-swift</artifactId>
    <version>${version.ostwind}</version>
</dependency>
```

### HDFS Store

```xml

<dependency>
    <groupId>io.github.qubitpi.ostwindio.github.qubitpi.ostwind</groupId>
    <artifactId>ostwind-filestore-hdfs</artifactId>
    <version>${version.ostwind}</version>
</dependency>
```

Overriding the Store
--------------------

To change the store, override the `FileStore` binding. For example, to use a store called `SomeCustomFileStore`:

```java
public class AppBinderFactory extends AbstractBinderFactory {

    ...

    @Override
    protected Class<? extends FileStore> buildFileStore() {
        return SomeCustomFileStore.class;
    }
}
```

Custom Stores
-------------

Custom stores can be written by implementing the `FileStore` interface. Take Amazon S3 for instance

```java
@Singleton
public class S3FileStore implements FileStore {

    private final AmazonS3 s3client;
    private final FileIdGenerator fileIdGenerator;

    @Inject
    public S3FileStore(@NotNull final AmazonS3 s3client, @NotNull final FileIdGenerator fileIdGenerator) {
        this.s3client = Objects.requireNonNull(s3client);
        this.fileIdGenerator = Objects.requireNonNull(fileIdGenerator);
    }

    @Override
    public String upload(final File file) {
        final String fileId = fileIdGenerator.apply(file);

        s3client.putObject(
                file.getMetaData().getFileType().name(),
                fileId,
                file.getFileContent(),
                new ObjectMetadata()
        );

        return fileId;
    }

    @Override
    public InputStream download(final String fileId) {
       return getS3client().getObject(...);
    }
}
```

Multiple Stores
---------------

A common pattern in Ostwind is the need to support multiple file stores. Typically, one file store manages most files,
but some others may require a different object storage backend or have other needs to specialize the behavior of the
store. The multiplex store in Ostwind manages multiple stores - delegating calls to the appropriate store which is
responsible for a particular file.

This is a [feature](https://trello.com/c/bHwNl4sk) yet to be offered soon.
