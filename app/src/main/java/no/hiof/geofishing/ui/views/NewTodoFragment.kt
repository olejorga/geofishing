package no.hiof.geofishing.ui.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import kotlinx.coroutines.launch
import no.hiof.geofishing.GeofishingApplication
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentNewTodoBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.NewTodoViewModel
import java.util.*

class NewTodoFragment : Fragment() {
    private var _binding: FragmentNewTodoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewTodoViewModel by viewModels {
        ViewModelFactory.create {
            NewTodoViewModel(
                (activity?.application as GeofishingApplication).authService,
                (activity?.application as GeofishingApplication).todoRepository
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNewTodoBinding.inflate(inflater, container, false)

        viewModel.reminder.observe(viewLifecycleOwner) {
            binding.switchReminder.isChecked = it != null
        }

        binding.switchReminder.setOnClickListener {
            if (binding.switchReminder.isChecked) {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText(getString(R.string.new_todo_choose_day))
                        .build()

                val timePicker =
                    MaterialTimePicker.Builder()
                        .setTimeFormat(CLOCK_24H)
                        .setTitleText(getString(R.string.new_todo_choose_time))
                        .build()

                datePicker.addOnPositiveButtonClickListener {
                    viewModel.calendar.timeInMillis = datePicker.selection!!
                }

                timePicker.addOnPositiveButtonClickListener {
                    viewModel.calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                    viewModel.calendar.set(Calendar.MINUTE, timePicker.minute)
                    viewModel.reminder.value = viewModel.calendar.time
                }

                datePicker.addOnDismissListener {
                    viewModel.reminder.value = viewModel.reminder.value
                }

                timePicker.addOnDismissListener {
                    viewModel.reminder.value = viewModel.reminder.value
                }

                timePicker.show(childFragmentManager, "")
                datePicker.show(childFragmentManager, "")
            } else {
                viewModel.reminder.value = null
            }
        }

        binding.buttonAddTodo.setOnClickListener {
            viewModel.viewModelScope.launch {
                viewModel.description = binding.fieldTodoDescription.text.toString()

                val (_, error) = viewModel.createTodo()
                if (error != null) Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                else {
                    Toast.makeText(context, getString(R.string.new_todo_created), Toast.LENGTH_LONG)
                        .show()
                    findNavController().navigateUp()
                }
            }
        }

        return binding.root
    }
}