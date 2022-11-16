package no.hiof.geofishing.ui.views

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.squareup.picasso.Picasso
import no.hiof.geofishing.GeofishingApplication
import no.hiof.geofishing.R
import no.hiof.geofishing.data.constants.Tags
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.databinding.FragmentUserPageBinding
import no.hiof.geofishing.ui.adapters.UserPageCatchesAdapter
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.UserPageViewModel


class UserPageFragment : Fragment() {
    private var _binding: FragmentUserPageBinding? = null
    private val binding get() = _binding!!
    private var position: Int = 0

    companion object {
        private const val LAST_POSITION = "lastPosition"
    }

    private val viewModel: UserPageViewModel by viewModels {
        ViewModelFactory.create {
            UserPageViewModel(
                (activity?.application as GeofishingApplication).authService,
                (activity?.application as GeofishingApplication).profileRepository,
                (activity?.application as GeofishingApplication).catchRepository
            )
        }
    }
    private lateinit var user: Profile

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPageBinding.inflate(inflater, container, false)

        viewModel.catches.observe(viewLifecycleOwner) { response ->
            if (response.error == null && response.data != null) {
                val sortedPosts = response.data.sortedByDescending { it.created }
                val recyclerView = binding.userPageRecyclerView

                recyclerView.adapter = UserPageCatchesAdapter(sortedPosts) {
                    position = recyclerView.getChildAdapterPosition(it)
                    val postId = sortedPosts[position].id
                    val uri = Uri.parse("myapp://Geofishing.com/${postId}")
                    if (findNavController().graph.hasDeepLink(uri)) {
                        findNavController().navigate(uri)
                    }
                }

                recyclerView.layoutManager = LinearLayoutManager(context)

                recyclerView.addOnScrollListener(object : OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        val xPos = recyclerView.computeVerticalScrollOffset()
                        position = xPos / (recyclerView.height / response.data.size)
                    }
                })

            } else if (response.error != null) {
                Log.d(Tags.REPOSITORY.toString(), response.error.toString())
            } else {
                Log.d(Tags.REPOSITORY.toString(), "Could not find any data")
            }
        }


        viewModel.profile.observe(viewLifecycleOwner) { response ->
            if (response.error == null && response.data != null) {
                user = response.data
                binding.textName.text = user.name
                binding.textBio.text = user.bio
                Picasso.get().load(user.portrait)
                    .resize(binding.imageProfile.maxWidth, binding.imageProfile.maxHeight)
                    .into(binding.imageProfile)
            } else if (response.error != null) {
                Log.d(Tags.REPOSITORY.toString(), response.error.toString())
            } else {
                Log.d(Tags.REPOSITORY.toString(), "Could not find any data")
            }
        }

        // TODOS
        binding.buttonTodoView.setOnClickListener {
            findNavController().navigate(R.id.action_userPageFragment_to_todoFragment)
        }

        // SETTINGS
        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_userPageFragment_to_settingsFragment)
        }

        // UPDATE USER
        binding.buttonUpdateUser.setOnClickListener {
            UpdateUserFragment().show(childFragmentManager, "UpdateProfile")
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(LAST_POSITION, position)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        var savedPosition = 0
        if (savedInstanceState != null) {
            savedPosition = savedInstanceState.getInt(LAST_POSITION)
        }
        val recyclerView = binding.userPageRecyclerView
        recyclerView.postDelayed({
            recyclerView.smoothScrollToPosition(savedPosition)
        }, 500)
    }
}