---
title: "Configuration"
date: 2025-06-26T04:01:38+05:30
draft: false
aliases: [/configuration.html]
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

Configuration
=============

The configuration for Ostwind is implemented using ResourceConfig class programmatically wires up dependencies with a
[BinderFactory].

The configuration does not expose all the settings that can be customized. Some requires overriding of the injected
dependency in [AbstractBinderFactory], which offers the default dependency injection and resource binding.

The required bindings are

- a [FileStore] implementation class
- a [MetaStore] implementation class with dependencies of

  1. a [QueryDataFetcher][DataFetcher] for read operation and
  2. a [MutationDataFetcher][DataFetcher] for write operation

```java
import org.apache.commons.dbcp2.BasicDataSource;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import graphql.schema.DataFetcher;

import jakarta.inject.Provider;
import javax.sql.DataSource;

public class MyBinderFactory extends AbstractBinderFactory {

    @Override
    protected Class<? extends FileStore> buildFileStore() {
        return SwiftFileStore.class;
    }

    @Override
    protected Class<? extends MetaStore> buildMetaStore() {
        return GraphQLMetaStore.class;
    }

    @Override
    protected DataFetcher<MetaData> buildQueryDataFetcher() {

    }

    @Override
    protected DataFetcher<MetaData> buildMutationDataFetcher() {

    }
}
```

Any custom bindings can be achieved by overriding the `afterBinding`

```java
@Override
protected void afterBinding(final AbstractBinder abstractBinder) {
    // Custom bindings here...
}
```

Database
--------

A database should be initialized with:

```sql
CREATE DATABASE IF NOT EXISTS Ostwind;
USE Ostwind;

CREATE TABLE BOOK_META_DATA (
    id        int NOT NULL AUTO_INCREMENT,
    file_id   VARCHAR(255) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(8)   NOT NULL,
    PRIMARY KEY (id)
);
```

[AbstractBinderFactory]: https://github.com/QubitPi/Ostwind/blob/master/ostwind-core/src/main/java/io/github/qubitpi/ostwind/application/AbstractBinderFactory.java

[BinderFactory]: https://ostwind.qubitpi.org/apidocs/io/github/qubitpi/ostwind/application/BinderFactory.html

[DataFetcher]: https://graphql-java.qubitpi.org/documentation/data-fetching/

[MetaStore]: https://ostwind.qubitpi.org/apidocs/io/github/qubitpi/ostwind/metastore/MetaStore.html

[FileStore]: https://ostwind.qubitpi.org/apidocs/io/github/qubitpi/ostwind/filestore/FileStore.html
