package com.driver733

import io.freefair.gradle.plugins.lombok.tasks.Delombok
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.spring")
    id("io.freefair.lombok")
}

tasks.find { it.name == "generateLombokConfig" }?.enabled = false

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.71")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.71")
    implementation("org.gradle.kotlin:plugins:1.3.5")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx2")

    implementation("org.awaitility:awaitility-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    api("com.github.pozo:mapstruct-kotlin:1.3.1.1")

    kapt("org.mapstruct:mapstruct-jdk8:1.3.1.Final")
    kapt("com.github.pozo:mapstruct-kotlin-processor:1.3.1.1")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}

tasks.withType(KotlinCompile::class.java).configureEach {
    kotlinOptions.jvmTarget = project.convention.getPlugin<JavaPluginConvention>().sourceCompatibility.ordinal
        .let {
            val version = it + 1
            return@let if (version < 9) "1.$version" else "$version"
        }
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}

tasks.filter { listOf("compileJava", "compileTestJava").contains(it.name) }
    .filter { it.enabled }
    .map { it as JavaCompile }
    .filter { it.source.asFileTree.files.isNotEmpty() }
    .forEach { task ->
        task.source = project.properties["delombok"].let { it as Delombok }.target.asFileTree
    }
