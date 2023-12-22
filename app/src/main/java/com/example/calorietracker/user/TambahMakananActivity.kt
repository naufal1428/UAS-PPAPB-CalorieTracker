package com.example.calorietracker.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorietracker.AppPreferences
import com.example.calorietracker.Makanan
import com.example.calorietracker.R
import com.example.calorietracker.databinding.ActivityTambahMakananBinding
import com.google.firebase.firestore.FirebaseFirestore

class TambahMakananActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahMakananBinding
    private lateinit var appPreferences: AppPreferences
    private val db = FirebaseFirestore.getInstance()
    private val makananCollection = db.collection("makanan")
    private lateinit var adapter: MakananUserAdapter
    private val listMakanan = mutableListOf<Makanan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi AppPreferences
        appPreferences = AppPreferences(this)

        binding.btnCustomMakanan.setOnClickListener {
            startActivity(Intent(this, FormTambahMakananCustomActivity::class.java))
        }

        adapter = MakananUserAdapter(listMakanan) { makanan -> onMakananClicked(makanan) }
        binding.rvListMakanan.adapter = adapter
        binding.rvListMakanan.layoutManager = LinearLayoutManager(this)
        binding.rvListMakanan.setHasFixedSize(true)

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })


        loadMakanan()
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

    private fun onMakananClicked(makanan: Makanan) {
        val intent = Intent(this, FormTambahMakananActivity::class.java)
        intent.putExtra("makanan", makanan)
        startActivity(intent)
    }

}