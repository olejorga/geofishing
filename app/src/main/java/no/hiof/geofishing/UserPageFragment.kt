package no.hiof.geofishing

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import no.hiof.geofishing.databinding.FragmentUserPageBinding
import androidx.navigation.fragment.navArgs

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

        binding.settingsButton.setOnClickListener {
            Toast.makeText(it.context, "Button", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_userPageFragment_to_settingsFragment)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}