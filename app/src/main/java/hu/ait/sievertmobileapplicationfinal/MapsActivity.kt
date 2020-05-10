package hu.ait.sievertmobileapplicationfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hu.ait.sievertmobileapplicationfinal.SearchActivity.Companion.DESTINATION
import hu.ait.sievertmobileapplicationfinal.SearchActivity.Companion.STOP_QUERY
import hu.ait.sievertmobileapplicationfinal.data.Base
import hu.ait.sievertmobileapplicationfinal.data.Base2
import hu.ait.sievertmobileapplicationfinal.network.StopInfoAPI
import hu.ait.sievertmobileapplicationfinal.network.TransitAPI
import kotlinx.android.synthetic.main.activity_maps.*
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
    lateinit var stopLocation: LatLng
    lateinit var currentStopName: String
    lateinit var stopQuery: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        stopQuery = intent.getStringExtra(STOP_QUERY)!!
        getDepartures(stopQuery)
        currentDestination = intent.getStringExtra(DESTINATION)!!

        btnBack.setOnClickListener() {
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
        mMap.addMarker(MarkerOptions().position(stopLocation).title(currentStopName))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(stopLocation))
    }

    fun getStopLocation(stopName:String) {
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
                    stopLocation = LatLng(locationResult.root?.stations?.station?.gtfs_latitude!!.toDouble(),
                        locationResult.root?.stations?.station?.gtfs_longitude!!.toDouble())
                    currentStopName = locationResult.root?.stations?.station?.name!!.toString()
                }

            }

            override fun onFailure(call: Call<Base2>, t: Throwable) {
                tvDestinationStop.text = t.message

            }
        })
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
                tvDestinationStop.text = t.message

            }
        })
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
                        departureRow.tvCurrentStop.text = response.body()?.root?.station?.get(0)?.name
                        departureRow.tvDestinationStop.text = currentDestination
                        departureRow.minutes.text = y.minutes.toString()
                        departureRow.platform.text = y.platform.toString()
                        layoutContent.addView(departureRow)
                        //resultRow.ivRouteColor.setImageDrawable()
                        //y.hexcolor?.toInt()?.let { resultRow.ivRouteColor.setBackgroundColor(it) }
                    }
                }
            }
        }
    }
}
