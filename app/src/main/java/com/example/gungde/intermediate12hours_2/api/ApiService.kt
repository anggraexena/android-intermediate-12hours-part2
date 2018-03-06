package com.reon.app.reon.api

import com.example.gungde.intermediate12hours_2.model.Base
import com.example.gungde.intermediate12hours_2.model.Jadwal
import com.example.gungde.intermediate12hours_2.model.User
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by gungdeaditya on 11/9/17.
 */
interface ApiService {

    /*===================================================================
     * JADWAL Services.
     ===================================================================*/

    @GET("api/jadwals")
    fun getJadwals(): Call<ArrayList<Jadwal>>

    @GET("api/jadwals/{userId}")
    fun getJadwalByUserId(@Path("userId") userId: String): Call<ArrayList<Jadwal>>

    @FormUrlEncoded
    @POST("api/jadwals")
    fun addJadwal(@Field("userId") userId: String,
                  @Field("kelas") kelas: String,
                  @Field("hari") hari: String,
                  @Field("matkul") matkul: String,
                  @Field("ruang") ruang: String): Call<Base>

    @FormUrlEncoded
    @PUT("api/jadwals/{id}")
    fun updateJadwal(@Path("id") id: String,
                     @Field("kelas") kelas: String,
                     @Field("hari") hari: String,
                     @Field("matkul") matkul: String,
                     @Field("ruang") ruang: String): Call<Base>

    @DELETE("api/jadwals/{id}")
    fun deleteJadwal(@Path("id") id: String): Call<Base>

    /*===================================================================
     * USER Services.
     ===================================================================*/

    @FormUrlEncoded
    @POST("api/users/signin")
    fun login(@Field("email") email: String,
              @Field("password") password: String): Call<User>

    @FormUrlEncoded
    @POST("api/users/signup")
    fun signup(@Field("email") email: String,
               @Field("password") password: String,
               @Field("name") name: String,
               @Field("phoneNumber") phoneNumber: String): Call<Base>

}