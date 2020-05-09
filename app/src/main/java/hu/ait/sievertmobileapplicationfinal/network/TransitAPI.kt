package hu.ait.sievertmobileapplicationfinal.network

import hu.ait.sievertmobileapplicationfinal.data.Base
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TransitAPI {
    @GET("api/etd.aspx?cmd=etd")
    fun getTransitDetails(@Query("orig") search: String,
                          @Query("key") key: String, @Query("json") json: String): Call<Base>
}