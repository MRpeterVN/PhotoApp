package com.example.photoapp_main

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.FLASH_MODE_OFF
import androidx.camera.core.ImageCapture.FLASH_MODE_ON
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.photoapp_main.data.Image
import com.example.photoapp_main.databinding.ActivityMainBinding
import com.example.photoapp_main.extension.setOnSafeClickListener
import com.example.photoapp_main.viewmodel.MainVM
import com.example.photoapp_main.viewmodel.MainVMFactory
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainVM
    private lateinit var binding: ActivityMainBinding
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var imageCapture: ImageCapture? = null
    private var camera: Camera? = null
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private lateinit var outputDirectory: File
    private val capturedImages = mutableListOf<Uri>()
    private var isFlashEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = MainVMFactory(this)
        viewModel = ViewModelProvider(this, factory)[MainVM::class.java]
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraExecutor = Executors.newSingleThreadExecutor()
        outputDirectory = getOutputDirectory()
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    startCamera()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        // Initialize thumbnail click listener to show the image gallery
        binding.imageThumbnail.setOnClickListener {
            showImageGallery()
        }
        binding.flipCamera.setOnClickListener {
            switchCamera()
        }
        binding.toggleFlash.setOnClickListener {
            toggleFlash()
        }
        // capture
        eventCapture()
        // viewmodel
        bindViewModel()
    }

    private fun startCamera() {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        imageCapture = ImageCapture.Builder()
            .setFlashMode(if (isFlashEnabled) FLASH_MODE_ON else FLASH_MODE_OFF)
            .build()

        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)

        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to bind camera preview", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindViewModel() {
        viewModel.iamge.observe(this) { images ->
            if (images.isNotEmpty()) {
                updateThumbnail(images[0].uri.toUri())
            } else {

            }
        }
    }

    private fun showDefaultThumbnail() {
        // Cập nhật thumbnail với ảnh mặc định từ drawable
        val defaultImageUri = Uri.parse("android.resource://${packageName}/drawable/image_background")
        updateThumbnail(defaultImageUri)
    }



    private fun eventCapture() {
        binding.btnCapture.setOnSafeClickListener {
            showShortToast("Capturing image...")

            imageCapture?.let { imageCapture ->
                val photoFile = createUniqueFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                imageCapture.takePicture(outputOptions,
                    cameraExecutor,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exc: ImageCaptureException) {
                            showShortToast("Error capturing image: ${exc.message}")
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                            capturedImages.add(savedUri)
                            viewModel.addImage(Image(0, savedUri.toString()))
                            updateThumbnail(savedUri)
                        }
                    })
            }
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    private fun createUniqueFile(baseFolder: File, format: String, extension: String): File = File(
        baseFolder,
        SimpleDateFormat(format, Locale.US).format(System.currentTimeMillis()) + extension
    )

    private fun showShortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun updateThumbnail(uri: Uri) {
        lifecycleScope.launch {
            Glide.with(binding.imageThumbnail.context).load(uri)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground).into(binding.imageThumbnail.source)
        }
    }

    private fun showImageGallery() {
        val galleryFragment = ImageGalleryFragment.newInstance()
        galleryFragment.show(supportFragmentManager, "imageGallery")
    }

    private fun toggleFlash() {
        isFlashEnabled = !isFlashEnabled
        camera?.cameraControl?.enableTorch(isFlashEnabled)
        binding.toggleFlash.setImageResource(
            if (isFlashEnabled) R.drawable.baseline_flash_on_24 else R.drawable.baseline_flash_off_24
        )
    }


    private fun switchCamera() {
        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
        startCamera() // Restart camera to apply changes
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
    }
}
