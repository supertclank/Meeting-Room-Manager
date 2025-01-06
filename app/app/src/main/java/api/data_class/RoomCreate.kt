package api.data_class

data class RoomCreate(
    val name: String,
    val capacity: Int,
    val amenities: List<String>,
    val availability: Boolean,
)