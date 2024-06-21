package com.ramm.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.IOException
import com.ramm.wastify.MainActivity
import com.ramm.wastify.databinding.ActivityKlasifikasiBinding
import com.ramm.wastify.ml.WastifyMetadata
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.TensorImage

class KlasifikasiActivity : AppCompatActivity() {
    private lateinit var userBinding: ActivityKlasifikasiBinding
    private var currentImageUri: Uri? = null
    private lateinit var model: WastifyMetadata
    private lateinit var labels: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userBinding = ActivityKlasifikasiBinding.inflate(layoutInflater)
        setContentView(userBinding.root)

        model = WastifyMetadata.newInstance(this)
        labels = FileUtil.loadLabels(this, "labelmap.txt")

        userBinding.btnChooseImage.setOnClickListener { startGallery() }
        userBinding.btnAnalyze.setOnClickListener {
            if (currentImageUri != null) {
                val intent = Intent(this, ResultActivity::class.java)
                analyzeImage(intent)

            } else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
        }
        userBinding.btnTakePhoto.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startGallery() {
        launcherGallery.launch("image/*")
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            currentImageUri = it
            userBinding.ivPreview.setImageURI(it)
        }
    }

    private fun analyzeImage(intent: Intent) {
        currentImageUri?.let { uri ->
            try {
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()

                val model = WastifyMetadata.newInstance(this)
                val image = TensorImage.fromBitmap(bitmap)

                val outputs = model.process(image)
                Log.d("KlasifikasiActivity", "Output: $outputs")
                val detectionResult = outputs.detectionResultList[0]

                val location = detectionResult.scoreAsFloat
                Log.d("KlasifikasiActivity", "Location: $location")
                val category = detectionResult.locationAsRectF
                Log.d("KlasifikasiActivity", "Category: $category")
                val score = detectionResult.categoryAsString
                Log.d("KlasifikasiActivity", "Score: $score")

                model.close()
                moveToResult(score)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveToResult(prediction: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_LABEL, "label") // You need to set the correct label value here
        intent.putExtra(ResultActivity.EXTRA_PHOTO_URI, currentImageUri.toString())
        intent.putExtra(ResultActivity.EXTRA_PREDICTION, prediction)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        model.close() // Release model resources
    }

    companion object {
        const val EXTRA_PHOTO_URI = "extra_photo_uri"
        const val EXTRA_LABEL = "extra_label"
        const val EXTRA_PREDICTION = "extra_prediction"
    }
}