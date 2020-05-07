package hu.ait.sievertmobileapplicationfinal

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import hu.ait.sievertmobileapplicationfinal.network.BKKAPI
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScrollingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    fun getBKKData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherAPI = retrofit.create(BKKAPI::class.java)

        val call = BKKAPI.getBKKDetails(
            cityName, "metric",
            "19eaa5bb6f3fc28a87efb91bed1ae16f"
        )


        call.enqueue(object : Callback<Base> {
            override fun onResponse(call: Call<Base>, response: Response<Base>) {
                var apiResult = response.body()

                //todo put data here to fill in the xml

            }

            override fun onFailure(call: Call<Base>, t: Throwable) {
                tvJSON.text = t.message

            }
        })
    }
}
