package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.launch
import no.hiof.geofishing.GeofishingApplication
import no.hiof.geofishing.data.constants.Tags
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.databinding.FragmentRankBinding
import no.hiof.geofishing.ui.adapters.RankAdapter
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.RankViewModel

class RankFragment : Fragment() {
    private var _binding: FragmentRankBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RankViewModel by viewModels {
        ViewModelFactory.create {
            RankViewModel(
                (activity?.application as GeofishingApplication).catchRepository,
                (activity?.application as GeofishingApplication).profileRepository
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRankBinding.inflate(inflater, container, false)

        viewModel.ranks.observe(viewLifecycleOwner) { ranks ->
            val sortedRanks = ranks.sortedByDescending { it.points }

            binding.rankRecyclerView.adapter = RankAdapter(sortedRanks)
            binding.rankRecyclerView.layoutManager = GridLayoutManager(context, 1)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}