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

dependencies {
    implementation(kotlin("gradle-plugin", "1.4.32"))
    implementation(kotlin("allopen", "1.4.32"))
    implementation("io.freefair.gradle:lombok-plugin:5.3.0")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.16.0")

    testImplementation(kotlin("reflect", "1.4.32"))
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.4.1")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.4.1")
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
