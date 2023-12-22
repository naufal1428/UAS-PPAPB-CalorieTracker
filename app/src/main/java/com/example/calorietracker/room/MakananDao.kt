package com.example.calorietracker.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.calorietracker.room.MakananEntity

@Dao
interface MakananDao {
    @Insert
    suspend fun insertMakanan(makanan: MakananEntity)

    @Query("SELECT * FROM makanan_table WHERE username = :username")
    fun getMakananByUser(username: String): LiveData<List<MakananEntity>>

    @Query("SELECT * FROM makanan_table")
    fun getAllMakanan(): LiveData<List<MakananEntity>>

    @Delete
    suspend fun deleteMakanan(makanan: MakananEntity)

    @Update
    suspend fun updateMakanan(makanan: MakananEntity)

}




