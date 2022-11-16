package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import kotlinx.coroutines.launch
import no.hiof.geofishing.GeofishingApplication
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentSettingsBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.SettingsViewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels {
        ViewModelFactory.create { SettingsViewModel((activity?.application as GeofishingApplication).authService) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogOut.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.logout()
                Navigation.findNavController(it).navigate(R.id.action_global_loginFragment)
            }
        }

        binding.buttonChangePassword.setOnClickListener {
            ChangePasswordFragment().show(childFragmentManager, "ChangePassword")
        }

        binding.buttonChangeEmail.setOnClickListener {
            ChangeEmailFragment().show(childFragmentManager, "ChangeEmail")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}