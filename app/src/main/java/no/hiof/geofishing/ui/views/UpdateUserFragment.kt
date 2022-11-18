package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import no.hiof.geofishing.GeofishingApplication
import no.hiof.geofishing.R
import no.hiof.geofishing.data.entities.Profile
import no.hiof.geofishing.databinding.FragmentUpdateUserBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.UpdateUserViewModel

class UpdateUserFragment : DialogFragment() {
    private var _binding: FragmentUpdateUserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UpdateUserViewModel by viewModels {
        ViewModelFactory.create {
            UpdateUserViewModel(
                (activity?.application as GeofishingApplication).authService,
                (activity?.application as GeofishingApplication).fileService,
                (activity?.application as GeofishingApplication).profileRepository
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateUserBinding.inflate(inflater, container, false)

        val photoPicker =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.setPicture(uri)
                } else {
                    Toast.makeText(context, R.string.update_profile_picture_not_selected, Toast.LENGTH_LONG).show()
                }
            }
        binding.buttonUpdateProfilePicture.setOnClickListener {
            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        viewModel.picture.observe(viewLifecycleOwner) { uri ->
            Picasso.get().load(uri)
                .resize(
                    binding.imageUpdateProfilePreview.maxWidth,
                    binding.imageUpdateProfilePreview.maxHeight
                )
                .into(binding.imageUpdateProfilePreview)
        }

        binding.buttonSaveChanges.setOnClickListener {
            val bio = binding.fieldUpdateBio
            val name =  binding.fieldUpdateName
            if(name.text.length > 50 || bio.text.length > 200){
                if(name.text.length > 50){
                    name.error = getString(R.string.update_profile_name_error)
                }
                if(bio.text.length > 200){
                    bio.error = getString(R.string.update_profile_bio_error)
                }
            }
            else {
                viewModel.viewModelScope.launch {
                    viewModel.name = name.text.toString()
                    viewModel.bio = bio.text.toString()
                    viewModel.updateUser()
                    Toast.makeText(it.context, R.string.update_profile_success, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }

        binding.buttonCancelChanges.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}