package com.ramm.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ramm.wastify.R
import com.ramm.wastify.databinding.ActivityEdukasiBinding

class EdukasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEdukasiBinding
    private var isOrganicExpanded = false
    private var isAnorganicExpanded = false
    private var isRecycleExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEdukasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set onClickListener for "Selengkapnya" button in organic card
        binding.textOrganicMore.setOnClickListener {
            toggleDescription(binding.descriptionOrganic, binding.textOrganicMore, isOrganicExpanded)
            isOrganicExpanded = !isOrganicExpanded
        }

        // Set onClickListener for "Selengkapnya" button in anorganic card
        binding.textAnorganicMore.setOnClickListener {
            toggleDescription(binding.descriptionAnorganic, binding.textAnorganicMore, isAnorganicExpanded)
            isAnorganicExpanded = !isAnorganicExpanded
        }

        // Set onClickListener for "Selengkapnya" button in recycle card
        binding.textRecycleMore.setOnClickListener {
            toggleDescription(binding.descriptionRecycle, binding.textRecycleMore, isRecycleExpanded)
            isRecycleExpanded = !isRecycleExpanded
        }

        // Set onClickListener for Home icon in navigation bar
        binding.iconHome.setOnClickListener {
            Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
        }

        // Set onClickListener for Features icon in navigation bar
        binding.iconFitur.setOnClickListener {
            Toast.makeText(this, "Features clicked", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, FeaturesActivity::class.java))
        }

        // Set onClickListener for User icon in navigation bar
        binding.iconUser.setOnClickListener {
            Toast.makeText(this, "User clicked", Toast.LENGTH_SHORT).show()
             startActivity(Intent(this, DashboardActivity::class.java))
        }
    }

    private fun toggleDescription(description: View, moreText: TextView, isExpanded: Boolean) {
        if (isExpanded) {
            description.visibility = View.GONE
            moreText.setText(R.string.selengkapnya)
        } else {
            description.visibility = View.VISIBLE
            moreText.setText(R.string.sedikit)
        }
    }
}
