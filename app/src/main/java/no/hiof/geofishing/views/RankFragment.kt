package no.hiof.geofishing.views

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import no.hiof.geofishing.R
import no.hiof.geofishing.adapters.RankAdapter
import no.hiof.geofishing.databinding.FragmentRankBinding
import no.hiof.geofishing.viewmodels.RankViewModel

class RankFragment : Fragment() {
    private var _binding: FragmentRankBinding? = null
    private val binding get() = _binding!!

    private val viewModel : RankViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rankRecyclerView = binding.rankRecyclerView

        val rankedList = viewModel.userList

        rankRecyclerView.adapter = RankAdapter(rankedList) {
            val position = rankRecyclerView.getChildAdapterPosition(it)

        }

        rankRecyclerView.layoutManager = GridLayoutManager(context, 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}