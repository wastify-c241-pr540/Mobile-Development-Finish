package com.ramm.ui
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ramm.ui.user.UserFragment
import com.ramm.ui.user.UserViewModel
import com.ramm.wastify.LoginActivity
import com.ramm.wastify.R
import com.ramm.wastify.databinding.ActivityDashboardBinding
import com.ramm.wastify.databinding.FragmentUserBinding
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var pieChart: PieChart
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var viewModel: UserViewModel

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iconHome.setOnClickListener {
            Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        binding.iconUser.setOnClickListener {
            Toast.makeText(this, "Account clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        binding.iconFitur.setOnClickListener {
            Toast.makeText(this, "Features clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, EdukasiActivity::class.java))
            finish()
        }

        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        val profileImage = findViewById<ImageView>(R.id.image_user)
        val photoUrl = firebaseUser?.photoUrl
        Glide.with(this)
            .load(photoUrl)
            .placeholder(R.drawable.baseline_person_24)
            .error(R.drawable.baseline_error_24)
            .into(profileImage)

        if (firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setSupportActionBar(binding.appBarDashboard.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home, R.id.nav_setting, R.id.nav_about), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        showPieChart()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_logout -> {
                showLogoutDialog(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    private fun fetchData() {
//        pieChartData =
//        profilePictureUrl =
//        viewModel.updatePieChartData(pieChartData)
//        viewModel.updateProfilePictureUrl(profilePictureUrl)
//    }

    private fun showPieChart(){
        pieChart = findViewById(R.id.pie_chart)

        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.setDragDecelerationFrictionCoef(0.95f)

        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f

        pieChart.setDrawCenterText(true)

        pieChart.setRotationAngle(0f)

        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        pieChart.animateY(1400, Easing.EaseInOutQuad)

        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)

        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(70f))
        entries.add(PieEntry(20f))
        entries.add(PieEntry(10f))

        val dataSet = PieDataSet(entries, "Trashes")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.purple_200))
        colors.add(resources.getColor(R.color.yellow))
        colors.add(resources.getColor(R.color.red))

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.setData(data)

        pieChart.highlightValues(null)

        pieChart.invalidate()
    }

    private fun showLogoutDialog(item: MenuItem) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.menu_log_out))
        builder.setMessage(getString(R.string.logout_dialog))
            .setPositiveButton(getString(R.string.logout_confirm)) { _, _ ->
                signOut(item)
            }
            .setNegativeButton(getString(R.string.logout_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    fun signOut(item: MenuItem) {
        lifecycleScope.launch {
        val credentialManager = CredentialManager.create(this@DashboardActivity)

        auth.signOut()
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
        startActivity(Intent(this@DashboardActivity, LoginActivity::class.java))
        finish()
    }
    }

    fun getPieChartData(): List<Float> {
        return viewModel.pieChartData.value ?: emptyList()
    }

    fun getProfilePictureUrl(): String? {
        return viewModel.profilePictureUrl.value
    }
}