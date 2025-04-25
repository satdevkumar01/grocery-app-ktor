val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.1.2"
    kotlin("plugin.serialization") version "2.1.10" // Use the same version as your Kotlin language

}

group = "com.sokhal.grocery"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
        // Ktor Core and Netty Engine
        implementation("io.ktor:ktor-server-core:2.3.4")
        implementation("io.ktor:ktor-server-netty:2.3.4")

        // Content Negotiation with Kotlin Serialization
        implementation("io.ktor:ktor-server-content-negotiation:2.3.4")
        implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")

        // Database (H2 in this example)
        implementation("com.h2database:h2:2.2.224")

        // Exposed ORM
        implementation("org.jetbrains.exposed:exposed-core:0.43.0")
        implementation("org.jetbrains.exposed:exposed-dao:0.43.0")
        implementation("org.jetbrains.exposed:exposed-jdbc:0.43.0")

        // Kotlin Serialization JSON
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

        // Logging (Optional, for better server logging)
        implementation("ch.qos.logback:logback-classic:1.4.11")


    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
