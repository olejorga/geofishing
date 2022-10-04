package no.hiof.geofishing.views

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentLoginBinding
import no.hiof.geofishing.databinding.FragmentSignupBinding
import no.hiof.geofishing.viewmodels.SignupViewModel

class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SignupFragment()
    }

    private lateinit var viewModel: SignupViewModel

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
            Navigation.findNavController(it).navigate(R.id.action_signupFragment_to_loginFragment)
        }

        binding.buttonLogin.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        // TODO: Use the ViewModel
    }

}