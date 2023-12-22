package com.example.calorietracker.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.calorietracker.room.MakananEntity
import com.example.calorietracker.room.MakananViewModel
import com.example.calorietracker.R
import com.example.calorietracker.databinding.ActivityEditRiwayatMakananBinding

class EditRiwayatMakananActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRiwayatMakananBinding
    private lateinit var makananViewModel: MakananViewModel
    private lateinit var makananEntity: MakananEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRiwayatMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ViewModel
        makananViewModel = ViewModelProvider(this).get(MakananViewModel::class.java)

        // Dapatkan data MakananEntity dari Intent
        makananEntity = intent.getSerializableExtra(EXTRA_MAKANAN) as MakananEntity

        // Set nilai awal pada elemen UI
        binding.spinnerWaktuMakan.setSelection(getIndex(binding.spinnerWaktuMakan, makananEntity.waktuMakan))
        binding.edtNamaMakanan.setText(makananEntity.namaMakanan)
        binding.edtJumlahKalori.setText(makananEntity.jumlahKalori.toString())

        // Atur adapter untuk Spinner waktu makan
        val waktuMakanArray = resources.getStringArray(R.array.waktu_makan_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, waktuMakanArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerWaktuMakan.adapter = adapter

        // Handle klik tombol Update Makanan
        binding.btnUpdateMakanan.setOnClickListener {
            updateMakanan()
        }
    }

    private fun updateMakanan() {
        // Ambil nilai dari elemen UI
        val waktuMakan = binding.spinnerWaktuMakan.selectedItem.toString()
        val namaMakanan = binding.edtNamaMakanan.text.toString().trim()
        val jumlahKalori = binding.edtJumlahKalori.text.toString().toInt()

        // Validasi input
        if (namaMakanan.isEmpty()) {
            binding.edtNamaMakanan.error = "Nama makanan tidak boleh kosong"
            return
        }

        // Buat objek MakananEntity baru dengan data yang diperbarui
        val updatedMakanan = MakananEntity(
            id = makananEntity.id,
            username = makananEntity.username,
            waktuMakan = waktuMakan,
            namaMakanan = namaMakanan,
            takaranSaji = makananEntity.takaranSaji,
            jumlahKalori = jumlahKalori
        )

        // Panggil fungsi pada ViewModel untuk melakukan update
        makananViewModel.updateMakanan(updatedMakanan)

        // Selesai, kembali ke activity sebelumnya
        Toast.makeText(this, "Makanan berhasil diupdate", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == myString) {
                return i
            }
        }
        return 0
    }

    companion object {
        const val EXTRA_MAKANAN = "extra_makanan"
    }
}
