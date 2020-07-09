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
    implementation(kotlin("stdlib-jdk8", "1.3.72"))
    implementation(kotlin("gradle-plugin", "1.3.72"))
    implementation(kotlin("allopen", "1.3.72"))
    implementation("io.freefair.gradle:lombok-plugin:5.0.0-rc6")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.1.0")
    testImplementation("io.kotest:kotest-runner-console-jvm:4.1.0")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.1.0")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
}

pluginBundle {
    website = "https://github.com/driver733/gradle-kotlin-setup-plugin"
    vcsUrl = "https://github.com/driver733/gradle-kotlin-setup-plugin.git"
    tags = listOf("kotlin", "setup")
}

gradlePlugin
    .plugins
    .find { it.name == "com.driver733.gradle-kotlin-setup-plugin" }!!
    .apply {
        id = "com.driver733.gradle-kotlin-setup-plugin"
        displayName = "A plugin that sets up kotlin in your project"
        description = "A plugin that sets up kotlin dependencies, plugins and build settings"
    }
