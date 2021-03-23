package com.driver733.gradle.plugin.kotlinsetup

import gradle.kotlin.dsl.accessors._1b4c26afbd40f3a80ad253d03a3cfe3d.*
import io.freefair.gradle.plugins.lombok.tasks.Delombok
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.spring")
    id("io.freefair.lombok")
    id("io.gitlab.arturbosch.detekt")
}

dependencies {
    implementation(kotlin("reflect", "1.4.32"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.3")

    implementation("io.github.microutils:kotlin-logging:2.0.6")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.1") // do not update (yet). the update breaks tests due to a bug.
    implementation("org.awaitility:awaitility-kotlin:4.0.3")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.4.3")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.4.3")
    testImplementation("io.mockk:mockk:1.10.6")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.16.0")
}

afterEvaluate {
    if (hasSpringTestDep()) {
        dependencies {
            testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
            testImplementation("com.ninja-squad:springmockk:2.0.3")
        }
    }
}

fun hasSpringTestDep() =
    allprojects.any { proj ->
        proj.configurations.any { config ->
            config.dependencies.any { dep ->
                dep.isSpringTest()
            }
        }
    }

fun Dependency.isSpringTest() =
    group?.contains("spring") ?: false
            && name.contains("spring")
            && name.contains("test")

allprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

detekt {
    ignoreFailures = true
    failFast = false
    autoCorrect = false
    buildUponDefaultConfig = true
    config = files(configFilePath())

    reports {
        html.enabled = true
        xml.enabled = true
        txt.enabled = true
    }
}

tasks.find { it.name == "generateLombokConfig" }?.enabled = false

tasks
    .getByName("check")
    .dependsOn("detekt")

tasks
    .withType<KotlinCompile>()
    .configureEach {
        with(kotlinOptions) {
            jvmTarget =
                project
                    .convention
                    .getPlugin<JavaPluginConvention>()
                    .sourceCompatibility
                    .ordinal
                    .let {
                        val version = it + 1
                        if (version <= 8) "1.$version" else "$version"
                    }
            freeCompilerArgs =
                listOf(
                    "-Xjsr305=strict",
                    "-Xjvm-default=all-compatibility"
                )
            kapt.includeCompileClasspath = false
        }
    }

tasks
    .filter { it.name in setOf("compileJava", "compileTestJava") }
    .filter { it.enabled }
    .map { it as JavaCompile }
    .filter { it.source.asFileTree.files.isNotEmpty() }
    .forEach { task ->
        task.source =
            project
                .properties["delombok"]
                .let { it as Delombok }
                .target
                .asFileTree
    }

fun DetektExtension.configFilePath() =
    javaClass
        .getResourceAsStream("/config/detekt.yml")
        .bufferedReader(Charsets.UTF_8)
        .use { it.readText() }
        .let {
            createTempFile()
                .apply { appendText(it) }
                .path
        }

