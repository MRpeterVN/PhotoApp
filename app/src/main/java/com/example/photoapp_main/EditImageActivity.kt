package com.example.photoapp_main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.photoapp_main.data.Image
import com.example.photoapp_main.databinding.ActivityEditImageBinding
import com.example.photoapp_main.viewmodel.MainVM
import com.example.photoapp_main.viewmodel.MainVMFactory
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class EditImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditImageBinding
    private lateinit var viewModel: MainVM
    private lateinit var outputDirectory: File
    private var red = 1f
    private var green = 1f
    private var blue = 1f
    private var textX = 100f
    private var textY = 100f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = MainVMFactory(this)
        viewModel = ViewModelProvider(this, factory)[MainVM::class.java]

        outputDirectory = getOutputDirectory()

        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        binding.imageView.load(imageUri) {
            allowHardware(false)
        }

        binding.seekBarRed.setOnSeekBarChangeListener(colorAdjustListener { value -> red = value })
        binding.seekBarGreen.setOnSeekBarChangeListener(colorAdjustListener { value ->
            green = value
        })
        binding.seekBarBlue.setOnSeekBarChangeListener(colorAdjustListener { value ->
            blue = value
        })

        binding.btnSave.setOnClickListener {
            saveEditedImage()
            Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnCrop.setOnClickListener {
            cropImage(imageUri)
        }

        binding.btnAddText.setOnClickListener {
            addTextToImage("Your Text Here")
        }

        binding.seekBarX.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textX = progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.seekBarY.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textY = progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, "photo_app").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    private fun adjustColor() {
        val colorMatrix = ColorMatrix(
            floatArrayOf(
                red, 0f, 0f, 0f, 0f,
                0f, green, 0f, 0f, 0f,
                0f, 0f, blue, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        binding.imageView.colorFilter = ColorMatrixColorFilter(colorMatrix)
    }

    private fun colorAdjustListener(action: (Float) -> Unit) =
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                action(progress / 255f)
                adjustColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }

    private fun saveEditedImage() {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        options.inMutable = true
        val drawable = binding.imageView.drawable
        if (drawable == null) return

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        val file = createUniqueFile(
            outputDirectory,
            FILENAME,
            PHOTO_EXTENSION
        )

        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
        }

        val uri = Uri.fromFile(file).toString()
        val image = Image(0, uri)
        lifecycleScope.launch {
            viewModel.addImage(image)
        }
    }

    private fun cropImage(imageUri: Uri?) {
        imageUri ?: return
        val destinationUri =
            Uri.fromFile(File(outputDirectory, "cropped_${System.currentTimeMillis()}.jpg"))
        UCrop.of(imageUri, destinationUri)
            .withAspectRatio(1f, 1f) // Tỉ lệ cắt, có thể thay đổi
            .start(this)
    }

    private fun addTextToImage(text: String) {
        val drawable = binding.imageView.drawable ?: return
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        val paint = Paint().apply {
            color = Color.WHITE
            textSize = 50f
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        canvas.drawText(text, textX, textY, paint)

        binding.imageView.setImageBitmap(bitmap)
    }

    private fun createUniqueFile(baseFolder: File, format: String, extension: String): File =
        File(
            baseFolder,
            SimpleDateFormat(format, Locale.US).format(System.currentTimeMillis()) + extension
        )

    companion object {
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
    }
}

