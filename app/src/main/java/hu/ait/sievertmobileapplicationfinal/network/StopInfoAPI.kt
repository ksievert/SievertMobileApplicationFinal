package hu.ait.sievertmobileapplicationfinal.network

import hu.ait.sievertmobileapplicationfinal.data.Base
import hu.ait.sievertmobileapplicationfinal.data.Base2
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StopInfoAPI {
    @GET("api/stn.aspx?cmd=stninfo")
    fun getTransitDetails(@Query("orig") search: String,
                          @Query("key") key: String, @Query("json") json: String): Call<Base2>
}