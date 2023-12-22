package com.example.calorietracker.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.calorietracker.Makanan
import com.example.calorietracker.R
import com.example.calorietracker.databinding.ActivityEditMakananAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditMakananAdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMakananAdminBinding
    private val db = FirebaseFirestore.getInstance()
    private val makananCollection = db.collection("makanan")
    private lateinit var makanan: Makanan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMakananAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makanan = intent.getSerializableExtra("makanan") as Makanan

        binding.edtNamaMakanan.setText(makanan.namaMakanan)
        binding.edtJumlahKalori.setText(makanan.jumlahKalori)

        binding.btnUpdate.setOnClickListener {
            // Simpan perubahan ke Firestore
            val updatedNamaMakanan = binding.edtNamaMakanan.text.toString()
            val updatedJumlahKalori = binding.edtJumlahKalori.text.toString()

            makanan.namaMakanan = updatedNamaMakanan
            makanan.jumlahKalori = updatedJumlahKalori

            makananCollection.document(makanan.id)
                .set(makanan)
                .addOnSuccessListener {
                    Toast.makeText(this, "Makanan berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal memperbarui makanan", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
