package data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val email: String,
    val password: String,
    val name: String,
    val phone: String? = null,
    val resetToken: String? = null,
    val resetTokenExpiry: Long? = null
)

// Request and response models for authentication
@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val phone: String? = null
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val user: User
)

@Serializable
data class ForgotPasswordRequest(
    val email: String
)

@Serializable
data class ResetPasswordRequest(
    val token: String,
    val newPassword: String
)

@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val phone: String? = null,
    val email: String? = null
)