import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.71"
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "0.11.0"
    id("maven-publish")
}

group = "com.driver733"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.71")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.3.71")
    implementation("io.freefair.gradle:lombok-plugin:5.0.0-rc6")
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

gradlePlugin {
    plugins {
        create("kotlin setup") {
            id = "com.driver733.gradle-kotlin-setup-plugin"
            displayName = "A plugin that sets up kotlin in your project"
            description = "A plugin that sets up kotlin dependencies, plugins and build settings"
            implementationClass = "com.driver733.gradle.plugin.GradleKotlinSetupPlugin"
        }
    }
}
