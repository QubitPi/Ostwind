---
title: "System Configuration"
date: 2025-06-26T04:01:38+05:30
draft: false
aliases: [/system-config.html]
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

Ostwind has two main configuration avenues, the domain object configuration (File Store, Meta Store, and Data Fetchers)
which happens via compiled Java code, and system configuration via properties. The domain configuration is
covered elsewhere, and we'll only cover the system configuration infrastructure here.

The system for property configuration that Ostwind uses lives in it's own [sub-module][ostwind-system-config]. This system
is extensible and reusable so that other Ostwind modules, and even other projects, can leverage it for their own property
config needs. That sub-module has it's own deep set of documentation, so we'll be focusing only on how to use it for
configuring Ostwind.

Configuration Sources and Overrides
-----------------------------------

Configuration for Ostwind modules come from only one location (that is, within the [sub-module][ostwind-system-config]
itself) and allows for overriding other settings. This is particularly useful when overriding a property set in a module
to turn off a feature, or to override a default configuration for your application in a certain environment, for
example.

Configuration sources are shown below, and are resolved in priority order, with higher-priority sources overriding
settings from lower-priority sources. Sources that are files will available to Ostwind on the Classpath for them to be
loaded.

| Priority |              Source              |                     Notes                     |
|:--------:|:--------------------------------:|:---------------------------------------------:|
| (High) 1 |      Environment variables       |                                               |
|    2     |         Java properties          |                                               |
|    3     |     `userConfig.properties`      |                                               |
|    5     |  `applicationConfig.properties`  |  Every application MUST provide one of these  |

{{<admonition bg-color="#00ee88">}}

Since `userConfig.properties` is often used while developing to turn features on and off, `.gitignore` includes
a  rule to ignore this file by default to help prevent checking it in accidentally.

{{</admonition>}}

[ostwind-system-config]: https://github.com/QubitPi/Ostwind/tree/master/ostwind-system-config
