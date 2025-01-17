package api.data_class

data class RoomRead(
    val id: Int,
    val name: String,
    val capacity: Int,
    val amenities: List<String>,
    val availability: Boolean,
)