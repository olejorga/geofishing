package no.hiof.geofishing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.hiof.geofishing.R
import no.hiof.geofishing.models.FeedPost

class FeedAdapter(private val feedList: List<FeedPost>, private val clickListener: View.OnClickListener) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feed_post_item, parent, false)

        return FeedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        val currentPost = feedList[position]

        holder.bind(currentPost, clickListener)
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    class FeedViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        private val feedPostImageView : ImageView = view.findViewById(R.id.postImageView)
        private val feedPostTitleTextView : TextView = view.findViewById(R.id.postTitleTextView)

        fun bind(post: FeedPost, clickListener: OnClickListener) {
            feedPostImageView.setImageResource(post.posterUrl)
            feedPostTitleTextView.text = post.title

            itemView.setOnClickListener(clickListener)
        }
    }
}

