package no.hiof.geofishing.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import no.hiof.geofishing.data.contracts.AuthService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.entities.Todo

class TodoViewModel(
    authService: AuthService,
    private val todoRepository: Repository<Todo>,
) : ViewModel() {
    val todos = todoRepository.search("profile", authService.id!!).asLiveData()

    suspend fun completeTodo(id: String, state: Boolean) =
        todoRepository.update(id, hashMapOf("completed" to !state))

    suspend fun clearTodos() {
        val res = todos.value

        if (res?.data != null) {
            for (todo in res.data) {
                todoRepository.delete(todo.id.toString())
            }
        }
    }
}