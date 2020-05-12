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
    lateinit var currentDestination: String
    var stopLocation = LatLng(0.0, 0.0)
    var currentStopName = ""
    lateinit var stopQuery: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getStopLocation(stopQuery)
    }

    fun getStopLocation(stopName:String) {
        if(stopName == "antc" || stopName == "dubl" || stopName == "milb"
            || stopName == "oakl" || stopName == "rich" || stopName == "warm"){
            stopLocation = hardcodedData.coordMap[stopName] ?: error("error with coordMap")
            placePin(stopLocation, stopName) //refactor code so that this doesn't have to be an abbr pls
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
                    stopLocation = LatLng(locationResult.root?.stations?.station?.gtfs_latitude!!.toDouble(), // refactor code so that these aren't global x2
                        locationResult.root?.stations?.station?.gtfs_longitude!!.toDouble())
                    currentStopName = locationResult.root?.stations?.station?.name!!.toString()

                    placePin(stopLocation, currentStopName)
                }

            }

            override fun onFailure(call: Call<Base2>, t: Throwable) {
                //tvDestinationStop.text = t.message

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
                //tvCurrentStop.text = t.message

            }
        })
    }

    fun setCircleColor(departureRow: View, color: String) {
        if(color == "YELLOW") {
            departureRow.circle.setBackgroundResource(R.drawable.yellow_circle)
        }
        else if(color == "RED") {
            departureRow.circle.setBackgroundResource(R.drawable.red_circle)
        }
        else if(color == "ORANGE") {
            departureRow.circle.setBackgroundResource(R.drawable.orange_circle)
        }
        else if(color == "GREEN") {
            departureRow.circle.setBackgroundResource(R.drawable.green_circle)
        }
        else if(color == "BLUE") {
            departureRow.circle.setBackgroundResource(R.drawable.blue_circle)
        }
        else if(color == "PURPLE") {
            departureRow.circle.setBackgroundResource(R.drawable.purple_circle)
        }
    }

    fun departureInflater(response: Response<Base>) {

        var etdList = response.body()?.root?.station?.get(0)?.etd
        if (etdList != null) {
            for (x in etdList) {
                if(currentDestination == x.destination!!) {
                    for (y in x.estimate!!) {
                        var departureRow = layoutInflater.inflate(
                            R.layout.departure_row,
                            null, false
                        )
                        tvStopName.text = response.body()?.root?.station?.get(0)?.name
                        var departureName = "to $currentDestination"
                        tvDepartureName.text = departureName

                        var minutesText: String
                        minutesText = if(y.minutes.toString().length > 3) {
                            y.minutes.toString()
                        } else {
                            "in ${y.minutes.toString()} min"
                        }
                        
                        departureRow.minutes.text = minutesText
                        var platformText = "Platform ${y.platform.toString()}"
                        departureRow.platform.text = platformText
                        setCircleColor(departureRow, y.color!!)
                        departureContent.addView(departureRow)

                    }
                }
            }
        }
    }
}
