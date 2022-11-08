package no.hiof.geofishing.ui.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import no.hiof.geofishing.data.contracts.FileService
import no.hiof.geofishing.data.contracts.Repository
import no.hiof.geofishing.data.contracts.Response
import no.hiof.geofishing.data.entities.Catch
import java.util.*

class UpdateCatchViewModel(private val catchRepository: Repository<Catch>, private val fileService: FileService) : ViewModel() {
    lateinit var catch : LiveData<Response<Catch>>
    lateinit var catchId : String

    var hashMap = HashMap<String, Any>()

    private val _picture = MutableLiveData<Uri>()
    val picture: LiveData<Uri> get() = _picture
    fun setPicture(uri: Uri) {
        _picture.value = uri
    }

    init {
        hashMap.putAll(mapOf("title" to String, "description" to String, "species" to String,
            "weight" to Int, "length" to Int, "rod" to String, "lure" to String))
    }

    fun setCatch() {
        catch = catchRepository.find(catchId).asLiveData()
    }

    suspend fun updateCatch() {
        hashMap.forEach { (key, value) ->
            catchRepository.update(catchId, mapOf(key to value))
        }

        if (_picture.value != null) {
            val filename = UUID.randomUUID().toString()
            Log.d("HERE", filename + " " + _picture.value.toString())
            val (data, error) = fileService.upload(filename, _picture.value!!)

            if (error != null) {
                Log.d("PICTURE", "Error saving picture to fileSystem")
                return
            }
            mapOf("picture" to data.toString()).let { catchRepository.update(catchId, it) }
        }
    }
}