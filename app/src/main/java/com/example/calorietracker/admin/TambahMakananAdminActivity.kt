package com.example.calorietracker.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.calorietracker.Makanan
import com.example.calorietracker.databinding.ActivityTambahMakananAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class TambahMakananAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahMakananAdminBinding
    private val db = FirebaseFirestore.getInstance()
    private val makananCollection = db.collection("makanan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahMakananAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSimpan.setOnClickListener {
            val namaMakanan = binding.edtNamaMakanan.text.toString()
            val jumlahKalori = binding.edtJumlahKalori.text.toString()

            val makanan = Makanan(namaMakanan = namaMakanan, jumlahKalori = jumlahKalori)
            makananCollection.add(makanan).addOnSuccessListener {
                Toast.makeText(this, "Makanan berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                finish()
            }
                .addOnFailureListener {
                    // Handle failure
                    Toast.makeText(this, "Gagal maenambahkan makanan", Toast.LENGTH_SHORT).show()
                }
        }

    }
}