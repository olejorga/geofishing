package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import no.hiof.geofishing.App
import no.hiof.geofishing.R
import no.hiof.geofishing.data.constants.Tags
import no.hiof.geofishing.databinding.FragmentTodoBinding
import no.hiof.geofishing.ui.adapters.TodoAdapter
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.TodoViewModel

class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodoViewModel by viewModels {
        ViewModelFactory.create {
            TodoViewModel(
                (activity?.application as App).authService,
                (activity?.application as App).todoRepository
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)

        viewModel.todos.observe(viewLifecycleOwner) { res ->
            if (res.error == null && res.data != null) {
                binding.recyclerViewTodos.adapter = TodoAdapter(res.data) {
                    viewModel.viewModelScope.launch {
                        val position = binding.recyclerViewTodos.getChildAdapterPosition(it)
                        val todo = res.data[position]
                        val (_, error) = viewModel.completeTodo(todo.id!!, todo.completed)

                        if (error != null) Log.d(Tags.REPOSITORY.toString(), error)
                    }
                }
                binding.recyclerViewTodos.layoutManager = GridLayoutManager(context, 1)
            } else if (res.error != null) {
                Log.d(Tags.REPOSITORY.toString(), res.error)
            } else {
                Log.d(Tags.REPOSITORY.toString(), "Could not find any data")
            }
        }

        binding.floatingActionButtonNewTodo.setOnClickListener {
            // findNavController().navigate(R.id.action_todoFragment_to_newTodoFragment)
        }

        return binding.root
    }
}