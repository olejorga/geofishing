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
import no.hiof.geofishing.App
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
                (activity?.application as App).authService,
                (activity?.application as App).todoRepository
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNewTodoBinding.inflate(inflater, container, false)

        viewModel.reminder.observe(viewLifecycleOwner) {
            Log.d("RAN", "HELLO")
            binding.switchReminder.isChecked = it != null
        }

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
                    Toast.makeText(context, "Todo added.", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
            }
        }

        return binding.root
    }
}