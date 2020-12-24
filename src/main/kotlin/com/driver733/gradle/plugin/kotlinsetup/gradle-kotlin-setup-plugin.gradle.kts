package com.driver733.gradle.plugin.kotlinsetup

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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.1")
    implementation("org.awaitility:awaitility-kotlin:4.0.3")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.3.2")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.3.2")
    testImplementation("io.kotest:kotest-extensions-spring:4.3.2")

    testImplementation("io.mockk:mockk:1.10.3-jdk8")
    testImplementation("com.ninja-squad:springmockk:2.0.3")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.14.2")
}

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
                        if (version < 9) "1.$version" else "$version"
                    }
            freeCompilerArgs = listOf("-Xjsr305=strict")
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

