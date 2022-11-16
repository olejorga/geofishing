package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.geofishing.GeofishingApplication
import no.hiof.geofishing.databinding.FragmentChangePasswordBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.ChangePasswordViewModel

class ChangePasswordFragment : DialogFragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChangePasswordViewModel by viewModels {
        ViewModelFactory.create {
            ChangePasswordViewModel(
                (activity?.application as GeofishingApplication).authService
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        val passwordField = binding.fieldUpdatePassword
        val confirmPasswordField = binding.fieldUpdatePasswordConfirm
        val oldPasswordField = binding.fieldOldPassword

        binding.buttonChangeProfileSave.setOnClickListener {
            viewModel.viewModelScope.launch {
                val response = viewModel.changePassword(
                    passwordField.text.toString(),
                    confirmPasswordField.text.toString(),
                    oldPasswordField.text.toString()
                )
                if (response.error == null) {
                    Toast.makeText(context, "Password successfully changed!", Toast.LENGTH_LONG)
                        .show()
                    dismiss()
                } else if (passwordField.text.toString() != confirmPasswordField.text.toString()) {
                    confirmPasswordField.error = response.error.toString()
                } else {
                    oldPasswordField.error = response.error.toString()
                }
            }
        }

        binding.buttonChangeProfileCancel.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}