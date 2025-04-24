package module

import db.Products
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDatabase() {
    Database.connect("jdbc:h2:./data/db", driver = "org.h2.Driver") // H2 database
    transaction {
        SchemaUtils.create(Products) // Creates the table if it doesn't exist
    }
}