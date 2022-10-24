package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.geofishing.App
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentChangeEmailBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.ChangeEmailViewModel

class ChangeEmailFragment : DialogFragment() {
    private var _binding: FragmentChangeEmailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChangeEmailViewModel by viewModels {
        ViewModelFactory.create {
            ChangeEmailViewModel(
                (activity?.application as App).authService
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeEmailBinding.inflate(inflater, container, false)

       val emailField = binding.fieldNewEmail
       val passwordField = binding.fieldChangeEmailPassword

        binding.buttonChangeEmailSave.setOnClickListener {
            viewModel.viewModelScope.launch {
                val response = viewModel.changeEmail(
                    emailField.text.toString(),
                    passwordField.text.toString()
                )
                if(response.error == null){
                    Toast.makeText(context, "Email successfully updated", Toast.LENGTH_LONG).show()
                    dismiss()
                }
                else{
                    binding.textChangeEmailErrorMessage.text = response.error.toString()
                }
            }
        }

        binding.buttonChangeEmailCancel.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}