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

    @DELETE("users/{user_id}")
    fun deleteUser(
        @Path("user_id") userId: Int,
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
    @POST("bookings/{booking_id}")
    fun createNewBooking(
        @Path("booking_id") bookingId: Int,
        @Body booking: BookingCreate,
    )

    @GET("bookings/{booking_id}")
    fun getBooking(
        @Path("booking_id") bookingId: Int,
    ): Call<BookingRead>

    @GET("bookings/")
    fun getBookings(): Call<List<BookingRead>>

    @PUT("bookings/{booking_id}")
    fun updateBooking(
        @Path("booking_id") bookingId: Int,
        @Body booking: BookingCreate,
    ): Call<BookingRead>

    @DELETE("bookings/{booking_id}")
    fun deleteBooking(
        @Path("booking_id") bookingId: Int,
    ): Call<Void>

    // Room endpoints
    @GET("rooms/{room_id}")
    fun getRoom(
        @Path("room_id") roomId: Int,
    ): Call<RoomRead>

    @GET("rooms/")
    fun getRooms(): Call<List<RoomRead>>

    @POST("rooms/{room_id}")
    fun createNewRoom(
        @Path("room_id") roomId: Int,
        @Body room: RoomCreate,
    ): Call<RoomRead>

    @PUT("rooms/{room_id}")
    fun updateRoom(
        @Path("room_id") roomId: Int,
        @Body room: RoomCreate,
    ): Call<RoomRead>

    @DELETE("rooms/{room_id}")
    fun deleteRoom(
        @Path("room_id") roomId: Int,
    ): Call<Void>

    // Notification endpoints
    @GET("notifications/{notification_id}")
    fun getNotification(
        @Path("notification_id") notificationId: Int,
    ): Call<NotificationRead>

    @POST("notifications/{notification_id}")
    fun createNewNotification(
        @Path("notification_id") notificationId: Int,
        @Body notification: NotificationCreate,
    ): Call<NotificationCreate>

    @DELETE("notifications/{notification_id}")
    fun deleteNotification(
        @Path("notification_id") notificationId: Int,
    ): Call<Void>

    @POST("email/recover/")
    fun recoverEmail(@Body recoveryRequest: EmailRecoveryRequest): Call<Void>
}