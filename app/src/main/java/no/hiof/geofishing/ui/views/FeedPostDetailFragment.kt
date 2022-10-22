package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import no.hiof.geofishing.App
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentFeedPostDetailBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.FeedViewModel

class FeedPostDetailFragment : Fragment() {
    private val args: FeedPostDetailFragmentArgs by navArgs()
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
        return inflater.inflate(R.layout.fragment_feed_post_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFeedPostDetailBinding.bind(view)
        fragmentBinding = binding

        viewModel.catchList.observe(viewLifecycleOwner) { res ->
            if (res.error == null && res.data != null) {
                val catch = res.data[args.uid]
                val img = binding.imageCatch

                Picasso.get()
                    .load(catch.picture)
                    .resize(img.maxWidth, img.maxHeight)
                    .into(img)

                binding.textTitle.text = catch.title
                binding.textDescription.text = catch.description
                binding.textProfile.text = catch.profile
            }
        }
    }

}