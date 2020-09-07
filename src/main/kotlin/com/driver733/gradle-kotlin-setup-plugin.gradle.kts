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
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx2:1.3.9")

    implementation("org.mapstruct:mapstruct:1.3.1.Final")
    implementation("com.github.pozo:mapstruct-kotlin:1.3.1.2")
    kapt("org.mapstruct:mapstruct-processor:1.3.1.Final")
    kapt("com.github.pozo:mapstruct-kotlin-processor:1.3.1.2")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.1")
    implementation("org.awaitility:awaitility-kotlin:4.0.3")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")

    testImplementation("junit:junit:4.13")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.6.2")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.2.3")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.2.3")
    testImplementation("io.kotest:kotest-property-jvm:4.2.3")
    testImplementation("io.kotest:kotest-extensions-spring:4.2.3")

    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("com.ninja-squad:springmockk:2.0.1")

    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.9")
    testRuntimeOnly("org.spekframework.spek2:spek-runtime-jvm:2.0.9")
}

tasks.test {
    useJUnitPlatform()
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
