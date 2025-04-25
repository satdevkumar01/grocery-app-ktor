package com.sokhal.grocery

import com.sokhal.grocery.routing.configureAuthRouting
import com.sokhal.grocery.routing.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import module.initDatabase

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true // Optional: Makes JSON responses easier to read
            isLenient = true   // Optional: Accepts non-strict JSON
            ignoreUnknownKeys = true // Optional: Ignores unknown fields in inbound JSON
        })
    }

    initDatabase()
    configureRouting()
    configureAuthRouting() // Add authentication routes
}
