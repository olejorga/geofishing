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

        viewModel.picture.observe(viewLifecycleOwner) { uri ->
            Picasso.get().load(uri)
                .resize(
                    binding.imageUpdateProfilePreview.maxWidth,
                    binding.imageUpdateProfilePreview.maxHeight
                )
                .into(binding.imageUpdateProfilePreview)
        }

        binding.buttonSaveChanges.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.name = binding.fieldUpdateName.text.toString()
                viewModel.bio = binding.fieldUpdateBio.text.toString()
                viewModel.updateUser()
                Toast.makeText(it.context, "Profile updated", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        binding.buttonCancelChanges.setOnClickListener {
            Log.d("BUTTON", "Cancel changes button pressed")
            dismiss()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}