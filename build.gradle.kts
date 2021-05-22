import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "0.11.0"
    // `maven-publish` // for debugging
}

group = "com.driver733"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

val KOTLIN_VERSION = findProperty("KOTLIN_VERSION").toString()
val KOTEST_VERSION = findProperty("KOTEST_VERSION").toString()

dependencies {
    implementation(kotlin("gradle-plugin", KOTLIN_VERSION))
    implementation(kotlin("allopen", KOTLIN_VERSION))
    implementation(kotlin("reflect", KOTLIN_VERSION))
    implementation("io.freefair.gradle:lombok-plugin:5.3.3.3")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.17.1")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:$KOTEST_VERSION")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$KOTEST_VERSION")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

pluginBundle {
    website = "https://github.com/driver733/gradle-kotlin-setup-plugin"
    vcsUrl = "https://github.com/driver733/gradle-kotlin-setup-plugin.git"
    tags = listOf("kotlin", "setup")
}

gradlePlugin
    .plugins
    .find { it.name == "com.driver733.gradle.plugin.kotlinsetup.gradle-kotlin-setup-plugin" }!!
    .apply {
        id = "com.driver733.gradle-kotlin-setup-plugin"
        displayName = "A plugin that sets up kotlin in your project"
        description = "A plugin that sets up kotlin dependencies, plugins and build settings"
    }
