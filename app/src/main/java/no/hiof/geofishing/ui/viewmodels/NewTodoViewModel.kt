package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.entities.Todo
import java.util.*

class NewTodoViewModel(
    private val authService: AuthService,
    private val todoRepository: Repository<Todo>,
) : ViewModel() {
    var description: String? = null
    var reminder = MutableLiveData<Date?>(null)
    val profile = authService.id
    val calendar: Calendar = Calendar.getInstance()

    suspend fun createTodo() = todoRepository.create(
        Todo(
            null,
            false,
            description,
            reminder.value,
            profile
        )
    )
}