package no.hiof.geofishing.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import no.hiof.geofishing.models.Catch
import no.hiof.geofishing.repositories.CatchRepository

class LoginViewModel : ViewModel() {
    init {
        viewModelScope.launch {
            val id = CatchRepository.create(Catch("test")).single()

            Log.d("HERE!", "Created catch: $id")

            test(id)

            delay(5000)

            CatchRepository.update(id, hashMapOf("title" to "Hello, Ocean!")).single()

            Log.d("HERE!", "Catch update!")

            CatchRepository.delete(id).single()

            Log.d("HERE!", "Catch deleted!")
        }
    }

    private fun test(id: String) = viewModelScope.launch {
        CatchRepository.find(id).catch { e ->
            Log.d("HERE!", "ERROR fetching catch: $e")
        }.collect { catch ->
            Log.d("HERE!", "Fetched catch: $catch")
        }
    }
}