package com.example.calorietracker.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.calorietracker.AppPreferences
import com.example.calorietracker.room.MakananEntity
import com.example.calorietracker.room.MakananViewModel
import com.example.calorietracker.databinding.FragmentBerandaBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class BerandaFragment : Fragment() {

    private lateinit var binding: FragmentBerandaBinding
    val db = Firebase.firestore
    val usersCollection = db.collection("users")
    private lateinit var appPreferences: AppPreferences

    private lateinit var makananViewModel: MakananViewModel

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getTargetKaloriFromFirestore(username: String) {
        usersCollection.document(username)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val targetKalori = document.getString("targetKalori")?.toInt() ?: 0
                    updateSisaKalori(targetKalori)
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

    private fun updateSisaKalori(targetKalori: Int) {
        // Dapatkan total kalori dari makanan menggunakan MakananViewModel
        makananViewModel.getMakananByUser(getUsernameFromPreferences()).observe(viewLifecycleOwner, { makananList ->
            val totalKalori = calculateTotalKalori(makananList)
            val sisaKalori = targetKalori - totalKalori

            binding.tvSisaKalori.text = sisaKalori.toString()
            binding.tvTargetKalori.text = targetKalori.toString()
            binding.tvTotalKalori.text = totalKalori.toString()

            // Hitung persentase kalori yang dikonsumsi
            val progress = ((totalKalori.toDouble() / targetKalori) * 100).toInt()
            setCircularProgressBar(progress)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize progress bar
        progressBar = binding.progressBar

        // Inisialisasi AppPreferences
        appPreferences = AppPreferences(requireContext())

        // Initialize ViewModel
        makananViewModel = ViewModelProvider(this).get(MakananViewModel::class.java)

        // Panggil fungsi getTargetKaloriFromFirestore dengan username dari preferences
        val username = getUsernameFromPreferences()
        getTargetKaloriFromFirestore(username)

    }

    private fun setCircularProgressBar(progress: Int) {
        // Update progress bar
        progressBar.progress = progress
    }

    private fun calculateTotalKalori(makananList: List<MakananEntity>): Int {
        // perhitungan total kalori dari makananList
        var totalKalori = 0
        for (makanan in makananList) {
            totalKalori += makanan.jumlahKalori
        }
        return totalKalori
    }

    private fun getUsernameFromPreferences(): String {
        // Retrieve username from preferences
        val appPreferences = AppPreferences(requireContext())
        return appPreferences.getUsername()
    }
}