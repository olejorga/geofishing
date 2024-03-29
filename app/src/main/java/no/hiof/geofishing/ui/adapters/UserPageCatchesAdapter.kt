package no.hiof.geofishing.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import no.hiof.geofishing.R
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.ui.utils.CoordinateFormatter

class UserPageCatchesAdapter(
    private val catch: List<Catch>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<UserPageCatchesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_catch)
        val textTitle: TextView = view.findViewById(R.id.text_title)
        val textProfile: TextView = view.findViewById(R.id.text_profile)
        val textDesc: TextView = view.findViewById(R.id.text_description)
        val textLocation: TextView = view.findViewById(R.id.text_location_date_feed)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.feed_post_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Picasso.get()
            .load(catch[position].picture)
            .resize(viewHolder.imageView.maxWidth, viewHolder.imageView.maxHeight)
            .into(viewHolder.imageView)

        viewHolder.textTitle.text = catch[position].title
        viewHolder.textProfile.text = catch[position].profileName
        viewHolder.textDesc.text = catch[position].description
        val location = CoordinateFormatter.prettyLocation(catch[position].latitude, catch[position].longitude)
        viewHolder.textLocation.text = location
        viewHolder.itemView.setOnClickListener(onClickListener)
    }

    override fun getItemCount() = catch.size
}

