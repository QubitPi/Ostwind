---
title: "GraphQL"
date: 2025-06-26T04:01:38+05:30
draft: false
aliases: [/graphql.html]
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

Graph APIs are an evolution of web service APIs that serve and manipulate data for mobile & web applications. They have
a number of characteristics that make them well suited to this task:

1. Most notably, they present a **data model** as an entity relationship graph and an **accompanying schema**.

    - A well-defined model allows for a consistent view of the data and a centralized way to manipulate an instance of
      the model or to cache it.
    - The schema provides powerful introspection capabilities that can be used to build tools to help developers
      understand and navigate the model

2. The API allows the client to **fetch or mutate as much or as little information in single roundtrip** between client
   and server. This also shrinks payload sizes and simplifies the process of schema evolution
3. There is a **well-defined standard** for the API that fosters a community approach to development of supporting tools
   & best practices.
