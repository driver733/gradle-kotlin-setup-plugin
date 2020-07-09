package com.driver733

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContainIgnoringCase
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

class GradleKotlinSetupPluginTest : FunSpec() {

    private val projectDir = createTempDir().apply {
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
            val actual = buildResult("build")

            actual.output shouldContainIgnoringCase "kotlin"
            actual.assertSuccess(":build")
        }

        test("dependencies") {
            val actual = buildResult("dependencies")

            actual.output shouldContainIgnoringCase "org.jetbrains.kotlin"
            actual.output shouldContainIgnoringCase "kotest"
            actual.output shouldContainIgnoringCase "mockk"
            actual.assertSuccess(":dependencies")
        }

        test("tasks") {
            val actual = buildResult("tasks")

            actual.output shouldContainIgnoringCase "Build tasks"
            actual.output shouldContainIgnoringCase "Lombok tasks"
            actual.assertSuccess(":tasks")
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




