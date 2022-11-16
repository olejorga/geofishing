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
import no.hiof.geofishing.ui.utils.CoordinateFormatter
import no.hiof.geofishing.ui.viewmodels.FeedViewModel

class FeedAdapter(
    private val posts: List<FeedViewModel.Post>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

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
            .load(posts[position].catch.picture)
            .resize(viewHolder.imageView.maxWidth, viewHolder.imageView.maxHeight)
            .into(viewHolder.imageView)

        viewHolder.textTitle.text = posts[position].catch.title
        viewHolder.textProfile.text = posts[position].profile.name
        viewHolder.textDesc.text = posts[position].catch.description
        val location = CoordinateFormatter.prettyLocation(posts[position].catch.latitude, posts[position].catch.longitude)
        viewHolder.textLocation.text = location

        viewHolder.itemView.setOnClickListener(onClickListener)
    }

    override fun getItemCount() = posts.size
}

