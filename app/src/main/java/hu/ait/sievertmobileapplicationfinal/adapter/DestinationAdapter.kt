package hu.ait.sievertmobileapplicationfinal.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ait.sievertmobileapplicationfinal.MapsActivity
import hu.ait.sievertmobileapplicationfinal.R
import hu.ait.sievertmobileapplicationfinal.SearchActivity
import kotlinx.android.synthetic.main.departure_row.view.*
import kotlinx.android.synthetic.main.results_row.*
import kotlinx.android.synthetic.main.results_row.view.*

class DestinationAdapter : RecyclerView.Adapter<DestinationAdapter.ViewHolder> {

    var destinationItems = mutableListOf<String>()
    val context: Context
    lateinit var currentQuery: String

    constructor(context: Context) {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.results_row, parent, false
        )

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return destinationItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // steal from scrolling activity
        val currentDestinationItem = destinationItems[position]

        holder.destination.text = currentDestinationItem

        holder.background.setOnClickListener() {
            val intent = Intent(context, MapsActivity::class.java)
            intent.putExtra(SearchActivity.STOP_QUERY, currentQuery)
            intent.putExtra(SearchActivity.DESTINATION, currentDestinationItem)
            context.startActivity(intent)
        }

    }

    fun clearAll() {
        destinationItems.clear()
        notifyDataSetChanged()
    }

    fun setQuery(query: String) {
        currentQuery = query
    }

    fun addDestinationItem(name: String) {
        destinationItems.add(name)
        notifyItemInserted(destinationItems.lastIndex)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val destination = itemView.destination
        val background = itemView.resultItem
    }

}