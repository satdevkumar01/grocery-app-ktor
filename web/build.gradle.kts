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
}

kotlin {
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "web.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        wasmJsMain.dependencies {
            implementation(npm("htmx.org", "2.0.3"))
            implementation("org.jetbrains.kotlinx:kotlinx-browser:$kotlinx_browser_version")
        }
    }
}
