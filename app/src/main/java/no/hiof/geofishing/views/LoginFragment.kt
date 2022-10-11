package no.hiof.geofishing.views

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import kotlinx.coroutines.launch
import no.hiof.geofishing.App
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentLoginBinding
import no.hiof.geofishing.utils.ViewModelFactory
import no.hiof.geofishing.viewmodels.LoginViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var locationPermissionGranted: Boolean = false

    private val viewModel : LoginViewModel by viewModels {
        ViewModelFactory.create { LoginViewModel((activity?.application as App).authService) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        checkLocationPermissions()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.authenticated == true)
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mapsFragment)

        binding.buttonLogin.setOnClickListener {
            viewModel.viewModelScope.launch {
                val email = binding.fieldEmail.text.toString()
                val password = binding.fieldPassword.text.toString()

                val (_, error) = viewModel.login(email, password)

                if (error != null)
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()

                if (viewModel.authenticated == true)
                    Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_mapsFragment)
            }
        }

        binding.buttonSignup.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private val locationPermissionsRequired = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    // TODO legg te else der du sett locationPermissionGranted = true
    private fun checkLocationPermissions() {
        locationPermissionsRequired.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    requireContext(), permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                requestLocationPermissions.launch(locationPermissionsRequired)
                return
            }
        }
    }

    private val requestLocationPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val isGranted = it.value
                val permission = it.key
                when {
                    isGranted -> locationPermissionGranted = true
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), permission
                    ) -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle(R.string.perm_request_rationale_title)
                            .setMessage(R.string.perm_request_rationale)
                            .setPositiveButton(R.string.request_perm_again) { _, _ ->
                                checkLocationPermissions()
                            }.setNegativeButton(R.string.dismiss, null).create().show()
                    }
                }
            }
        }
}