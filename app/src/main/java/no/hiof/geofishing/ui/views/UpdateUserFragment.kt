package no.hiof.geofishing.ui.views

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import no.hiof.geofishing.R
import no.hiof.geofishing.ui.viewmodels.UpdateUserViewModel

class UpdateUserFragment : Fragment() {

    companion object {
        fun newInstance() = UpdateUserFragment()
    }

    private lateinit var viewModel: UpdateUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UpdateUserViewModel::class.java)
        // TODO: Use the ViewModel
    }

}