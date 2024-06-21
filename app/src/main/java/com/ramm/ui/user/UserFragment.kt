package com.ramm.ui.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.ramm.wastify.databinding.FragmentUserBinding

class UserFragment : Fragment() {

  private var _binding: FragmentUserBinding? = null
  private val binding get() = _binding!!
  private lateinit var auth: FirebaseAuth

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel =
            ViewModelProvider(this).get(UserViewModel::class.java)

    _binding = FragmentUserBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val auth = Firebase.auth
    val user = auth.currentUser
    user?.let {
      for (profile in it.providerData) {
        val providerId = profile.providerId

        val uid = profile.uid

        val name = profile.displayName
        val email = profile.email
        val photoUrl = profile.photoUrl
      }
    }

    val usernameView: TextView = binding.textUsername
    val emailView: TextView = binding.textEmail

    homeViewModel.username.observe(viewLifecycleOwner) {
      if (user != null) {
        usernameView.text = user.displayName
      } else {
        usernameView.text = it
      }
    }

    homeViewModel.email.observe(viewLifecycleOwner) {
      if (user != null) {
        emailView.text = user.email
      } else {
        emailView.text = it
      }
    }

    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}