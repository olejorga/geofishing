package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import no.hiof.geofishing.GeofishingApplication
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.databinding.FragmentFeedBinding
import no.hiof.geofishing.ui.adapters.FeedAdapter
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.FeedViewModel

class FeedFragment : Fragment() {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FeedViewModel by viewModels {
        ViewModelFactory.create {
            FeedViewModel(
                (activity?.application as GeofishingApplication).catchRepository,
                (activity?.application as GeofishingApplication).profileRepository
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        lateinit var profiles: List<Profile>
        viewModel.profileList.observe(viewLifecycleOwner) { res ->
            if (res.error == null)
                profiles = res.data!!
        }


        val feedRecyclerView = binding.feedRecyclerView
        viewModel.catchList.observe(viewLifecycleOwner) { res ->
            if (res.error == null && res.data != null) {
                val catches = res.data

                viewModel.setAllProfileNames(catches, profiles)

                feedRecyclerView.adapter = FeedAdapter(catches) {
                    val position = feedRecyclerView.getChildAdapterPosition(it)
                    val clickedPost = catches[position]
                    val action =
                        FeedFragmentDirections.actionFeedFragmentToFeedPostDetailFragment()

                    action.uid = catches.indexOf(clickedPost)
                    findNavController().navigate(action)
                }

                feedRecyclerView.layoutManager = GridLayoutManager(context, 1)
            } else
                Toast.makeText(context, res.error, Toast.LENGTH_LONG).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
