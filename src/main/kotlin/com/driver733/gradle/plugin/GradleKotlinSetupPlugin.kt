package com.driver733.gradle.plugin

import io.freefair.gradle.plugins.lombok.LombokPlugin
import io.freefair.gradle.plugins.lombok.tasks.Delombok
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getPlugin
import org.jetbrains.kotlin.allopen.gradle.SpringGradleSubplugin
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class GradleKotlinSetupPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        val jvmTarget: String = project.convention.getPlugin<JavaPluginConvention>().sourceCompatibility.ordinal.let {
            val version = it + 1
            return@let if (version < 9) "1.$version" else "$version"
        }

        with(project) {
            with(plugins) {
                apply(KotlinPlatformJvmPlugin::class.java)
                apply(Kapt3GradleSubplugin::class.java)
                apply(SpringGradleSubplugin::class.java)
                apply(LombokPlugin::class.java)
            }
            tasks.find { it.name == "generateLombokConfig" }?.enabled = false
            dependencies {
                "implementation"("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
                "implementation"("org.jetbrains.kotlin:kotlin-reflect")
                "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-core")
                "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
                "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-test")
                "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
                "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
                "implementation"("org.jetbrains.kotlinx:kotlinx-coroutines-rx2")
                "implementation"("org.awaitility:awaitility-kotlin")
                "implementation" ("com.fasterxml.jackson.module:jackson-module-kotlin")

                "api"("com.github.pozo:mapstruct-kotlin:1.3.1.1")

                "kapt"("org.mapstruct:mapstruct-jdk8:1.3.1.Final")
                "kapt"("com.github.pozo:mapstruct-kotlin-processor:1.3.1.1")

                "testImplementation"("org.jetbrains.kotlin:kotlin-test")
                "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit")
                "testImplementation"("org.jetbrains.kotlin:kotlin-test-junit5")
                "testImplementation"("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
            }
            tasks.withType(KotlinCompile::class.java).configureEach {
                kotlinOptions.jvmTarget = jvmTarget
                kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
            }
            tasks.withType(JavaCompile::class.java).configureEach {
                source = (project.properties["delombok"] as Delombok).target.asFileTree
            }
        }
    }

}