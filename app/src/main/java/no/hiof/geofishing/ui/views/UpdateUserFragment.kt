package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.geofishing.App
import no.hiof.geofishing.R
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.databinding.FragmentUpdateUserBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.UpdateUserViewModel

class UpdateUserFragment : Fragment() {
    private var _binding: FragmentUpdateUserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UpdateUserViewModel by viewModels {
        ViewModelFactory.create {
            UpdateUserViewModel(
                (activity?.application as App).authService,
                (activity?.application as App).fileService,
                (activity?.application as App).profileRepository
            )
        }
    }
    private lateinit var user: Profile

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateUserBinding.inflate(inflater, container, false)

        val photoPicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if(uri != null){
                Log.d("URI", uri.toString())
                viewModel.setPicture(uri)
            } else {
                Toast.makeText(context, "Could not select image", Toast.LENGTH_LONG).show()
            }
        }
        binding.buttonUpdateProfilePicture.setOnClickListener {
            Log.d("BUTTON", "Image button pressed")
            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonSaveChanges.setOnClickListener {
            Log.d("BUTTON", "Save changed button pressed!")
            viewModel.viewModelScope.launch {
                viewModel.name = binding.fieldUpdateName.text.toString()
                viewModel.bio = binding.fieldUpdateBio.text.toString()
                viewModel.email = binding.fieldUpdateEmail.text.toString()
                viewModel.password = binding.fieldUpdatePassword.text.toString()
                viewModel.passwordConfirm = binding.fieldUpdatePasswordConfirm.text.toString()
                viewModel.updateUser()
                Toast.makeText(it.context, "Profile updated", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonCancelChanges.setOnClickListener {
            Log.d("BUTTON", "Cancel changes button pressed")
        }



        return binding.root
    }



}