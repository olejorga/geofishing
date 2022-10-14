package no.hiof.geofishing.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import no.hiof.geofishing.App
import no.hiof.geofishing.viewmodels.FeedViewModel
import no.hiof.geofishing.adapters.FeedAdapter
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.databinding.FragmentFeedBinding
import no.hiof.geofishing.models.FeedPost
import no.hiof.geofishing.utils.ViewModelFactory
import no.hiof.geofishing.viewmodels.CatchViewModel

class FeedFragment : Fragment() {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    private val viewModel : FeedViewModel by viewModels {
        ViewModelFactory.create { FeedViewModel(
                (activity?.application as App).catchRepository
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val feedRecyclerView = binding.feedRecyclerView
        viewModel.catchList.observe(viewLifecycleOwner) { response ->
            Log.d("FEEED", response.toString())
            if(response.error == null && response.data != null) {
                val feedList = response.data

                feedRecyclerView.adapter = FeedAdapter(feedList) {

                    val position = feedRecyclerView.getChildAdapterPosition(it)

                    val clickedPost = feedList[position]

                    val action = FeedFragmentDirections.actionFeedFragmentToFeedPostDetailFragment()
                    action.uid = feedList.indexOf(clickedPost)

                    findNavController().navigate(action)
                }
                feedRecyclerView.layoutManager = GridLayoutManager(context, 1)
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
