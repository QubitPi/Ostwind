---
title: "Getting Started"
date: 2025-06-26T04:01:38+05:30
draft: false
aliases: [/intro.html]
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

The easiest way to get started with Ostwind is to use the [Ostwind Book App Starter]. The starter bundles all of the
dependencies we will need to stand up a web service. This tutorial uses the starter, and all of the code is
[available here][Ostwind Book App Starter]. We will deploy and play with this example locally

Docker Compose
--------------

Ostwind Compose is a tool for setting up and running a full-fledged Ostwind instance Docker application. With Compose,
an Ostwind application is backed by a real MySQL meta store and an in-memory OpenStack Swift service. With a single
command, we will be able to create and start all the services from Ostwind. **It's the quickest approach to get a taste
of Ostwind**.

Ostwind Compose works in all environments: production, staging, development, testing, as well as CI workflows. You can
learn more about it from [source code][Ostwind Book App Starter].

Using Ostwind Compose is basically a three-step process:

1. Package Ostwind at project root with `mvn clean package`
2. cd into [compose top directory][Ostwind Book App Starter] and fire-up `docker compose up`
3. hit Ostwind at `http://localhost:8080/v1/metadata/graphql?query={metaData(fileId:%221%22){fileName}}` with your favorite
   browser

For more information about the Ostwind Compose the [Compose file definition][Ostwind Book App Starter].

Ostwind Compose has ability for managing the whole lifecycle of an Ostwind application:

- Start, stop, and rebuild services
- View the status of running services
- Stream the log output of running services
- Run a one-off command on a service

### Extending Ostwind Compose

Happy with Ostwind? You can go further with productionizing Ostwind from
here <img src="https://user-images.githubusercontent.com/16126939/174438007-b9adae25-baf8-42a7-bf39-83786435d397.gif" width="40"/>

{{<admonition title="Business Scenario Testing" bg-color="#00ee88">}}

