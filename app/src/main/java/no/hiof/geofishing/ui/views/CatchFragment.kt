package no.hiof.geofishing.ui.views

import android.os.Bundle
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
    ): View? {
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
            viewModel.viewModelScope.launch {
                viewModel.title = binding.fieldTitle.text.toString()
                viewModel.description = binding.fieldDescription.text.toString()
                viewModel.length = binding.fieldLength.text.toString().toInt()
                viewModel.weight = binding.fieldWeight.text.toString().toInt()
                viewModel.rod = binding.fieldFishingRod.text.toString()
                viewModel.lure = binding.fieldFishingLure.text.toString()
                viewModel.species = binding.spinnerSpecies.selectedItem.toString()

                // TODO LatLng test
                viewModel.longitude = MainActivity.longitude
                viewModel.latitude = MainActivity.latitude

                // TODO: Error handling here. Crashed if no input in fields.
                val (_, error) = viewModel.createCatch()

                if (error != null) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }

                Toast.makeText(context, "Catch added", Toast.LENGTH_LONG).show()
                // TODO: Clear field after adding new catch.
                //  * binding.fieldTitle.text.clear()
                //  * binding.fieldDescription.text.clear()
                //  * binding.fieldLength.text.clear()
                //  * binding.fieldWeight.text.clear()
            }
        }

        val photoPicker =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
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
