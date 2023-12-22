package com.example.calorietracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calorietracker.databinding.ItemRiwayatMakananBinding
import com.example.calorietracker.room.MakananEntity


class RiwayatMakananAdapter :
    ListAdapter<MakananEntity, RiwayatMakananAdapter.MakananViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakananViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRiwayatMakananBinding.inflate(inflater, parent, false)
        return MakananViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MakananViewHolder, position: Int) {
        val currentMakanan = getItem(position)
        holder.bind(currentMakanan)
    }

    inner class MakananViewHolder(private val binding: ItemRiwayatMakananBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnEdit.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val makanan = getItem(position)
                    // Panggil fungsi untuk meng-handle edit makanan
                    onEditClickListener?.onEditClick(makanan)
                }
            }

            binding.btnDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val makanan = getItem(position)
                    // Panggil fungsi untuk meng-handle hapus makanan
                    onDeleteClickListener?.onDeleteClick(makanan)
                }
            }
        }

        fun bind(makanan: MakananEntity) {
            with(binding) {
                tvWaktuMakan.text = makanan.waktuMakan
                tvItemNamaMakanan.text = makanan.namaMakanan
                tvItemJumlahKalori.text = makanan.jumlahKalori.toString()
            }
        }
    }

    // Listener untuk tombol edit
    interface OnEditClickListener {
        fun onEditClick(makanan: MakananEntity)
    }

    // Listener untuk tombol hapus
    interface OnDeleteClickListener {
        fun onDeleteClick(makanan: MakananEntity)
    }

    // Inisialisasi listener pada RiwayatMakananAdapter
    var onEditClickListener: OnEditClickListener? = null
    var onDeleteClickListener: OnDeleteClickListener? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MakananEntity>() {
            override fun areItemsTheSame(oldItem: MakananEntity, newItem: MakananEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MakananEntity, newItem: MakananEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
