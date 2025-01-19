package api.data_class

data class UserCreate(
    val first_name: String,
    val last_name: String,
    val email: String,
    val password: String,
)
