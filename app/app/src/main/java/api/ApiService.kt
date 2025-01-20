package api

import api.data_class.BookingCreate
import api.data_class.BookingRead
import api.data_class.EmailRecoveryRequest
import api.data_class.NotificationCreate
import api.data_class.NotificationRead
import api.data_class.RoomCreate
import api.data_class.RoomRead
import api.data_class.TokenResponse
import api.data_class.UserCreate
import api.data_class.UserRead
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    // User endpoints
    @GET("users/email/{email}")
    fun checkUserExistsByEmail(@Path("email") email: String): Call<Boolean>

    @POST("users/")
    fun createUser(@Body user: UserCreate): Call<UserRead>

    @GET("users/")
    fun getUsers(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
    ): Call<List<UserRead>>

    @GET("users/{id}")
    fun getUser(
        @Path("id") userId: Int?,
    ): Call<UserRead>

    @DELETE("users/{id}")
    fun deleteUser(
        @Path("id") userId: Int,
    ): Call<Void>

    @FormUrlEncoded
    @POST("login/")
    fun login(
        @Field("grant_type") grantType: String = "password",
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("scope") scope: String? = null,
        @Field("client_id") clientId: String? = null,
        @Field("client_secret") clientSecret: String? = null,
    ): Call<TokenResponse>

    // Booking endpoints
    @POST("bookings/{id}")
    fun createNewBooking(
        @Path("id") bookingId: Int,
        @Body booking: BookingCreate,
    )

    @GET("bookings/{id}")
    fun getBooking(
        @Path("id") bookingId: Int,
    ): Call<BookingRead>

    @GET("bookings/")
    fun getBookings(): Call<List<BookingRead>>

    @PUT("bookings/{id}")
    fun updateBooking(
        @Path("id") bookingId: Int,
        @Body booking: BookingCreate,
    ): Call<BookingRead>

    @DELETE("bookings/{id}")
    fun deleteBooking(
        @Path("id") bookingId: Int,
    ): Call<Void>

    // Room endpoints
    @GET("rooms/{room_id}")
    fun getRoom(
        @Path("room_id") roomId: Int,
        @Header("Authorization") token: String
    ): Call<RoomRead>

    @GET("rooms/")
    fun getRooms(
        @Header("Authorization") token: String
    ): Call<List<RoomRead>>

    @POST("rooms/")
    fun createNewRoom(
        @Header("Authorization") token: String,
        @Body newRoom: RoomCreate
    ): Call<RoomRead>

    @PUT("rooms/{room_id}")
    fun updateRoom(
        @Path("room_id") roomId: Int,
        @Header("Authorization") token: String,
        @Body room: RoomCreate
    ): Call<RoomRead>

    @DELETE("rooms/{room_id}")
    fun deleteRoom(
        @Path("room_id") roomId: Int,
        @Header("Authorization") token: String
    ): Call<Void>

    // Notification endpoints
    @GET("notifications/{id}")
    fun getNotification(
        @Path("id") notificationId: Int,
    ): Call<NotificationRead>

    @POST("notifications/{id}")
    fun createNewNotification(
        @Path("id") notificationId: Int,
        @Body notification: NotificationCreate,
    ): Call<NotificationCreate>

    @DELETE("notifications/{id}")
    fun deleteNotification(
        @Path("id") notificationId: Int,
    ): Call<Void>

    @POST("email/recover/")
    fun recoverEmail(@Body recoveryRequest: EmailRecoveryRequest): Call<Void>
}