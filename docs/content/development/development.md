---
title: "Development"
date: 2025-06-26T19:16:15+05:30
draft: false
aliases:
  - /development/intro/
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

Ostwind is developed in [Jersey](https://eclipse-ee4j.github.io/jersey/) framework.

**NOTE:** In case you are not familiar with Jersey, it is a parallel technology with "Spring Boot framework". **Ostwind
offers absolutely NO support for Spring** and will remain as an exclusive Jersey application in the future, because
Jersey, alone with its backing technology [HK2](https://javaee.github.io/hk2/), is the reference-implementation of
JSR-370 (and HK2, JSR-330) _standards_ while Spring is not.

By "having no support for Spring", Ostwind means the following:

1. Ostwind DOES NOT, AND WILL NOT, run as a Spring Boot Webservice
2. Ostwind has ABSOLUTE ZERO direct-dependency from Spring
3. Ostwind runs in NON-SPRING containers, such as Jetty

_Ostwind rejects any conducts that violate the 3 rules above. NO EXCEPTION_.

Overview
--------

![Error loading class-diagram.png](../../images/class-diagram.png)


The following guide is intended to help developers who maintain or want to make changes to the Ostwind framework.

Building
--------

Ostwind is built using Maven. Because Ostwind is a mono-repo with interdependencies between modules, it is recommended to
fully build and install the project at least once:

```bash
mvn clean install
```

Thereafter, individual modules can be built whenever making changes to them. For example, the following command would
rebuild only ostwind-core:

```bash
mvn clean install -f ostwind-core
```

Pull requests and release builds leverage GitHub Action. PR builds simply run the complete build along with code
coverage.

Running Webservice in Standalone Jetty
--------------------------------------

### Download Jetty

For JDK **17**, which is the version JWT runs on, it's been tested that Jetty _11.0.15_ worked. Hence, we will use
["11.0.15" release](https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-home/11.0.15/jetty-home-11.0.15.tar.gz) as
an example:

![Error loading download-jetty-page.png](../../images/download-jetty-page.png)

Put the `tar.gz` file into a location of your choice as the installation path and extract the Jetty binary using

```bash
tar -xzvf jetty-home-11.0.15.tar.gz
```

The extracted directory *jetty-home-11.0.15* is the Jetty distribution. We call this directory **$JETTY_HOME**, which
should not be modified.

### Setting Up Standalone Jetty

Our [WAR file](#building) will be dropped to a directory where Jetty can pick up and run. We call this directory
**$JETTY_BASE**, which is usually different from the _$JETTY_HOME_. The _$JETTY_BASE_ also contains container runtime
configs. In short, the Standalone Jetty container will be setup with

```bash
export JETTY_HOME=/path/to/jetty-home-11.0.15
mkdir -p /path/to/jetty-base
cd /path/to/jetty-base
java -jar $JETTY_HOME/start.jar --add-module=annotations,server,http,deploy,servlet,webapp,resources,jsp
```

where `/path/to/` is the _absolute_ path to the directory containing the `jetty-home-11.0.15` directory

The `--add-module=annotations,server,http,deploy,servlet,webapp,resources,jsp` is how we configure the Jetty
container.

Lastly, drop the [WAR file](#building) into **/path/to/jetty-base/webapps** directory and rename the WAR file to
**ROOT.war**:

```bash
mv /path/to/war-file /path/to/jetty-base/webapps/ROOT.war
```

### Running Webservice

```bash
java -jar $JETTY_HOME/start.jar
```

The webservice will run on port **8080**, and you will see the data you inserted

Release Versions
----------------

Ostwind follows [semantic versioning](https://semver.org/) for its releases. Minor and patch versions only have the
version components of `MAJOR.MINOR.PATCH`.

Major releases are often pre-released prior to the publication of the final version.  Pre-releases have the format of
`MAJOR.MINOR.PATCH-prCANDIDATE`.  For example, 5.0.0-pr2 is release candidate 2 of the Ostwind 5.0.0 version.

FAQ
---

### I Use IntelliJ. Is There Any Way to Easily Sync up with Ostwind's Code styles

For the moment, we have distilled the most important code style conventions with respect to Ostwind's code as IntelliJ
settings. If you are using IntelliJ, you may import these code style settings by importing the
[Ostwind-Project-intellij-code-style.xml] file in the root of the repo. The setting for the project will appear as a new
Scheme named *Ostwind-Project* under IntelliJ's `Editor -> Code Style` section.

Alternatively, you might check the xml file that is included in the jar and map its settings to your development
environment.

Troubleshooting
---------------

### Checkstyle Error - "Extra lines between braces [RegexpMultiline]"

This is an Ostwind-custom checkstyle rule which simple disallow the following code snippet:

```java
    }

}
```

Basically, multiple lines between right curly braces is defined as a checkstyle violation. The error, however, might
still pops up with something _visually_ like this:

```java
    }
}
```

Note that no extra line can be seen in this reported case. The most probably cause might be a shared development
environment where one team member wrote code on Windows, which uses CRLF line endings, and the other uses UNIX/Mac.
We should [replace all CRLF endings with UNIX '\n' endings](https://stackoverflow.com/a/50765523/14312712).

[Ostwind-Project-intellij-code-style.xml]: https://github.com/QubitPi/Ostwind/blob/master/Ostwind-Project-intellij-code-style.xml
