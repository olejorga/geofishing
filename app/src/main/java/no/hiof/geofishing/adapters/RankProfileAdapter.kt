package no.hiof.geofishing.adapters

import no.hiof.geofishing.data.entities.Profile
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.hiof.geofishing.R

class RankProfileAdapter (private val rankList: List<Profile>, private val clickListener: OnClickListener) : RecyclerView.Adapter<RankProfileAdapter.RankViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rank_item, parent, false)
        return RankViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        val currentPost = rankList[position]
        holder.bind(currentPost, clickListener)
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

    class RankViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        private val rankNameTextView : TextView = view.findViewById(R.id.rankNameTextView)
        private val rankPointsTextView : TextView = view.findViewById(R.id.rankPointsTextView)
        private val rankNumberTextView : TextView = view.findViewById(R.id.rankNumberTextView)

        fun bind (profile: Profile, clickListener: OnClickListener) {
            rankNameTextView.text = profile.name
            rankPointsTextView.text = profile.bio
            rankNumberTextView.text = position().toString()
            itemView.setOnClickListener(clickListener)
        }

        private fun position() : Int{
            return adapterPosition + 1
        }
    }
}