Ostwind also comes with
[an example acceptance testing module](https://github.com/QubitPi/Ostwind/tree/master/ostwind-examples/ostwind-example-acceptance-tests)
which can be pull out separately for our own project needs.

{{</admonition>}}

If you would like to go from basic Ostwind Compose setup and changed anything, rebuild it with

```bash
docker compose up --build --force-recreate
```

Ostwind Compose has been tested with [MySQL 5.7](https://hub.docker.com/_/mysql) connected using
_mysql-connector-java 5.1.38_ within Ostwind running on
[Jetty 11.0.15](https://download.eclipse.org/oomph/jetty/release/11.0.15/).

{{<admonition title="Warning" bg-color="#ff4545">}}

Please take extra caution with MySQL 8, as some of the features might not work properly on Ostwind Compose. In
addition, make sure `?autoReconnect=true&useSSL=false` is in connection string. For example,
`jdbc:mysql://db:3306/Ostwind?autoReconnect=true&useSSL=false`

{{</admonition>}}

### MySQL Container (Meta Store)

Ostwind Compose uses MySQL 5 as the backing meta store, i.e. the database that DataFetcher is talking to for file
metadata.

The MySQL instance is network-reachable at 3306 inside compose and 3305 for host (wo choose 3305 just in case 3306 has
already been occupied)

### Networking in Ostwind Compose

By default Ostwind Compose sets up a single
[network](https://docs.docker.com/engine/reference/commandline/network_create/). Both Ostwind and MySQL container
services join this default network and is both reachable by other containers on that network, and discoverable by them
at a hostname identical to the container name.

For example, inside [docker-compose.yml][docker-compose.yml]

```yaml
services:
  web:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      db:
        condition: service_healthy
  db:
    image: "mysql:5.7"
    ports:
      - "3305:3306"
    volumes:
      - "./mysql-init.sql:/docker-entrypoint-initdb.d/mysql-init.sql"
    environment:
      MYSQL_ROOT_PASSWORD: root
    healthcheck:
      test: mysqladmin ping -h localhost -u root -proot
      timeout: 3s
      retries: 3
```

When you run docker compose up, the following happens:

- A network called "ostwind-example-books" is created.
- An Ostwind container is created using ostwind-example-books configuration. It joins the network "ostwind-example-books"
  under the name "web".
- An MySQL container is created using `db`'s configuration. It joins the network "ostwind-example-books" under the name
  "db".

Each container can now look up the hostname `web` or `db` and get back the appropriate container's IP address. For
example, web's application code could connect to the URL `mysql://db:3306` and start using the MySQL database.

Production
----------

### Creating a Deployable War File

We build the ".war" File first by navigating to ostwind project root and compile the project

```bash
cd ostwind/
mvn clean package
```

Successfully executing the command above shall generate a ".war" file under
`ostwind/ostwind-examples/ostwind-example-books/target/ostwind-example-books-<ostwind-version>.war`, where is the version of
the ostwind, for example `1.0.2`, please make sure to replace `<ostwind-version>` with one of
[our release versions](https://central.sonatype.com/namespace/io.github.qubitpi.ostwind).

### Setting Up Jetty

#### Downloading Jetty

At [download page](https://www.eclipse.org/jetty/download.php), pick up a `.tgz` distribution, we will use
"9.4.44.v20210927" release as an example:

![Error loading download-jetty.png](../../images/download-jetty.png)

#### Installing Jetty

Put the `tar.gz` file into a preferred location as the installation path and extract the Jetty binary using

```bash
tar -czvf jetty-distribution-9.4.44.v20210927.tar.gz
```

#### Dropping the ".war" File into the Jetty "webapp"

```bash
cd jetty-distribution-9.4.44.v20210927/webapps/
mv /path/to/.war .
```

Then rename the war file to "ROOT.war", the reason of which is so that the context path would be root context - `/`,
which is a common industry standard.

{{<admonition title="Setting a Context Path" bg-color="#00ee88">}}

The context path is the prefix of a URL path that is used to select the context(s) to which an incoming request is
passed. Typically a URL in a Java servlet server is of the format
`http://hostname.com/contextPath/servletPath/pathInfo`, where each of the path elements can be zero or more "/"
separated elements. If there is no context path, the context is referred to as the **root context**. The root context
must be configured as "/" but is reported as the empty string by the servlet
[API `getContextPath()` method](https://www.eclipse.org/jetty/).

How we set the context path depends on how we deploy the web application (or `ContextHandler`). In this case, we
configure the context path by **naming convention**:

If a web application is deployed using the WebAppProvider of the DeploymentManager without an XML IoC file, then **the
name of the WAR file is used to set the context path**:

- If the WAR file is named "myapp.war", then the context will be deployed with a context path of `/myapp`
- __If the WAR file is named "ROOT.WAR" (or any case insensitive variation), then the context will be deployed with a
  context path of `/`__
- If the WAR file is named "ROOT-foobar.war" (or any case insensitive variation), then the context will be deployed
  with a context path of / and a
  [virtual host](https://www.eclipse.org/jetty/documentation/jetty-9/index.html#configuring-virtual-hosts) of "foobar"

{{</admonition>}}

### Starting the Webservice

```bash
cd ../
java -jar start.jar
```

{{<admonition bg-color="#00ee88">}}

To specify the port that container exposes for our app, we could use

```bash
java -jar start.jar -Djetty.port=8081
```

{{</admonition>}}

### Firing The First Request

```bash
brew install --cask graphiql
```

[Ostwind Book App Starter]: https://github.com/QubitPi/Ostwind/tree/master/ostwind-examples/ostwind-example-books

[docker-compose.yml]: https://github.com/QubitPi/Ostwind/tree/master/ostwind-examples/ostwind-example-books/docker-compose.yml
[ostwind-demo]: https://github.com/QubitPi/Ostwind/tree/master/ostwind-examples/ostwind-example-books
[swagger-ui]: https://swagger.io/tools/swagger-ui/
