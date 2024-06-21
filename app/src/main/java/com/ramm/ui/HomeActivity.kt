package com.ramm.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.data.adapter.PostAdapter
import com.data.model.PostModel
import com.data.model.PostResponse
import com.data.remote.ApiConfig
import com.ramm.wastify.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.system.exitProcess

class HomeActivity : AppCompatActivity() {
    private lateinit var userBinding: ActivityHomeBinding
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var call: Call<PostResponse>
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(userBinding.root)

        swipeRefresh = userBinding.refreshLayout
        recyclerView = userBinding.recyclerView

        postAdapter = PostAdapter { post -> postOnClick(post) }
        recyclerView.adapter = postAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        getPosts()

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
            getPosts()
        }
        userBinding.cameraIcon.setOnClickListener {
            val intent = Intent(this, KlasifikasiActivity::class.java)
            startActivity(intent)
        }

        userBinding.iconHome.setOnClickListener {
            // Handle Home icon click
            Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, HomeActivity::class.java))
        }

        userBinding.iconUser.setOnClickListener {
            // Handle Features icon click
            Toast.makeText(this, "Features clicked", Toast.LENGTH_SHORT).show()
             startActivity(Intent(this, EdukasiActivity::class.java))
        }

        userBinding.iconFitur.setOnClickListener {
            // Handle User icon click
            Toast.makeText(this, "Account clicked", Toast.LENGTH_SHORT).show()
             startActivity(Intent(this, DashboardActivity::class.java))
        }

        val backDispatcher = onBackPressedDispatcher
        backDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
                exitProcess(0)
            }
        })
    }

    private fun getPosts(){
        swipeRefresh.isRefreshing = true
        call = ApiConfig.apiService.getAll()
        call.enqueue(object : Callback<PostResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                swipeRefresh.isRefreshing = false
                if (response.isSuccessful) {
                    postAdapter.submitList(response.body()?.products)
                    postAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                swipeRefresh.isRefreshing = false
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun postOnClick(post: PostModel) {
        Toast.makeText(applicationContext, post.title, Toast.LENGTH_SHORT).show()
    }

}