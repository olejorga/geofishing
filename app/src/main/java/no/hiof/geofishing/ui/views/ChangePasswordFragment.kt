package no.hiof.geofishing.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment : DialogFragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        binding.buttonChangeProfileCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonChangeProfileSave.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}