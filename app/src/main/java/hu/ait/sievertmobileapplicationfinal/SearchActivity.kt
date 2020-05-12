package hu.ait.sievertmobileapplicationfinal

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import hu.ait.sievertmobileapplicationfinal.adapter.DestinationAdapter
import hu.ait.sievertmobileapplicationfinal.data.Base
import hu.ait.sievertmobileapplicationfinal.data.hardcodedData
import hu.ait.sievertmobileapplicationfinal.network.TransitAPI
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.content_search.*
import kotlinx.android.synthetic.main.results_row.*
import kotlinx.android.synthetic.main.results_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class SearchActivity : AppCompatActivity() {

    lateinit var destinationAdapter: DestinationAdapter

    companion object {
        const val STOP_QUERY = "STOP_QUERY"
        const val DESTINATION = "DESTINATION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

            val stopSearchBar = findViewById<AutoCompleteTextView>(R.id.autoTextView)
            val allStops = resources.getStringArray(R.array.all_stops)
            val stopsAdapter = ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, allStops)
            stopSearchBar.setAdapter(stopsAdapter)


        destinationAdapter = DestinationAdapter(this)
        rvContent.adapter = destinationAdapter

        btnSearch.setOnClickListener(){
            if (autoTextView.text.isEmpty()) {
                autoTextView.error = "Must enter search here"
            }
            else {
                getAbbr(autoTextView.text.toString())
            }
        }
    }

    fun getAbbr(stopName: String){
        var abbr = hardcodedData.abbrMap[stopName]
        if (abbr == null) {
            autoTextView.error = "Cannot find station"
        }
        else {
            destinationAdapter.clearAll()
            destinationAdapter.setQuery(abbr)
            getTransitData(abbr)
        }
    }

    fun getTransitData(stopName:String) {

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
                    fillInflater(response)
                }

            }

            override fun onFailure(call: Call<Base>, t: Throwable) {
                autoTextView.error = "No upcoming trains at this stop"

            }
        })
    }

    fun fillInflater(response:Response<Base>) {
        var etdList = response.body()?.root?.station?.get(0)?.etd
        var currentDestination = ""
        if (etdList != null) {
            for(x in etdList) {
                currentDestination = x.destination!!
//                for(y in x.estimate!!) {
                    destinationAdapter.addDestinationItem(currentDestination)
                    //resultRow.ivRouteColor.setImageDrawable()
                    //y.hexcolor?.toInt()?.let { resultRow.ivRouteColor.setBackgroundColor(it) }
//                }
            }
        }
    }
}
