package com.driver733.gradle.plugin.kotlinsetup

open class KotlinSetupPluginExtension(
    var kotlinVersion: String = "1.5.0",
    var coroutinesVersion: String = "1.5.0",
    var kotestVersion: String = "4.6.0",
    var detektVersion: String = "1.17.1"
)