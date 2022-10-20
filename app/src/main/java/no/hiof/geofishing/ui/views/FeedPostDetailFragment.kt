package no.hiof.geofishing.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import no.hiof.geofishing.App
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentFeedPostDetailBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.FeedViewModel

class FeedPostDetailFragment : Fragment() {
    private val args: no.hiof.geofishing.ui.views.FeedPostDetailFragmentArgs by navArgs()
    private var fragmentBinding: FragmentFeedPostDetailBinding? = null

    private val viewModel: FeedViewModel by viewModels {
        ViewModelFactory.create {
            FeedViewModel(
                (activity?.application as App).catchRepository
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_post_detail, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFeedPostDetailBinding.bind(view)
        fragmentBinding = binding

        viewModel.catchList.observe(viewLifecycleOwner) { response ->
            if (response.error == null && response.data != null) {
                val feedPost = response.data[args.uid]
                binding.textTitle.text = feedPost.title
                val img = binding.imageCatch
                Picasso.get().load(feedPost.picture).resize(img.maxWidth, img.maxHeight).into(img)
                binding.textDescription.text = feedPost.description
                binding.textProfile.text = feedPost.profile
            }
        }
    }

}