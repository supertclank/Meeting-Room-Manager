package api.data_class

data class LoginRequest(
    val email: String,
    val password: String,
)