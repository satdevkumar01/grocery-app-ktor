package com.sokhal.grocery

import com.sokhal.grocery.routing.configureRouting
import io.ktor.server.application.*
import module.initDatabase

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    initDatabase()
    configureRouting()
}
