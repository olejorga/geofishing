package no.hiof.geofishing.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentCatchBinding
import no.hiof.geofishing.viewmodels.CatchViewModel
import androidx.fragment.app.viewModels
import no.hiof.geofishing.models.Catch
import kotlin.math.log


class CatchFragment : Fragment() {
    private var _binding : FragmentCatchBinding? = null
    private val binding get() = _binding!!

    private val viewModel : CatchViewModel by viewModels()

    private lateinit var catch: Catch

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCatchBinding.inflate(inflater, container, false)
        val spinner : Spinner = binding.inputSpeciesSpinner
        context?.let { context ->
            ArrayAdapter.createFromResource(context, R.array.fish_array, android.R.layout.simple_spinner_item)
                .also { arrayAdapter ->
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = arrayAdapter
                }
        }
     return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}