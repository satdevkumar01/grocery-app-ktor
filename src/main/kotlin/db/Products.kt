package db

import org.jetbrains.exposed.sql.Table

object Products : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", length = 100)
    val price = decimal("price", precision = 10, scale = 2)
    val quantity = integer("quantity")
    override val primaryKey = PrimaryKey(id)
}