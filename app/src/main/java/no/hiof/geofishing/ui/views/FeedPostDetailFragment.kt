package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
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
            if (posts != null) {
                val post = posts.filter { it.catch.id == args.catchId }[0]

                Picasso.get()
                    .load(post.catch.picture)
                    .resize(binding.imageCatch.maxWidth, binding.imageCatch.maxHeight)
                    .into(binding.imageCatch)

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
                    binding.buttonDeleteCatch.isEnabled = true

                    binding.buttonEditCatch.setOnClickListener {
                        val action =
                            FeedPostDetailFragmentDirections.actionFeedPostDetailFragmentToUpdateCatchFragment()

                        action.catchId = post.catch.id.toString()
                        findNavController().navigate(action)

                        Log.i("CurrentProfile", "${viewModel.currentProfileId}")
                        Log.i("CatchProf", "${post.catch.profile}")
                    }

                    binding.buttonDeleteCatch.setOnClickListener {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Delete catch")
                            .setMessage("Are you sure you want to delete this catch?")
                            .setPositiveButton("Yes") { _, _ ->
                                viewModel.viewModelScope.launch {
                                    if (viewModel.posts.hasActiveObservers()) {
                                        viewModel.posts.removeObservers(viewLifecycleOwner)
                                    }

                                    viewModel.deleteCatch(post.catch.id.toString())
                                    Toast.makeText(context, "Catch deleted", Toast.LENGTH_LONG)
                                        .show()
                                    findNavController().navigateUp()
                                }
                            }
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show()
                    }

                } else {
                    binding.buttonEditCatch.isVisible = false
                    binding.buttonDeleteCatch.isVisible = false
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}