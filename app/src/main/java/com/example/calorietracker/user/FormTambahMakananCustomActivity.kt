package com.example.calorietracker.user

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.calorietracker.AppPreferences
import com.example.calorietracker.room.MakananEntity
import com.example.calorietracker.room.MakananViewModel
import com.example.calorietracker.R
import com.example.calorietracker.databinding.ActivityFormTambahMakananCustomBinding

class FormTambahMakananCustomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormTambahMakananCustomBinding
    private lateinit var makananViewModel: MakananViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormTambahMakananCustomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atur adapter untuk Spinner waktu makan
        val waktuMakanArray = resources.getStringArray(R.array.waktu_makan_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, waktuMakanArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerWaktuMakan.adapter = adapter

        // initialize the ViewModel
        makananViewModel = ViewModelProvider(this).get(MakananViewModel::class.java)

        binding.btnTambahMakanan.setOnClickListener {
            tambahMakanan()
        }
    }

    private fun tambahMakanan() {
        val waktuMakan: String = binding.spinnerWaktuMakan.selectedItem.toString()
        val namaMakanan: String = binding.edtNamaMakanan.text.toString()
        val takaranSaji: Int = binding.edtTakaranSaji.text.toString().toInt()
        val jumlahKalori: Int = binding.edtJumlahKalori.text.toString().toInt()

        val makanan = MakananEntity(
            username = getUsernameFromPreferences(),
            waktuMakan = waktuMakan,
            namaMakanan = namaMakanan,
            takaranSaji = takaranSaji,
            jumlahKalori = jumlahKalori
        )

        // Save to Room Database using ViewModel
        makananViewModel.tambahMakanan(makanan)

        // Clear input fields
//        binding.edtNamaMakanan.text.clear()
//        binding.edtTakaranSaji.text.clear()
//        binding.edtJumlahKalori.text.clear()

        // finish the activity
        showNotification("Makanan Ditambahkan", "Anda berhasil menambahkan makanan yang anda konsumsi.")
        finish()
    }

    private fun showNotification(title: String, content: String) {
        // Intent untuk membuka Dashboard
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("open_dashboard", true)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id",
                "channel_name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) // Tambahkan PendingIntent
            .setAutoCancel(true) // Menghapus notifikasi saat diklik

        notificationManager.notify(1, builder.build())
    }

    private fun getUsernameFromPreferences(): String {
        // Retrieve username from preferences
        val appPreferences = AppPreferences(this)
        return appPreferences.getUsername()
    }
}

