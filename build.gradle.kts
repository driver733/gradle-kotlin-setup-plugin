import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
    id("com.gradle.plugin-publish") version "0.11.0"
//    `maven-publish` // for debugging
}

group = "com.driver733"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${getKotlinPluginVersion()}")
    implementation("org.jetbrains.kotlin:kotlin-allopen:${getKotlinPluginVersion()}")
    implementation("io.freefair.gradle:lombok-plugin:5.0.0-rc6")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.2.6.RELEASE")
    implementation("io.spring.gradle:dependency-management-plugin:1.0.9.RELEASE")
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
    .find { it.name == "gradle-kotlin-setup-plugin" }!!
    .apply {
        id = "com.driver733.gradle-kotlin-setup-plugin"
        displayName = "A plugin that sets up kotlin in your project"
        description = "A plugin that sets up kotlin dependencies, plugins and build settings"
    }
