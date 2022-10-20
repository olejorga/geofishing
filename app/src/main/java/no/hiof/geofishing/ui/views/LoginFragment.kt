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
import no.hiof.geofishing.databinding.FragmentLoginBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.LoginViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.create { LoginViewModel((activity?.application as App).authService) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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
                    Navigation.findNavController(it)
                        .navigate(R.id.action_loginFragment_to_mapsFragment)
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
}