package no.hiof.geofishing.ui.views

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
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import no.hiof.geofishing.App
import no.hiof.geofishing.MainActivity
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentCatchBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.CatchViewModel


class CatchFragment : Fragment() {
    private var _binding: FragmentCatchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CatchViewModel by viewModels {
        ViewModelFactory.create {
            CatchViewModel(
                (activity?.application as App).authService,
                (activity?.application as App).catchRepository,
                (activity?.application as App).fileService
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatchBinding.inflate(inflater, container, false)
        val spinner: Spinner = binding.spinnerSpecies
        context?.let { context ->
            ArrayAdapter.createFromResource(
                context,
                R.array.fish_array,
                android.R.layout.simple_spinner_item
            ).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = arrayAdapter
            }
        }

        binding.buttonCreateCatch.setOnClickListener {
            try {
                // TODO: Required fields
                viewModel.viewModelScope.launch {
                    viewModel.title = binding.fieldTitle.text.toString()
                    viewModel.description = binding.fieldDescription.text.toString()
                    
                    if (binding.fieldLength.text.toString() == "") viewModel.length = 0
                    else viewModel.length = binding.fieldLength.text.toString().toInt()
                    if (binding.fieldWeight.text.toString() == "") viewModel.weight = 0
                    else viewModel.weight = binding.fieldWeight.text.toString().toInt()
                    viewModel.rod = binding.fieldFishingRod.text.toString()
                    viewModel.lure = binding.fieldFishingLure.text.toString()
                    viewModel.species = binding.spinnerSpecies.selectedItem.toString()
                    viewModel.longitude = MainActivity.longitude
                    viewModel.latitude = MainActivity.latitude

                    val (_, error) = viewModel.createCatch()

                    if (error != null) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }

                    Toast.makeText(context, "Catch added", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_menu_catch_fragment_to_menu_maps_fragment)
                }
            }
            catch (ex : NumberFormatException) {
                Log.e("NumberFormatException", ex.message.toString())
                Toast.makeText(context, "Weight and Length must be numbers", Toast.LENGTH_LONG).show()
            }
        }

        val photoPicker =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Log.d("URI", uri.toString())
                    viewModel.setPicture(uri)
                }
            }

        binding.buttonAddPicture.setOnClickListener {
            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        viewModel.picture.observe(viewLifecycleOwner) { uri ->
            Picasso.get().load(uri).into(binding.imagePreview)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
