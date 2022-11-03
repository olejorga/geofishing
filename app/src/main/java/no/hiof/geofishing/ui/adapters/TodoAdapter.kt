package no.hiof.geofishing.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import no.hiof.geofishing.R
import no.hiof.geofishing.data.entities.Todo

class TodoAdapter(
    private val todos: List<Todo>,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chip: Chip = view.findViewById(R.id.chip_todo_item)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.todo_item, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.chip.text = todos[position].description
        viewHolder.chip.isChecked = todos[position].completed
        viewHolder.itemView.setOnClickListener(onClickListener)
    }

    override fun getItemCount() = todos.size
}