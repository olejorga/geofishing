package no.hiof.geofishing.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import no.hiof.geofishing.viewmodels.FeedViewModel
import no.hiof.geofishing.adapters.FeedAdapter
import no.hiof.geofishing.databinding.FragmentFeedBinding
import no.hiof.geofishing.models.FeedPost

class FeedFragment : Fragment() {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val feedList : List<FeedPost> = FeedPost.getFeedPosts()

    private lateinit var viewModel: FeedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val feedRecyclerView = binding.feedRecyclerView

        feedRecyclerView.adapter = FeedAdapter(feedList) {
            val position = feedRecyclerView.getChildAdapterPosition(it)

            val clickedPost = feedList[position]

            val action =
                FeedFragmentDirections.actionFeedFragmentToFeedPostDetailFragment()
            action.uid = clickedPost.uid

            findNavController().navigate(action)
        }
        feedRecyclerView.layoutManager = GridLayoutManager(context, 3)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}