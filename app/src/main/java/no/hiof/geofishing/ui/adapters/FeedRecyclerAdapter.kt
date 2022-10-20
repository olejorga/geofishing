package no.hiof.geofishing.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.hiof.geofishing.R
import no.hiof.geofishing.ui.models.FeedPost

class FeedRecyclerAdapter(
    private val feedList: List<FeedPost>,
    private val clickListener: View.OnClickListener
) : RecyclerView.Adapter<FeedRecyclerAdapter.FeedViewHolder>() {

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
        private val feedPostImageView: ImageView = view.findViewById(R.id.image_catch)
        private val feedPostTitleTextView: TextView = view.findViewById(R.id.text_title)

        fun bind(post: FeedPost, clickListener: OnClickListener) {
            feedPostImageView.setImageResource(post.posterUrl)
            feedPostTitleTextView.text = post.title

            itemView.setOnClickListener(clickListener)
        }
    }
}

