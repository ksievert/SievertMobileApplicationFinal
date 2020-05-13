package hu.ait.sievertmobileapplicationfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.ait.sievertmobileapplicationfinal.SearchActivity.Companion.DESTINATION
import hu.ait.sievertmobileapplicationfinal.SearchActivity.Companion.STOP_QUERY
import hu.ait.sievertmobileapplicationfinal.data.Base
import hu.ait.sievertmobileapplicationfinal.data.Base2
import hu.ait.sievertmobileapplicationfinal.data.hardcodedData
import hu.ait.sievertmobileapplicationfinal.network.StopInfoAPI
import hu.ait.sievertmobileapplicationfinal.network.TransitAPI
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_maps.view.*
import kotlinx.android.synthetic.main.content_search.*
import kotlinx.android.synthetic.main.departure_row.*
import kotlinx.android.synthetic.main.departure_row.view.*
import kotlinx.android.synthetic.main.results_row.*
import kotlinx.android.synthetic.main.results_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var currentDestination: String
    lateinit var stopQuery: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        stopQuery = intent.getStringExtra(STOP_QUERY)!!
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getDepartures(stopQuery)
        currentDestination = intent.getStringExtra(DESTINATION)!!

        btnBack.setOnClickListener() {
            val intent = Intent(this, SearchActivity::class.java)
            this.startActivity(intent)
        }
        tvBack.setOnClickListener(){
            val intent = Intent(this, SearchActivity::class.java)
            this.startActivity(intent)
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getStopLocation(stopQuery)
    }

    private fun checkIfEndStop(stopName: String): Boolean {
        return if(stopName == "antc" || stopName == "dubl" || stopName == "milb"
            || stopName == "oakl" || stopName == "rich" || stopName == "warm"){
            var stopLocation = hardcodedData.coordMap[stopName] ?: error("error with coordMap")
            placePin(stopLocation, stopName)
            true
        } else {
            false
        }
    }

    fun getStopLocation(stopName:String) {

        if(checkIfEndStop(stopName)) {
            return
        }
            val retrofit = Retrofit.Builder()
            .baseUrl("https://api.bart.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val StopInfoAPI = retrofit.create(StopInfoAPI::class.java)

        val call = StopInfoAPI.getTransitDetails(
            stopName, "QSR8-5PY9-9QDT-DWEI",
            "y"
        )


        call.enqueue(object : Callback<Base2> {
            override fun onResponse(call: Call<Base2>, response: Response<Base2>) {
                var locationResult = response.body()
                if(locationResult != null) {
                    var stopLocation = LatLng(locationResult.root?.stations?.station?.gtfs_latitude!!.toDouble(), // refactor code so that these aren't global x2
                        locationResult.root?.stations?.station?.gtfs_longitude!!.toDouble())
                    var currentStopName = locationResult.root?.stations?.station?.name!!.toString()

                    placePin(stopLocation, currentStopName)
                }

            }

            override fun onFailure(call: Call<Base2>, t: Throwable) {

            }
        })
    }

    fun placePin(location: LatLng, name:String) {
        mMap.addMarker(MarkerOptions().position(location).title(name))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17f))
    }

    fun getDepartures(stopName:String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.bart.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val transitAPI = retrofit.create(TransitAPI::class.java)

        val call = transitAPI.getTransitDetails(
            stopName, "QSR8-5PY9-9QDT-DWEI",
            "y"
        )


        call.enqueue(object : Callback<Base> {
            override fun onResponse(call: Call<Base>, response: Response<Base>) {
                var etdResult = response.body()
                if(etdResult != null) {
                    departureInflater(response)
                }

            }

            override fun onFailure(call: Call<Base>, t: Throwable) {

            }
        })
    }

    private fun setCircleColor(departureRow: View, color: String) {
        when (color) {
            "YELLOW" -> {
                departureRow.circle.setBackgroundResource(R.drawable.yellow_circle)
            }
            "RED" -> {
                departureRow.circle.setBackgroundResource(R.drawable.red_circle)
            }
            "ORANGE" -> {
                departureRow.circle.setBackgroundResource(R.drawable.orange_circle)
            }
            "GREEN" -> {
                departureRow.circle.setBackgroundResource(R.drawable.green_circle)
            }
            "BLUE" -> {
                departureRow.circle.setBackgroundResource(R.drawable.blue_circle)
            }
            "PURPLE" -> {
                departureRow.circle.setBackgroundResource(R.drawable.purple_circle)
            }
        }
    }

    fun departureInflater(response: Response<Base>) {

        val etdList = response.body()?.root?.station?.get(0)?.etd
        if (etdList != null) {
            for (x in etdList) {
                if(currentDestination == x.destination!!) {
                    for (y in x.estimate!!) {
                        var departureRow = layoutInflater.inflate(
                            R.layout.departure_row,
                            null, false
                        )

                        tvStopName.text = response.body()?.root?.station?.get(0)?.name
                        val departureName = "to $currentDestination"
                        tvDepartureName.text = departureName

                        var minutesText: String = if(y.minutes.toString().length > 3) {
                            y.minutes.toString()
                        } else {
                            "in ${y.minutes.toString()} min"
                        }
                        
                        departureRow.minutes.text = minutesText
                        val platformText = "Platform ${y.platform.toString()}"
                        departureRow.platform.text = platformText
                        setCircleColor(departureRow, y.color!!)
                        departureContent.addView(departureRow)

                    }
                }
            }
        }
    }
}
