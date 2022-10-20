package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import no.hiof.geofishing.App
import no.hiof.geofishing.ui.adapters.FeedAdapter
import no.hiof.geofishing.data.constants.Tags
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.databinding.FragmentUserPageBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.UserPageViewModel


class UserPageFragment : Fragment() {
    private var _binding: FragmentUserPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserPageViewModel by viewModels {
        ViewModelFactory.create {
            UserPageViewModel(
                (activity?.application as App).authService,
                (activity?.application as App).profileRepository,
                (activity?.application as App).catchRepository
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
                val recyclerView = binding.userPageRecyclerView
                Log.d("UserPageF", response.data.firstOrNull().toString())
                // TODO: Lag en egen adapter, som passer litt bedre.
                // TODO: Lag et eget detailview som passer litt bedre.
                recyclerView.adapter = FeedAdapter(response.data) {
                    val position = recyclerView.getChildAdapterPosition(it)
                }
                recyclerView.layoutManager = GridLayoutManager(context, 1)
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

        // SETTINGS
        val action =
            no.hiof.geofishing.ui.views.UserPageFragmentDirections.actionUserPageFragmentToSettingsFragment()
        binding.settingsButton.setOnClickListener {
            Toast.makeText(it.context, "Button", Toast.LENGTH_SHORT).show()

            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}