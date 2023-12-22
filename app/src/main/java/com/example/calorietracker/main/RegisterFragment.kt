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
import com.example.calorietracker.user.DashboardActivity
import com.example.calorietracker.R
import com.example.calorietracker.User
import com.example.calorietracker.databinding.FragmentRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class RegisterFragment : Fragment() {

    private lateinit var binding : FragmentRegisterBinding
    // Dapatkan instance Firebase Firestore
    val db = Firebase.firestore
    // Ambil referensi koleksi pengguna
    val usersCollection = db.collection("users")
    private lateinit var appPreferences: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi AppPreferences
        appPreferences = AppPreferences(requireContext())

        binding.navigateToLogin.setOnClickListener{
            // Mendapatkan ViewPager dari parent activity
            val viewPager = requireActivity().findViewById<ViewPager2>(R.id.viewPager)
            // Pindah ke Fragment Login (index 1) menggunakan ViewPager setCurrentItem
            viewPager.currentItem = 1
        }

        binding.btnRegister.setOnClickListener{
            val username = binding.usernameEditText.text.toString()
            val nama = binding.namaEditText.text.toString()
            val beratBadan = binding.beratBadanEditText.text.toString()
            val tinggiBadan = binding.tinggiBadanEditText.text.toString()
            val targetKalori = binding.targetKaloriEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val konfirmasiPassword = binding.konfirmasiPasswordEditText.text.toString()

            if (username.isEmpty() || nama.isEmpty() || beratBadan.isEmpty() || password.isEmpty()
                || tinggiBadan.isEmpty() || targetKalori.isEmpty() || konfirmasiPassword.isEmpty()){

                Toast.makeText(requireContext(), "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()

            } else if (password != konfirmasiPassword) {
                Toast.makeText(requireContext(), "Password tidak sama",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            else{
                usersCollection.document(username)
                    .get().addOnSuccessListener { document ->
                        if (document.exists()) {
                            Toast.makeText(requireContext(), "Username sudah digunakan", Toast.LENGTH_SHORT).show()
                        } else {
                            val user = User(
                                username = username,
                                nama = nama,
                                beratBadan = beratBadan,
                                tinggiBadan = tinggiBadan,
                                targetKalori = targetKalori,
                                password = password,
                                role = "user"
                            )

                            usersCollection.document(user.username)
                                .set(user)
                                .addOnSuccessListener {
                                    appPreferences.setLoggedIn(true)
                                    appPreferences.saveUsername(username)

                                    // Penanganan sukses
                                    Toast.makeText(requireContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                                    val intentToDashboardActivity =
                                        Intent(requireContext(), DashboardActivity::class.java)
                                    startActivity(intentToDashboardActivity)

                                    binding.usernameEditText.text.clear()
                                    binding.namaEditText.text.clear()
                                    binding.beratBadanEditText.text.clear()
                                    binding.tinggiBadanEditText.text.clear()
                                    binding.targetKaloriEditText.text.clear()
                                    binding.passwordEditText.text.clear()
                                    binding.konfirmasiPasswordEditText.text.clear()
                                }
                                .addOnFailureListener { e ->
                                    // Penanganan kesalahan
                                    Toast.makeText(requireContext(), "Registrasi gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }

            }
        }
    }
}