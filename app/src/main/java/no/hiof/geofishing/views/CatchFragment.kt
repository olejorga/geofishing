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

class CatchFragment : Fragment() {
    private var _binding : FragmentCatchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CatchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCatchBinding.inflate(inflater, container, false)
        val spinner : Spinner = binding.inputSpeciesSpinner
        context?.let {
            ArrayAdapter.createFromResource(it, R.array.fish_array, android.R.layout.simple_spinner_item)
                .also {
                    it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = it
                }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CatchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}