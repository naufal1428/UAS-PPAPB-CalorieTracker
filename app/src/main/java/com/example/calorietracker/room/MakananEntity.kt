package com.example.calorietracker.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "makanan_table")
data class MakananEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,
    val username: String,
    @ColumnInfo(name = "waktuMakan")
    val waktuMakan: String,
    @ColumnInfo(name = "namaMakanan")
    val namaMakanan: String,
    @ColumnInfo(name = "takaranSaji")
    val takaranSaji: Int,
    @ColumnInfo(name = "jumlahKalori")
    val jumlahKalori: Int
) : Serializable

