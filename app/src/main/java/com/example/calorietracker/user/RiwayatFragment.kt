package com.example.calorietracker.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calorietracker.AppPreferences
import com.example.calorietracker.room.MakananEntity
import com.example.calorietracker.room.MakananViewModel
import com.example.calorietracker.RiwayatMakananAdapter
import com.example.calorietracker.databinding.FragmentRiwayatBinding


class RiwayatFragment : Fragment() {

    private lateinit var makananViewModel: MakananViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        val recyclerView = binding.rvRiwayatMakanan
        val adapter = RiwayatMakananAdapter()

        binding.btnTambahMakanan.setOnClickListener {
            startActivity(Intent(requireContext(), TambahMakananActivity::class.java))
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize ViewModel
        makananViewModel = ViewModelProvider(this).get(MakananViewModel::class.java)

        // Observe the LiveData from ViewModel
        makananViewModel.getMakananByUser(getUsernameFromPreferences())
            .observe(viewLifecycleOwner, { makananList ->
                // Update the UI with the list of makanan for the current user
                adapter.submitList(makananList)
            })

        // Set listener untuk adapter
        adapter.onEditClickListener = object : RiwayatMakananAdapter.OnEditClickListener {
            override fun onEditClick(makanan: MakananEntity) {
                // Panggil fungsi untuk membuka activity edit makanan
                onEditMakanan(makanan)
            }
        }

        adapter.onDeleteClickListener = object : RiwayatMakananAdapter.OnDeleteClickListener {
            override fun onDeleteClick(makanan: MakananEntity) {
                // Panggil fungsi untuk menampilkan konfirmasi hapus makanan
                showDeleteConfirmationDialog(makanan)
            }
        }

        return binding.root
    }

    // membuka activity edit makanan
    private fun onEditMakanan(makanan: MakananEntity) {
        val intent = Intent(requireContext(), EditRiwayatMakananActivity::class.java)
        intent.putExtra(EditRiwayatMakananActivity.EXTRA_MAKANAN, makanan)
        startActivity(intent)
    }

    // menampilkan konfirmasi hapus makanan
    private fun showDeleteConfirmationDialog(makanan: MakananEntity) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Konfirmasi Hapus")
        alertDialogBuilder.setMessage("Apakah Anda yakin ingin menghapus item ini?")
        alertDialogBuilder.setPositiveButton("Ya") { _, _ ->
            // Panggil fungsi untuk menghapus makanan dari database
            onDeleteMakanan(makanan)
        }
        alertDialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    // Fungsi untuk menghapus makanan dari database
    private fun onDeleteMakanan(makanan: MakananEntity) {
        makananViewModel.deleteMakanan(makanan)
        Toast.makeText(requireContext(), "Makanan berhasil dihapus", Toast.LENGTH_SHORT).show()
    }

    private fun getUsernameFromPreferences(): String {
        // Retrieve username from preferences (you can implement your own logic)
        val appPreferences = AppPreferences(requireContext())
        return appPreferences.getUsername()
    }

}
