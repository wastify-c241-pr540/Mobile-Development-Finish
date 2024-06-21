package com.ramm.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ramm.wastify.R
import com.ramm.wastify.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photoUri = Uri.parse(intent.getStringExtra(EXTRA_PHOTO_URI))
        photoUri?.let {
            binding.ivResult.setImageURI(it)
        }
        val extraLabel = intent.getStringExtra(EXTRA_LABEL)
        val extraPrediction = intent.getStringExtra(EXTRA_PREDICTION)

        Log.d("ResultActivity", "Prediction: $extraPrediction")
        binding.resultText.text = when (extraPrediction) {
            "organic" -> getString(R.string.organik)
            "inorganic" -> getString(R.string.inorganik)
            "anorganik" -> getString(R.string.anorganik)
            "daur_ulang" -> getString(R.string.daur_ulang)
            else -> getString(R.string.tidak_diketahui)
        }

        


    }



    companion object {
        const val EXTRA_PHOTO_URI = "extra_photo_uri"
        const val EXTRA_LABEL = "extra_label"
        const val EXTRA_PREDICTION = "extra_prediction"
    }
}
