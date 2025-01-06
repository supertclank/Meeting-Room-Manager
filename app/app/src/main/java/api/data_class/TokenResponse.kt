package api.data_class

data class TokenResponse(
    val id: Int,
    val access_token: String,
    val token_type: String,
    val email: String,
)