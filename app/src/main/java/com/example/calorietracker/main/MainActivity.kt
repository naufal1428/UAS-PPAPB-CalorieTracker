package com.example.calorietracker.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.calorietracker.AppPreferences
import com.example.calorietracker.admin.CrudMakananActivity
import com.example.calorietracker.user.DashboardActivity
import com.example.calorietracker.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewPager2: ViewPager2
    lateinit var mediator: TabLayoutMediator
    private lateinit var appPreferences: AppPreferences
    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            viewPager2 = viewPager
            viewPager.adapter = TabAdapter(supportFragmentManager,
                this@MainActivity.lifecycle)
            mediator = TabLayoutMediator(tabLayout, viewPager)
            {tab, position ->
                when (position){
                    0-> tab.text = "Register"
                    1-> tab.text = "Login"
                }
            }
            mediator.attach()
        }

        // Inisialisasi AppPreferences
        appPreferences = AppPreferences(this)

        // Cek status login
        checkLoginStatus()

    }

    private fun checkLoginStatus() {
        if (appPreferences.isLoggedIn()) {
            val userRole = appPreferences.getUserRole()
            if (userRole == "admin") {
                val intent = Intent(this, CrudMakananActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intentToDashboardActivity = Intent(this, DashboardActivity::class.java)
                startActivity(intentToDashboardActivity)
                finish()
            }
        }
    }

}