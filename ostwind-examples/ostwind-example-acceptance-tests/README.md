Ostwind Acceptance Test Framework
================================

![Cucumber Badge][Cucumber Badge]

Ostwind acceptance test framework is a slightly modified BDD, which
[eliminates the QA](https://spectrum.ieee.org/yahoos-engineers-move-to-coding-without-a-net)

The acceptances tests runs against the
[book example application](https://github.com/QubitPi/Ostwind/tree/master/ostwind-examples/ostwind-example-books), which
can be brought up using its dedicated Docker Compose:

```console
cd ostwind/ostwind-examples/ostwind-example-books/
docker compose up
```

Then navigate to the project root and run all acceptance tests with

```console
mvn clean verify -P acceptance-test
```

> [!NOTE]
> The `acceptance-test` profiles runs acceptance tests only and skips all Groovy Spock and IT tests.

[Cucumber Badge]: https://img.shields.io/badge/Cucumber-23D96C?style=for-the-badge&logo=cucumber&logoColor=white
