package com.example.calorietracker.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.calorietracker.AppPreferences
import com.example.calorietracker.databinding.FragmentProfilBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class ProfilFragment : Fragment() {

    private lateinit var binding: FragmentProfilBinding
    val db = Firebase.firestore
    val usersCollection = db.collection("users")
    private lateinit var appPreferences: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi AppPreferences
        appPreferences = AppPreferences(requireContext())

        val username = appPreferences.getUsername()
        usersCollection.document(username)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val nama = document.getString("nama")
                    val tinggiBadan = document.getString("tinggiBadan")
                    val beratBadan = document.getString("beratBadan")

                    binding.tvNama.text = nama
                    binding.tvTinggiBadan.text = tinggiBadan
                    binding.tvBeratBadan.text = beratBadan

                } else {
                    // Dokumen tidak ditemukan
                    Log.d("TAG", "Document not found")
                }
            }
            .addOnFailureListener { exception ->
                // Penanganan kesalahan
                Log.d("TAG", "get failed with ", exception)
            }
    }
}