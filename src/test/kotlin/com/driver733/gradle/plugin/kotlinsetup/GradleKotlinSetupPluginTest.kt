package com.driver733.gradle.plugin.kotlinsetup

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContainIgnoringCase
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

class GradleKotlinSetupPluginTest : FunSpec() {

    private val projectDir =
        createTempDir().apply {
            resolve("setting.gradle.kts").apply {
                appendText("rootProject.name = \"gradle-kotlin-setup-plugin-test\"")
            }
            resolve("build.gradle.kts").apply {
                appendText(
                    """
                    plugins {
                        id("com.driver733.gradle-kotlin-setup-plugin")
                    }
                    repositories {
                        mavenCentral()
                    }
            """
                )
            }
        }

    init {

        test("build") {
            with(buildResult("build")) {
                output shouldContainIgnoringCase "kotlin"
                assertSuccess(":build")
            }
        }

        test("test") {
            with(buildResult("test")) {
                tasks.first { it.path == ":test" }.outcome shouldBe TaskOutcome.NO_SOURCE
            }
        }

        test("kapt") {
            with(buildResult("kaptKotlin")) {
                tasks.first { it.path == ":kaptKotlin" }.outcome shouldBe TaskOutcome.UP_TO_DATE
            }
        }

        test("detekt") {
            with(buildResult("detekt")) {
                tasks.first { it.path == ":detekt" }.outcome shouldBe TaskOutcome.NO_SOURCE
            }
        }

        test("dependencies") {
            with(buildResult("dependencies")) {
                output shouldContainIgnoringCase "org.jetbrains.kotlin"
                output shouldContainIgnoringCase "kotest"
                output shouldContainIgnoringCase "mockk"
                assertSuccess(":dependencies")
            }
        }

        test("tasks") {
            with(buildResult("tasks")) {
                output shouldContainIgnoringCase "Build tasks"
                output shouldContainIgnoringCase "Lombok tasks"
                assertSuccess(":tasks")
            }
        }

    }

    private fun BuildResult.assertSuccess(task: String) {
        task(task)?.outcome shouldBe TaskOutcome.SUCCESS
        output.contains("BUILD SUCCESSFUL")
    }

    private fun buildResult(vararg args: String) =
        GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments(*args)
            .withPluginClasspath()
            .build()
}
