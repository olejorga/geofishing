package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import no.hiof.geofishing.GeofishingApplication
import no.hiof.geofishing.databinding.FragmentFeedPostDetailBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.FeedViewModel


class FeedPostDetailFragment : Fragment() {
    private val args: FeedPostDetailFragmentArgs by navArgs()
    private var _binding: FragmentFeedPostDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FeedViewModel by viewModels {
        ViewModelFactory.create {
            FeedViewModel(
                (activity?.application as GeofishingApplication).catchRepository,
                (activity?.application as GeofishingApplication).profileRepository,
                (activity?.application as GeofishingApplication).authService
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedPostDetailBinding.inflate(inflater, container, false)

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            val post = posts[args.uid]
            val img = binding.imageCatch

            Picasso.get()
                .load(post.catch.picture)
                .resize(img.maxWidth, img.maxHeight)
                .into(img)

            val weight = post.catch.weight.toString() + "g"
            val length = post.catch.length.toString() + "cm"

            binding.textTitle.text = post.catch.title
            binding.textDescription.text = post.catch.description
            binding.textSpecies.text = post.catch.species
            binding.textWeight.text = weight
            binding.textLength.text = length
            binding.textRod.text = post.catch.rod
            binding.textLure.text = post.catch.lure

            binding.textProfile.text = post.profile.name
            
            if (viewModel.currentProfileId == post.catch.profile) {
                binding.buttonEditCatch.isEnabled = true
                binding.buttonEditCatch.setOnClickListener {
//                        UpdateCatchFragment().show(childFragmentManager, "UpdateCatch")
//                        UpdateCatchFragment().show(childFragmentManager, "UpdateCatch")
                    Log.i("CurrentProfile", "${viewModel.currentProfileId}")
                    Log.i("CatchProf", "${post.catch.profile}")
                }
            } else {
                binding.buttonEditCatch.isVisible = false
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}