package no.hiof.geofishing.ui.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import no.hiof.geofishing.GeofishingApplication
import no.hiof.geofishing.R
import no.hiof.geofishing.data.entities.Catch
import no.hiof.geofishing.databinding.FragmentUpdateCatchBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.UpdateCatchViewModel

class UpdateCatchFragment : Fragment() {
    private var _binding: FragmentUpdateCatchBinding? = null
    private val binding get() = _binding!!

    private val args: UpdateCatchFragmentArgs by navArgs()
    private lateinit var catch: Catch
    private lateinit var fishSpeciesArray: Array<String>

    private val viewModel: UpdateCatchViewModel by viewModels {
        ViewModelFactory.create {
            UpdateCatchViewModel(
                (activity?.application as GeofishingApplication).catchRepository,
                (activity?.application as GeofishingApplication).fileService
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fishSpeciesArray = context.resources.getStringArray(R.array.fish_array)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.catchId = args.catchId
        viewModel.setCatch()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateCatchBinding.inflate(inflater, container, false)

        val spinnerUpdateSpecies: Spinner = binding.spinnerSpeciesUpdate
        context?.let { context ->
            ArrayAdapter.createFromResource(
                context,
                R.array.fish_array,
                android.R.layout.simple_spinner_item
            ).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerUpdateSpecies.adapter = arrayAdapter
            }
        }

        viewModel.catch.observe(viewLifecycleOwner) { response ->
            if (response.error == null && response.data != null) {
                catch = response.data
                // binding to xml show current catch values as hint text in view
                binding.catchBinding = catch

                // Sets spinner value to original catch species
                val originalSpeciesSelected = fishSpeciesArray.indexOf(catch.species)
                spinnerUpdateSpecies.setSelection(originalSpeciesSelected)

                // gets image
                Picasso.get()
                    .load(catch.picture)
                    .into(binding.imagePreviewUpdate)
            }
        }

        val photoPicker =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Log.d("URI", uri.toString())
                    viewModel.setPicture(uri)
                } else {
                    Toast.makeText(context, "Could not select image", Toast.LENGTH_LONG).show()
                }
            }

        binding.buttonUpdatePicture.setOnClickListener {
            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        viewModel.picture.observe(viewLifecycleOwner) { uri ->
            Picasso.get().load(uri)
                .into(binding.imagePreviewUpdate)
        }

        binding.buttonUpdateCatch.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.hashMap["title"] =
                    binding.fieldTitleUpdate.text.toString().ifBlank { catch.title.toString() }
                viewModel.hashMap["description"] = binding.fieldDescriptionUpdate.text.toString()
                    .ifBlank { catch.description.toString() }
                viewModel.hashMap["species"] = binding.spinnerSpeciesUpdate.selectedItem.toString()
                    .ifBlank { catch.species.toString() }

                viewModel.hashMap["weight"] =
                    if (binding.fieldWeightUpdate.text.toString().isBlank()
                    ) catch.weight.toString().toInt() else binding.fieldWeightUpdate.text.toString()
                        .toInt()

                viewModel.hashMap["length"] =
                    if (binding.fieldLengthUpdate.text.toString().isBlank()
                    ) catch.length.toString().toInt() else binding.fieldLengthUpdate.text.toString()
                        .toInt()

                viewModel.hashMap["rod"] =
                    binding.fieldFishingRodUpdate.text.toString().ifBlank { catch.rod.toString() }
                viewModel.hashMap["lure"] =
                    binding.fieldFishingLureUpdate.text.toString().ifBlank { catch.lure.toString() }

                viewModel.updateCatch()
                findNavController().navigateUp()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}