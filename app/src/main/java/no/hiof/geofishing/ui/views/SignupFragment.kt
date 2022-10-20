package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import kotlinx.coroutines.launch
import no.hiof.geofishing.App
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentSignupBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.SignupViewModel

class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignupViewModel by viewModels {
        ViewModelFactory.create { SignupViewModel((activity?.application as App).authService) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignup.setOnClickListener {
            viewModel.viewModelScope.launch {
                val email = binding.fieldEmail.text.toString()
                val password = binding.fieldPassword.text.toString()
                val confirmPassword = binding.fieldConfirmPassword.text.toString()
                val name = binding.fieldName.text.toString()

                if (confirmPassword != password) {
                    Toast.makeText(context, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                    binding.fieldConfirmPassword.error = "Passwords do not match."
                    return@launch
                }

                val (_, error) = viewModel.signup(email, password, name)

                if (error != null)
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()

                if (viewModel.authenticated == true)
                    Navigation.findNavController(it)
                        .navigate(R.id.action_signupFragment_to_mapsFragment)
            }
        }

        binding.buttonLogin.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }
}