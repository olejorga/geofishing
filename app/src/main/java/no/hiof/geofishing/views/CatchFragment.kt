package no.hiof.geofishing.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentCatchBinding
import no.hiof.geofishing.viewmodels.CatchViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.hiof.geofishing.App
import no.hiof.geofishing.data.constants.Species
import no.hiof.geofishing.models.Catch
import no.hiof.geofishing.utils.ViewModelFactory
import kotlin.math.log


class CatchFragment : Fragment() {
    private var _binding : FragmentCatchBinding? = null
    private val binding get() = _binding!!

    private val viewModel : CatchViewModel by viewModels {
        ViewModelFactory.create { CatchViewModel(
            (activity?.application as App).catchRepository)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCatchBinding.inflate(inflater, container, false)
        val spinner : Spinner = binding.spinnerSpecies
        context?.let { context ->
            ArrayAdapter.createFromResource(context, R.array.fish_array , android.R.layout.simple_spinner_item)
                .also { arrayAdapter ->
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

                // TODO: Error handling here. Crashed if no input in fields.
                val (_, error) = viewModel.createCatch()

                if(error != null) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Catch added", Toast.LENGTH_LONG).show()
                    // TODO: Clear field after adding new catch.
//                    binding.fieldTitle.text.clear()
//                    binding.fieldDescription.text.clear()
//                    binding.fieldLength.text.clear()
//                    binding.fieldWeight.text.clear()
                }
            }
        }
     return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
