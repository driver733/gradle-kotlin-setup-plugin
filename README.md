# Gradle Kotlin Setup Plugin

[![Build](https://github.com/driver733/gradle-kotlin-setup-plugin/workflows/Build/badge.svg?branch=master)](https://github.com/driver733/gradle-kotlin-setup-plugin/actions?query=workflow%3ABuild+branch%3Amaster)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v?label=Gradle%20Plugin%20Portal&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fcom%2Fdriver733%2Fgradle-kotlin-setup-plugin%2Fcom.driver733.gradle-kotlin-setup-plugin.gradle.plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/com.driver733.gradle-kotlin-setup-plugin)

[![semantic-release](https://img.shields.io/badge/%20%20%F0%9F%93%A6%F0%9F%9A%80-semantic--release-e10079.svg)](https://github.com/driver733/gradle-kotlin-setup-plugin/actions?query=workflow%3ARelease)

[![Licence](https://img.shields.io/github/license/driver733/gradle-kotlin-setup-plugin)](https://github.com/driver733/gradle-kotlin-setup-plugin/blob/master/LICENSE)

This plugin makes it easy to set up Koltin in a new project as well as in an existing Java project.

More specifically this plugin does the following:

1. Applies the `kotlin-jvm` and `KAPT` gradle plugins
2. Adds common Kotlin dependencies, such as:
    1. `kotlin-stdlib-jdk8`
    2. `kotlin-reflect`
    3. `kotlin-coroutines`
    4. [`kotest` (with extensions)](https://github.com/kotest/kotest)
    5. [`mockk`](https://github.com/mockk/mockk)
    6. [`Skek Framework` (with extensions)](https://github.com/spekframework/spek/)
    5. [`mockito-kotlin`](https://github.com/nhaarman/mockito-kotlin)
    6. and [others](https://github.com/driver733/gradle-kotlin-setup-plugin/blob/master/src/main/kotlin/com/driver733/gradle-kotlin-setup-plugin.gradle.kts)
3. Configures `kotlinOptions.jvmTarget` to match the `JavaPlugin.sourceCompatibility`
4. Resolves the Kotlin's [incompatibility](https://stackoverflow.com/a/35530223/2441104) with Lombok by delomboking
the project's java source code and pointing the `JavaPlugin` compile tasks to the delomboked source code files.
5. Sets up the `test` task to use `JUnit Platform` (aka JUnit 5).

## Distribution

The plugin is [available](https://plugins.gradle.org/plugin/com.driver733.gradle-kotlin-setup-plugin) on the Gradle Plugins portal.

## Getting Started

### Requirements

Gradle >= `v. 6.0` is required to use this plugin, because it is a
[precompiled script plugin](https://docs.gradle.org/current/userguide/kotlin_dsl.html#kotdsl:precompiled_plugins)
built with Gradle >= `v. 6.0`.

### Install

#### Groovy DSL

Add this to your project's `build.gradle`:

```
plugins {
  id "com.driver733.gradle-kotlin-setup-plugin" version "1.2.2"
}
```

#### Kotlin DSL

Add this to your project's `build.gradle.kts`:

```
plugins {
  id("com.driver733.gradle-kotlin-setup-plugin") version "1.2.2"
}
```

## Development

### Prerequisites

[JDK](https://stackoverflow.com/a/52524114/2441104), preferably >= `v. 1.8`

### Build

```
./gradlew clean build
```

### CI/CD

[Github actions](https://github.com/driver733/gradle-kotlin-setup-plugin/actions) is used for CI/CD.

### Releases

Releases to the Gradle Plugins portal are [automatically](https://github.com/driver733/gradle-kotlin-setup-plugin/actions?query=workflow%3ARelease) made on each commit on the master branch with the help of the [semantic-release](https://github.com/semantic-release/semantic-release).

## Contributing

1. Create an issue and describe your problem/suggestion in it.
2. Submit a pull request with a reference to the issue that the pull request closes.
3. I will review your changes and merge them.
4. A new version with your changes will be released automatically right after merging.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags](https://github.com/driver733/gradle-kotlin-setup-plugin/tags). 

## Authors

* **Mikhail [@driver733](https://www.driver733.com) Yakushin** - *Initial work*

See also the list of [contributors](https://github.com/driver733/gradle-kotlin-setup-plugin/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/driver733/gradle-kotlin-setup-plugin/blob/master/LICENSE) file for details.

## Acknowledgments

* [Gradle lombok plugin](https://plugins.gradle.org/plugin/io.freefair.lombok)
* [KengoTODA/gradle-semantic-release-plugin](https://github.com/KengoTODA/gradle-semantic-release-plugin)
* [semantic-release](https://github.com/semantic-release/semantic-release)
