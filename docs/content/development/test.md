---
title: "Testing"
date: 2025-06-26T19:16:15+05:30
draft: false
aliases:
- /development/test/
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

Groovy Spock
------------

We're _big_ believers in testing our code, both for correctness, as well as to ensure that changes don't unintentionally
break existing contracts unintentionally. For example, we rely heavily on the [Spock]
framework for our backend service tests, and see a lot of benefit from it's conciseness, built-in
[mocking framework], and the fact that it uses [Groovy].

We also strive for very high-quality code, with the belief that quality code is easier to maintain, easier to
understand, and has fewer bugs. To help keep the quality bar high. For instance we have an automated style checker
([Checkstyle]) in our Maven-based projects with rules that _should_ catch most of the common style issues.

Database
--------

Database-related tests contain 2 parts

1. Groovy Spock unit tests on

   - [Injected Query DataFetcher](../../../ostwind-examples/ostwind-example-books/src/test/groovy/io/github/qubitpi/ostwind/example/books/application/SQLQueryDataFetcherSpec.groovy)
   - [Injected Mutation DataFetcher](../../../ostwind-examples/ostwind-example-books/src/test/groovy/io/github/qubitpi/ostwind/example/books/application/SQLMutationDataFetcherSpec.groovy)

2. Live DB tests on endpoints

   - In [file servlet endpoint test](../../../ostwind-examples/ostwind-example-books/src/test/groovy/io/github/qubitpi/ostwind/example/books/web/endpoints/FileServletSpec.groovy)
     and
     [meta data servlet endpoint test](../../../ostwind-examples/ostwind-example-books/src/test/groovy/io/github/qubitpi/ostwind/example/books/web/endpoints/MetaServletSpec.groovy),
     [Flyway migration](../../../ostwind-examples/ostwind-example-books/src/test/groovy/io/github/qubitpi/ostwind/example/books/application/SQLDBResourceManager.groovy)
     injects real data into a Derby in-meomroy SQL DB
   - The Derby data is injected via a shared [DBCP DataSource](#reference---apache-commons-dbcp2) declared in
     [application BinderFactory](../../../ostwind-examples/ostwind-example-books/src/main/java/io/github/qubitpi/ostwind/example/books/application/BooksBinderFactory.java)
   - The application resource is set alive through `JettyServerFactory`

### Reference - Apache Commons DBCP2

For testing Book example application, we use Derby's in-memory database facility, which resides completely in main
memory, not in the file system.

In addition, we use [Apache DBCP 2](https://commons.apache.org/proper/commons-dbcp/) to provide
[DataSource](https://gitbox.apache.org/repos/asf?p=commons-dbcp.git;a=blob_plain;f=doc/BasicDataSourceExample.java;hb=HEAD)
pointing at the in-memory Derby instance.

### Derby

Derby was meant to be used only in tests and, hence, must be imported in test scope only

[Checkstyle]: http://checkstyle.sourceforge.net/

[Groovy]: http://www.groovy-lang.org/

[mocking framework]: http://spockframework.org/spock/docs/1.1-rc-2/interaction_based_testing.html

[Spock]: http://spockframework.org/
