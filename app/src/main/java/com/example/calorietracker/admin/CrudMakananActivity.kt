package com.example.calorietracker.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorietracker.AppPreferences
import com.example.calorietracker.Makanan
import com.example.calorietracker.R
import com.example.calorietracker.databinding.ActivityCrudMakananBinding
import com.example.calorietracker.main.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

class CrudMakananActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrudMakananBinding
    private lateinit var appPreferences: AppPreferences
    private val db = FirebaseFirestore.getInstance()
    private val makananCollection = db.collection("makanan")
    private lateinit var adapter: MakananAdminAdapter
    private val listMakanan = mutableListOf<Makanan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi AppPreferences
        appPreferences = AppPreferences(this)

        adapter = MakananAdminAdapter(listMakanan,
            onEditClickListener = { makanan -> onEditMakananClicked(makanan) },
            onDeleteClickListener = { makanan -> onDeleteMakananClicked(makanan) }
        )
        binding.rvListMakanan.adapter = adapter
        binding.rvListMakanan.layoutManager = LinearLayoutManager(this)

        binding.btnTambahMakanan.setOnClickListener {
            startActivity(Intent(this, TambahMakananAdminActivity::class.java))
        }

        loadMakanan()
    }

    private fun onEditMakananClicked(makanan: Makanan) {
        // Redirect ke EditMakananAdminActivity dan kirim data makanan
        val intent = Intent(this, EditMakananAdminActivity::class.java)
        intent.putExtra("makanan", makanan)
        startActivity(intent)
    }

    private fun onDeleteMakananClicked(makanan: Makanan) {
        // Tampilkan konfirmasi hapus
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus ${makanan.namaMakanan}?")
            .setPositiveButton("Ya") { _, _ ->
                // Hapus makanan dari Firestore
                makananCollection.document(makanan.id).delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Makanan berhasil dihapus", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal menghapus makanan", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun loadMakanan() {
        makananCollection.addSnapshotListener { value, error ->
            if (error != null) {
                // Handle error
                Log.d("CrudMakananActivity", "error listening for makanan",
                    error)
                return@addSnapshotListener
            }

            listMakanan.clear()
            value?.documents?.forEach {
                val note = it.toObject(Makanan::class.java)
                if (note != null) {
                    note.id = it.id
                    listMakanan.add(note)
                }
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                // Show confirmation dialog
                showLogoutConfirmationDialog()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Apakah anda yakin ingin logout?")
        builder.setPositiveButton("Ya") { _, _ ->
            // Handle Logout
            appPreferences.setLoggedIn(false)

            // Redirect to login page or perform other actions as needed
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        builder.setNegativeButton("Tidak") { _, _ ->
            // Do nothing, user canceled logout
        }

        val dialog = builder.create()
        dialog.show()
    }
}