package no.hiof.geofishing.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.hiof.geofishing.R
import no.hiof.geofishing.data.entities.Profile

class RankProfileAdapter(
    private val profiles: List<Profile>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<RankProfileAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.rankNameTextView)
        val textPoints: TextView = view.findViewById(R.id.rankPointsTextView)
        val textPositionNumber: TextView = view.findViewById(R.id.rankNumberTextView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rank_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val rank = position + 1

        viewHolder.textName.text = profiles[position].name
        viewHolder.textPoints.text = profiles[position].points.toString()
        viewHolder.textPositionNumber.text = rank.toString()
        viewHolder.itemView.setOnClickListener(onClickListener)
    }

    override fun getItemCount() = profiles.size
}