package hu.ait.sievertmobileapplicationfinal

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import hu.ait.sievertmobileapplicationfinal.adapter.DestinationAdapter
import hu.ait.sievertmobileapplicationfinal.data.Base
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

class SearchActivity : AppCompatActivity() {

    var stop_query = ""
    lateinit var destinationAdapter: DestinationAdapter

    companion object {
        const val STOP_QUERY = "STOP_QUERY"
        const val DESTINATION = "DESTINATION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        destinationAdapter = DestinationAdapter(this)
        rvContent.adapter = destinationAdapter

        btnSearch.setOnClickListener(){
            if (etStopName.text.isEmpty()) {
                etStopName.error = "Must enter search here"
            }
            else {
                destinationAdapter.clearAll()
                stop_query = etStopName.text.toString()
                destinationAdapter.setQuery(stop_query)
                getTransitData(etStopName.text.toString())
            }
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
                etStopName.error = t.message //alternatively, "No current trains at this stop"?

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
