package com.example.calorietracker.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.calorietracker.AppPreferences
import com.example.calorietracker.R
import com.example.calorietracker.databinding.ActivityDashboardBinding
import com.example.calorietracker.main.MainActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi AppPreferences
        appPreferences = AppPreferences(this)

        with(binding){
            val navController = findNavController(R.id.nav_host_fragment)
            bottomNavigationView.setupWithNavController(navController)
        }

        // Handle intent from notification
        handleNotificationIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        // Handle new intent from notification
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        if (intent?.getBooleanExtra("open_beranda_fragment", false) == true) {
            // Buka BerandaFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, BerandaFragment())
                .commit()
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

            // Redirect to login page
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