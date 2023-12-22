package com.example.calorietracker.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.calorietracker.AppPreferences
import com.example.calorietracker.admin.CrudMakananActivity
import com.example.calorietracker.user.DashboardActivity
import com.example.calorietracker.R
import com.example.calorietracker.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    val db = Firebase.firestore
    val usersCollection = db.collection("users")
    private lateinit var appPreferences: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi AppPreferences
        appPreferences = AppPreferences(requireContext())

        binding.navigateToRegister.setOnClickListener{
            // Mendapatkan ViewPager dari parent activity
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
            // Pindah ke Fragment Register (index 0) menggunakan ViewPager setCurrentItem
            viewPager.currentItem = 0
        }

        binding.btnLogin.setOnClickListener{
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isNotEmpty()&&password.isNotEmpty()){
                usersCollection.document(username)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            // Pengguna ditemukan, periksa password
                            val storedPassword = document.getString("password")
                            val userRole = document.getString("role")

                            if (storedPassword == password) {
                                // Setelah login berhasil
                                appPreferences.setLoggedIn(true)
                                appPreferences.saveUsername(username)

                                if (userRole == "admin") {
                                    appPreferences.setUserRole("admin")
                                    // Login admin ke halaman CRUD makanan
                                    Toast.makeText(requireContext(), "Login berhasil", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(requireContext(), CrudMakananActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                } else {
                                    appPreferences.setUserRole("user")
                                    // Login user ke halaman dashboard
                                    Toast.makeText(requireContext(), "Login berhasil", Toast.LENGTH_SHORT).show()
                                    val intentToDashboardActivity =
                                        Intent(requireContext(), DashboardActivity::class.java)
                                    startActivity(intentToDashboardActivity)
                                    requireActivity().finish()
                                }

                                // Mengosongkan input field
                                binding.usernameEditText.text.clear()
                                binding.passwordEditText.text.clear()
                            } else {
                                // Password salah
                                Toast.makeText(requireContext(), "Password salah", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Pengguna tidak ditemukan
                            Toast.makeText(requireContext(), "Username tidak ditemukan", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        // Penanganan kesalahan
                        Toast.makeText(requireContext(), "Login gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

            }
            else{
                Toast.makeText(requireContext(), "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}