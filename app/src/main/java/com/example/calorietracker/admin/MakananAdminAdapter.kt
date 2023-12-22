package com.example.calorietracker.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calorietracker.Makanan
import com.example.calorietracker.databinding.ItemMakananAdminBinding

class MakananAdminAdapter(
    private val listMakanan:List<Makanan>,
    private val onEditClickListener: (Makanan) -> Unit,
    private val onDeleteClickListener: (Makanan) -> Unit
): RecyclerView.Adapter<MakananAdminAdapter.MakananAdminViewHolder>() {

    inner class MakananAdminViewHolder(private val binding: ItemMakananAdminBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            // Set listener pada item view atau tombol edit/hapus
            binding.btnEdit.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEditClickListener(listMakanan[position])
                }
            }

            binding.btnDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener(listMakanan[position])
                }
            }
        }

        fun bind(makanan : Makanan) {
            binding.tvItemNamaMakanan.text = makanan.namaMakanan
            binding.tvItemJumlahKalori.text = makanan.jumlahKalori
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MakananAdminViewHolder {
        val binding = ItemMakananAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MakananAdminViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MakananAdminViewHolder,
        position: Int
    ) {
        holder.bind(listMakanan[position])
    }

    override fun getItemCount(): Int {
        return listMakanan.size
    }
}