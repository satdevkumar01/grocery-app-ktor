package com.sokhal.grocery.routing

import data.*
import db.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.security.MessageDigest
import java.util.*
import kotlin.random.Random

fun Application.configureAuthRouting() {
    routing {
        // Registration endpoint
        post("/auth/register") {
            val registerRequest = call.receive<RegisterRequest>()

            // Check if email already exists
            val emailExists = transaction {
                Users.select { Users.email eq registerRequest.email }.count() > 0
            }

            if (emailExists) {
                call.respond(HttpStatusCode.Conflict, "Email already registered")
                return@post
            }

            // Hash the password
            val hashedPassword = hashPassword(registerRequest.password)

            // Insert new user
            val userId = transaction {
                Users.insert {
                    it[email] = registerRequest.email
                    it[password] = hashedPassword
                    it[name] = registerRequest.name
                    it[phone] = registerRequest.phone
                } get Users.id
            }

            // Return the created user (without password)
            val user = transaction {
                Users.select { Users.id eq userId }.map {
                    User(
                        id = it[Users.id],
                        email = it[Users.email],
                        password = "", // Don't send the actual password
                        name = it[Users.name],
                        phone = it[Users.phone],
                        resetToken = it[Users.resetToken],
                        resetTokenExpiry = it[Users.resetTokenExpiry]
                    )
                }.single()
            }

            call.respond(HttpStatusCode.Created, user)
        }

        // Login endpoint
        post("/auth/login") {
            val loginRequest = call.receive<LoginRequest>()

            // Hash the provided password
            val hashedPassword = hashPassword(loginRequest.password)

            // Find user by email and password
            val user = transaction {
                Users.select { 
                    (Users.email eq loginRequest.email) and (Users.password eq hashedPassword)
                }.map {
                    User(
                        id = it[Users.id],
                        email = it[Users.email],
                        password = "", // Don't send the actual password
                        name = it[Users.name],
                        phone = it[Users.phone],
                        resetToken = it[Users.resetToken],
                        resetTokenExpiry = it[Users.resetTokenExpiry]
                    )
                }.singleOrNull()
            }

            if (user != null) {
                // Generate a simple token (in a real app, use a proper JWT library)
                val token = generateToken(user.id!!)
                call.respond(LoginResponse(token = token, user = user))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }

        // Forgot password endpoint
        post("/auth/forgot-password") {
            val request = call.receive<ForgotPasswordRequest>()

            // Find user by email
            val userExists = transaction {
                Users.select { Users.email eq request.email }.count() > 0
            }

            if (!userExists) {
                // For security reasons, don't reveal if the email exists or not
                call.respond(HttpStatusCode.OK, "If your email is registered, you will receive a password reset link")
                return@post
            }

            // Generate reset token
            val resetToken = UUID.randomUUID().toString()
            val resetTokenExpiry = System.currentTimeMillis() + (24 * 60 * 60 * 1000) // 24 hours from now

            // Update user with reset token
            transaction {
                Users.update({ Users.email eq request.email }) {
                    it[Users.resetToken] = resetToken
                    it[Users.resetTokenExpiry] = resetTokenExpiry
                }
            }

            // In a real app, send an email with the reset link
            // For this example, we'll just return the token in the response
            call.respond(HttpStatusCode.OK, "If your email is registered, you will receive a password reset link. Token: $resetToken")
        }

        // Reset password endpoint
        post("/auth/reset-password") {
            val request = call.receive<ResetPasswordRequest>()

            // Find user by reset token and check if token is not expired
            val user = transaction {
                Users.select { 
                    (Users.resetToken eq request.token) and 
                    (Users.resetTokenExpiry greater System.currentTimeMillis())
                }.map {
                    User(
                        id = it[Users.id],
                        email = it[Users.email],
                        password = it[Users.password],
                        name = it[Users.name],
                        phone = it[Users.phone],
                        resetToken = it[Users.resetToken],
                        resetTokenExpiry = it[Users.resetTokenExpiry]
                    )
                }.singleOrNull()
            }

            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid or expired reset token")
                return@post
            }

            // Hash the new password
            val hashedPassword = hashPassword(request.newPassword)

            // Update user's password and clear reset token
            transaction {
                user.id?.let { userId ->
                    Users.update({ Users.id eq userId }) {
                        it[password] = hashedPassword
                        it[resetToken] = null
                        it[resetTokenExpiry] = null
                    }
                }
            }

            call.respond(HttpStatusCode.OK, "Password has been reset successfully")
        }

        // Update user endpoint (requires authentication)
        put("/auth/user") {
            // In a real app, get the user ID from the authentication token
            // For this example, we'll get it from a header
            val userId = call.request.headers["X-User-ID"]?.toIntOrNull()

            if (userId == null) {
                call.respond(HttpStatusCode.Unauthorized, "Authentication required")
                return@put
            }

            val updateRequest = call.receive<UpdateUserRequest>()

            // Check if user exists
            val userExists = transaction {
                Users.select { Users.id eq userId }.count() > 0
            }

            if (!userExists) {
                call.respond(HttpStatusCode.NotFound, "User not found")
                return@put
            }

            // Update user
            transaction {
                Users.update({ Users.id eq userId }) {
                    updateRequest.name?.let { name -> it[Users.name] = name }
                    updateRequest.phone?.let { phone -> it[Users.phone] = phone }
                    updateRequest.email?.let { email -> it[Users.email] = email }
                }
            }

            // Get updated user
            val updatedUser = transaction {
                Users.select { Users.id eq userId }.map {
                    User(
                        id = it[Users.id],
                        email = it[Users.email],
                        password = "", // Don't send the actual password
                        name = it[Users.name],
                        phone = it[Users.phone],
                        resetToken = it[Users.resetToken],
                        resetTokenExpiry = it[Users.resetTokenExpiry]
                    )
                }.single()
            }

            call.respond(updatedUser)
        }
    }
}

// Helper functions
private fun hashPassword(password: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}

private fun generateToken(userId: Int): String {
    // In a real app, use a proper JWT library
    return "user_${userId}_${Random.nextInt(100000, 999999)}"
}
