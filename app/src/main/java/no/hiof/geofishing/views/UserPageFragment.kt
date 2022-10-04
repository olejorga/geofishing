package no.hiof.geofishing.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import no.hiof.geofishing.viewmodels.UserPageViewModel
import no.hiof.geofishing.databinding.FragmentUserPageBinding

class UserPageFragment : Fragment() {
    //private val args : UserPageFragmentArgs by navArgs()
    private var _binding : FragmentUserPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UserPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserPageBinding.inflate(inflater, container, false)

        val action =
            no.hiof.geofishing.views.UserPageFragmentDirections.actionUserPageFragmentToSettingsFragment()
        binding.settingsButton.setOnClickListener {
            Toast.makeText(it.context, "Button", Toast.LENGTH_SHORT).show()

            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}