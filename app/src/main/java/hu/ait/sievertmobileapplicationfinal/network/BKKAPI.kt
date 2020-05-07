package hu.ait.sievertmobileapplicationfinal.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BKKAPI {
    @GET("data/2.5/weather")
    fun getWeatherDetails(@Query("q") city: String,
                          @Query("units") units: String, @Query("appid") appid: String): Call<Base>
}