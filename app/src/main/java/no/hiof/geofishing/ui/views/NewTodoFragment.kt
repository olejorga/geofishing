package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import kotlinx.coroutines.launch
import no.hiof.geofishing.App
import no.hiof.geofishing.R
import no.hiof.geofishing.data.constants.Tags
import no.hiof.geofishing.data.entities.Todo
import no.hiof.geofishing.databinding.FragmentNewTodoBinding
import no.hiof.geofishing.databinding.FragmentTodoBinding
import no.hiof.geofishing.ui.adapters.TodoAdapter
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.TodoViewModel
import java.time.LocalDateTime
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

class NewTodoFragment : Fragment() {
    private var _binding: FragmentNewTodoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TodoViewModel by viewModels {
        ViewModelFactory.create {
            TodoViewModel(
                (activity?.application as App).authService,
                (activity?.application as App).todoRepository
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentNewTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    var date = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchReminder.setOnClickListener {
            if (binding.switchReminder.isChecked) {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Velg dag for påminnelse")
                        .build()

                val timePicker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(CLOCK_24H)
                        .setTitleText("Velg tid for påminnelse")
                        .build()

                datePicker.addOnPositiveButtonClickListener {
                    val x = datePicker.selection.days
                }

                timePicker.addOnPositiveButtonClickListener {
                    date =
                }

                timePicker.show(childFragmentManager, "")
                datePicker.show(childFragmentManager, "")
            } else {
                Log.d("HERE", "Date: " + date.toString() + ", Hour: " + hour.toString() + ", Minute: " + minute.toString())
            }
        }

        binding.buttonAddTodo.setOnClickListener {

        }
    }
}