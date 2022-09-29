package no.hiof.geofishing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import no.hiof.geofishing.databinding.FragmentFeedPostDetailBinding
import no.hiof.geofishing.models.FeedPost

class FeedPostDetailFragment : Fragment() {
    private val args : FeedPostDetailFragmentArgs by navArgs()
    private var fragmentBinding : FragmentFeedPostDetailBinding? = null

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

        val feedPost = FeedPost.getFeedPosts()[args.uid]

        binding.postTitleTextView.text = feedPost.title
        binding.postImageView.setImageResource(feedPost.posterUrl)
        binding.postDescriptiontextView.text = feedPost.description

    }

}