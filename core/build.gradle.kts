val exposed_version: String by project
val h2_version: String by project
val koin_version: String by project
val kotlin_version: String by project
val kotlinx_browser_version: String by project
val kotlinx_html_version: String by project
val kotlinx_rpc_version: String by project
val ktor_version: String by project
val logback_version: String by project
val mongo_version: String by project
val postgres_version: String by project
val prometheus_version: String by project

plugins {
    kotlin("multiplatform") version "2.1.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.10"
    id("org.jetbrains.kotlinx.rpc.plugin") version "0.6.0"
}

kotlin {
    jvm()
    wasmJs()

    sourceSets {
        commonMain.dependencies {
            api("org.jetbrains.kotlinx:kotlinx-rpc-krpc-serialization-json:$kotlinx_rpc_version")
            api("org.jetbrains.kotlinx:kotlinx-rpc-core:$kotlinx_rpc_version")
        }
    }
}
