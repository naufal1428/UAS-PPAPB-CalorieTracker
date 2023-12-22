package com.example.calorietracker.user

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.calorietracker.Makanan
import com.example.calorietracker.databinding.ItemMakananUserBinding

class MakananUserAdapter(
    private val listMakananFull: List<Makanan>,
    private val onMakananClickListener: (Makanan) -> Unit
) : RecyclerView.Adapter<MakananUserAdapter.MakananUserViewHolder>(), Filterable {

    private var listMakanan: List<Makanan> = listMakananFull

    inner class MakananUserViewHolder(private val binding: ItemMakananUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(makanan : Makanan) {
            binding.tvItemNamaMakanan.text = makanan.namaMakanan
            binding.tvItemJumlahKalori.text = makanan.jumlahKalori

            itemView.setOnClickListener {
                onMakananClickListener(makanan)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MakananUserViewHolder {
        val binding = ItemMakananUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MakananUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MakananUserViewHolder, position: Int) {
        holder.bind(listMakanan[position])
    }

    override fun getItemCount(): Int {
        return listMakanan.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Makanan>()

                if (constraint.isNullOrBlank()) {
                    filteredList.addAll(listMakananFull)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()

                    for (makanan in listMakananFull) {
                        if (makanan.namaMakanan.toLowerCase().contains(filterPattern)) {
                            filteredList.add(makanan)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listMakanan = results?.values as List<Makanan>
                notifyDataSetChanged()
            }
        }
    }

}