package api.data_class

data class UserRead(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val user_type: String,
)