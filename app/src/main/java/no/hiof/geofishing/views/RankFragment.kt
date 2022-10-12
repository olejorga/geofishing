package no.hiof.geofishing.views

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import no.hiof.geofishing.App
import no.hiof.geofishing.R
import no.hiof.geofishing.adapters.RankAdapter
import no.hiof.geofishing.adapters.RankProfileAdapter
import no.hiof.geofishing.data.constants.Tags
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.databinding.FragmentRankBinding
import no.hiof.geofishing.utils.ViewModelFactory
import no.hiof.geofishing.viewmodels.RankViewModel

class RankFragment : Fragment() {
    private var _binding: FragmentRankBinding? = null
    private val binding get() = _binding!!

    private val viewModel : RankViewModel by viewModels {
        ViewModelFactory.create {RankViewModel(
            (activity?.application as App).profileRepository)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankBinding.inflate(inflater, container, false)


        val rankRecyclerView = binding.rankRecyclerView

        //var rankedList = viewModel.userList
        var rankedList : MutableList<Profile>

        viewModel.profileList.observe(viewLifecycleOwner) { response ->
            if(response.error == null && response.data != null){
                rankedList = ArrayList(response.data)
                rankRecyclerView.adapter = RankProfileAdapter(rankedList) {
                    val position = rankRecyclerView.getChildAdapterPosition(it)
                    }
                }
            else if(response.error != null){
                Log.d(Tags.REPOSITORY.toString(), response.error.toString())
            }
            else {
                Log.d(Tags.REPOSITORY.toString(), "Could not find any data")
            }
        }

        rankRecyclerView.layoutManager = GridLayoutManager(context, 1)

        return binding.root

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}