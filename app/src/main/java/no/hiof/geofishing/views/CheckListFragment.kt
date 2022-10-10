package no.hiof.geofishing.views

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentCheckListBinding
import no.hiof.geofishing.databinding.FragmentFeedBinding
import no.hiof.geofishing.models.FeedPost
import no.hiof.geofishing.models.Task
import no.hiof.geofishing.viewmodels.CheckListViewModel

class CheckListFragment : Fragment() {
    private var _binding: FragmentCheckListBinding? = null
    private val binding get() = _binding!!
    private val taskList : List<Task> = Task.getTasks()

    private lateinit var viewModel: CheckListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}