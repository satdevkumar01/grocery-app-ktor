package com.sokhal.grocery.routing

import data.Product
import db.Products
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

fun Application.configureRouting() {
    routing {
        // Get All Products
        get("/products") {
            val products = transaction {
                Products.selectAll().map {
                    Product(
                        id = it[Products.id],
                        name = it[Products.name],
                        price = it[Products.price].toDouble(),
                        quantity = it[Products.quantity]
                    )
                }
            }
            if (products.isEmpty()) {
                call.respond("There is no Items at now please try after some time s")
            }
            else {call.respond(products)}

        }

        // Add a Product
        post("/addproducts") {
            var productId: Int? = null
            transaction {
                productId = Products.insert {
                    it[name] = "Rice"
                    it[price] =100.00.toBigDecimal()
                    it[quantity] = 11
                } get Products.id
            }
            call.respond(HttpStatusCode.Created, mapOf("new product is created id" to productId))
        }

        // Update a Product
        put("/products/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val updatedProduct = call.receive<Product>()
            if (id != null) {
                val updated = transaction {
                    Products.update({ Products.id eq id }) {
                        it[name] = updatedProduct.name
                        it[price] = updatedProduct.price.toBigDecimal()
                        it[quantity] = updatedProduct.quantity
                    }
                }
                if (updated > 0) {
                    call.respond(HttpStatusCode.OK, "Product updated successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Product not found")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            }
        }

        // Delete a Product
        delete("/products/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id != null) {
                val deleted = transaction { Products.deleteWhere { Products.id eq id } }
                if (deleted > 0) {
                    call.respond(HttpStatusCode.OK, "Product deleted successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Product not found")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
            }
        }
    }
}