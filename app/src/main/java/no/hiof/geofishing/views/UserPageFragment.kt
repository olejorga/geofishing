package no.hiof.geofishing.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import no.hiof.geofishing.App
import no.hiof.geofishing.R
import no.hiof.geofishing.viewmodels.UserPageViewModel
import no.hiof.geofishing.databinding.FragmentUserPageBinding
import no.hiof.geofishing.utils.ViewModelFactory
import no.hiof.geofishing.viewmodels.LoginViewModel

class UserPageFragment : Fragment() {
    //private val args : UserPageFragmentArgs by navArgs()
    private var _binding : FragmentUserPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel : UserPageViewModel by viewModels {
        ViewModelFactory.create { UserPageViewModel((activity?.application as App).authService) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserPageBinding.inflate(inflater, container, false)

        binding.buttonLogout.setOnClickListener {
            viewModel.viewModelScope.launch {

                val (_, error) = viewModel.logout()

                if (error != null)
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()

                if (viewModel.authenticated == false)
                    Navigation.findNavController(it).navigate(R.id.action_global_loginFragment)
            }
        }

        val action =
            UserPageFragmentDirections.actionUserPageFragmentToSettingsFragment()
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