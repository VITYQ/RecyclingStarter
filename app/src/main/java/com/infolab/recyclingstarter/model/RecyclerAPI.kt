package com.infolab.recyclingstarter.model

import retrofit2.Call
import retrofit2.http.*

var token: String? = null
public interface RecyclerAPI {

    @GET("/v1/boxes?building=1")
    fun getBoxesList(
        @Header("Authorization") token: String): Call<MutableList<Box>>

    @GET("/v1/organizations")
    fun getOrganizationsList(
        @Header("Authorization") token: String): Call<MutableList<Organization>>

    @GET("/v1/users/{id}")
    fun getUserData(
        @Header("Authorization") token: String, @Path("id") id: Int): Call<User>

    @GET("/v1/organizations/buildings")
    fun getAllBuildings(): Call<List<Building>>

    @PUT("/v1/boxes/{box}")
    fun changeFullness(@Header("Authorization") token: String,
                       @Path("box") id: Int,
                       @Body box: BoxResponse): Call<Box>

    @PATCH("/v1/users/")
    fun changePassword(@Header("Authorization") token: String,
                       @Body password: Password): Call<String>

    @PUT("/v1/users/")
    fun changeUserData(@Header("Authorization") token: String,
                       @Body user: User): Call<User>

    @POST("/v1/users/auth/")
    fun authWithEmailAndPass(@Body authCredentials: authCredentials) : Call<authResponse>

    @POST("/v1/users/")
    fun registerUser(@Body register: UserRegister) : Call<String>
}

data class authCredentials(
    val email: String,
    val password: String
)

data class authResponse(
    val token: String,
    val id: Int
)

data class BoxResponse(
    val fullness: Int?,
    val room: String?
)

data class UserRegister(
    val room: String,
    val email: String,
    val phone: String,
    val password: String,
    val building: Int,
    val first_name: String
)