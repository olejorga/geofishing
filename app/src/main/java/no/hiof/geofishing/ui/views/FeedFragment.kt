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
import kotlinx.coroutines.flow.collect
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
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            binding.feedRecyclerView.adapter = FeedAdapter(posts) {
                val position = binding.feedRecyclerView.getChildAdapterPosition(it)
                val post = posts[position]
                val action = FeedFragmentDirections.actionFeedFragmentToFeedPostDetailFragment()

                action.uid = posts.indexOf(post)

                findNavController().navigate(action)
            }

            binding.feedRecyclerView.layoutManager = GridLayoutManager(context, 1)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
