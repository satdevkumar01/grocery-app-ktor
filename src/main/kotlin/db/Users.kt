package db

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = integer("id").autoIncrement()
    val email = varchar("email", length = 100).uniqueIndex()
    val password = varchar("password", length = 255)
    val name = varchar("name", length = 100)
    val phone = varchar("phone", length = 20).nullable()
    val resetToken = varchar("reset_token", length = 255).nullable()
    val resetTokenExpiry = long("reset_token_expiry").nullable()
    override val primaryKey = PrimaryKey(id)
}