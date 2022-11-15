package no.hiof.geofishing.ui.views

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import no.hiof.geofishing.GeofishingApplication
import no.hiof.geofishing.MainActivity
import no.hiof.geofishing.R
import no.hiof.geofishing.databinding.FragmentCatchBinding
import no.hiof.geofishing.ui.utils.ViewModelFactory
import no.hiof.geofishing.ui.viewmodels.CatchViewModel
import java.io.File
import java.io.IOException


class CatchFragment : Fragment() {
    private var _binding: FragmentCatchBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageUri: Uri
    private lateinit var currentPhotoPath: String

    private val cameraAndStoragePermissionsArray = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val viewModel: CatchViewModel by viewModels {
        ViewModelFactory.create {
            CatchViewModel(
                (activity?.application as GeofishingApplication).authService,
                (activity?.application as GeofishingApplication).catchRepository,
                (activity?.application as GeofishingApplication).fileService
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatchBinding.inflate(inflater, container, false)
        val spinner: Spinner = binding.spinnerSpecies
        context?.let { context ->
            ArrayAdapter.createFromResource(
                context,
                R.array.fish_array,
                android.R.layout.simple_spinner_item
            ).also { arrayAdapter ->
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = arrayAdapter
            }
        }

        binding.buttonCreateCatch.setOnClickListener {
            try {
                viewModel.viewModelScope.launch {
                    viewModel.title = binding.fieldTitle.text.toString()
                    viewModel.description = binding.fieldDescription.text.toString()

                    if (binding.fieldLength.text.toString() == "") viewModel.length = 0
                    else viewModel.length = binding.fieldLength.text.toString().toInt()
                    if (binding.fieldWeight.text.toString() == "") viewModel.weight = 0
                    else viewModel.weight = binding.fieldWeight.text.toString().toInt()
                    viewModel.rod = binding.fieldFishingRod.text.toString()
                    viewModel.lure = binding.fieldFishingLure.text.toString()
                    viewModel.species = binding.spinnerSpecies.selectedItem.toString()
                    viewModel.longitude = MainActivity.longitude
                    viewModel.latitude = MainActivity.latitude

                    val (_, error) = viewModel.createCatch()

                    if (error != null) {
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }

                    Toast.makeText(context, "Catch added", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_menu_catch_fragment_to_menu_maps_fragment)
                }
            } catch (ex: NumberFormatException) {
                Log.e("NumberFormatException", ex.message.toString())
                Toast.makeText(context, "Weight and Length must be numbers", Toast.LENGTH_LONG)
                    .show()
            }
        }

        val photoPicker =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Log.d("URI", uri.toString())
                    viewModel.setPicture(uri)
                }
            }

        binding.buttonAddPicture.setOnClickListener {
            photoPicker.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        viewModel.picture.observe(viewLifecycleOwner) { uri ->
            Picasso.get().load(uri).into(binding.imagePreview)
        }

        // First checks if user has granted permissions, if not query. then try to create a unique
        // file, then launch camera
        binding.buttonTakePicture.setOnClickListener {
            if (checkCameraStoragePermissions()) {
                if (createUniqueFileName())
                    imageResult.launch(imageUri)
            } else {
                requestCameraAndStoragePermissions()
            }
        }
        return binding.root
    }

    private fun requestCameraAndStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            cameraPermission.launch(Manifest.permission.CAMERA)
        } else {
            imageRequest.launch(cameraAndStoragePermissionsArray)
        }
    }

    // Permission for camera SDK <=30
    private val cameraPermission: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Camera Granted", Toast.LENGTH_LONG).show()
            Log.i("Camera Granted", "$it")
        } else {
            Toast.makeText(context, "Camera not Granted", Toast.LENGTH_LONG).show()
            Log.i("Camera Granted", "$it")
        }
    }

    // Depending on SDK version camera permissions do not require a query
    private fun checkCameraStoragePermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            createUniqueFileName()
        } else {
            val cameraCheck =
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            cameraCheck == PackageManager.PERMISSION_GRANTED
        }
    }

    // Tries to create a file at ../Pictures
    private fun createUniqueFileName(): Boolean {
        try {
            imageUri = FileProvider.getUriForFile(
                requireContext(),
                "no.hiof.geofishing.fileprovider",
                createImageFile()
            )
            Log.i("imageUri", imageUri.toString())
            return true
        } catch (ex: IOException) {
            Log.e("IOException", ex.message.toString())
            Toast.makeText(context, "Could not create image file", Toast.LENGTH_LONG).show()
            return false
        } catch (ex: SecurityException) {
            Log.e("SecurityException", ex.message.toString())
            Toast.makeText(context, "Could not create image file", Toast.LENGTH_LONG).show()
            return false
        }
    }

    // Creates a unique file using currenttimemillis appended to filename
    @Throws(IOException::class, SecurityException::class)
    private fun createImageFile(): File {
        val timeStamp: String = System.currentTimeMillis().toString()
        val imgDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            imgDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    // Permission request SDK >30
    private val imageRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            permissions.entries.forEach { permissionReq ->
                val isGranted = permissionReq.value
                val permissionString = permissionReq.key
                when {
                    isGranted -> {
                        Log.i("isGranted", permissionString)
                        if (permissionString == cameraAndStoragePermissionsArray[1])
                            createUniqueFileName()
                        return@registerForActivityResult
                    }
                }
            }
        }

    // Takes a picture and saves it as a content URI
    private val imageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                viewModel.setPicture(imageUri)
                Log.e("IMGURI", imageUri.toString())
            }
            Log.i("Picture URI", "Result: $success")
        }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}