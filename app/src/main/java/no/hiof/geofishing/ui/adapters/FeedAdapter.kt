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

class FeedAdapter(
    private val feedList: List<Catch>,
    private val clickListener: View.OnClickListener
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.feed_post_item, parent, false)

        return FeedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val currentPost = feedList[position]
        holder.bind(currentPost, clickListener)
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.image_catch)
        private val textTitle: TextView = view.findViewById(R.id.text_title)
        private val textProfile: TextView = view.findViewById(R.id.text_profile)
        private val textDesc: TextView = view.findViewById(R.id.text_description)

        // LatLng test
        private val textLocation: TextView = view.findViewById(R.id.text_location_date_feed)

        fun bind(post: Catch, clickListener: OnClickListener) {
            Picasso.get().load(post.picture).resize(imageView.maxWidth, imageView.maxHeight)
                .into(imageView)
            textTitle.text = post.title
            textProfile.text = post.profile
            textDesc.text = post.description

            itemView.setOnClickListener(clickListener)
        }
    }
}

