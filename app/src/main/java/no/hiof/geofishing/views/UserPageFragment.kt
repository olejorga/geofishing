package no.hiof.geofishing.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.first
import no.hiof.geofishing.App
import no.hiof.geofishing.data.constants.Tags
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.viewmodels.UserPageViewModel
import no.hiof.geofishing.databinding.FragmentUserPageBinding
import no.hiof.geofishing.utils.ViewModelFactory

class UserPageFragment : Fragment() {
    private var _binding : FragmentUserPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserPageViewModel by viewModels {
        ViewModelFactory.create { UserPageViewModel(
            (activity?.application as App).authService,
            (activity?.application as App).profileRepository)
        }
    }
    private lateinit var user : Profile

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserPageBinding.inflate(inflater, container, false)
//        binding.userPageViewModel = viewModel
//        binding.lifecycleOwner = this

        viewModel.profile.observe(viewLifecycleOwner){ response ->
            if(response.error == null && response.data != null){
                user = response.data
            }
            else if(response.error != null){
                Log.d(Tags.REPOSITORY.toString(), response.error.toString())
            }
            else {
                Log.d(Tags.REPOSITORY.toString(), "Could not find any data")
            }
        }

        // SETTINGS
        val action = UserPageFragmentDirections.actionUserPageFragmentToSettingsFragment()
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