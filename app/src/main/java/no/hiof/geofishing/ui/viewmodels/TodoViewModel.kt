package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.entities.Todo

class TodoViewModel(
    private val authService: AuthService,
    private val todoRepository: Repository<Todo>,
) : ViewModel() {
    val todos = todoRepository.search("profile", authService.id!!).asLiveData()
    val profileId = authService.id

    suspend fun completeTodo(id: String, state: Boolean) =
        todoRepository.update(id, hashMapOf("completed" to !state))

    suspend fun addTodo(todo: Todo) = todoRepository.create(todo)
}