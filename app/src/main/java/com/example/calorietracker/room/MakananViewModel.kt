package com.example.calorietracker.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.calorietracker.room.MakananDao
import com.example.calorietracker.room.MakananDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.calorietracker.room.MakananEntity

class MakananViewModel(application: Application) : AndroidViewModel(application) {

    private val makananDao: MakananDao

    val allMakanan: LiveData<List<MakananEntity>>

    init {
        val db = MakananDatabase.getDatabase(application)
        makananDao = db.makananDao()
        allMakanan = makananDao.getAllMakanan()
    }

    fun tambahMakanan(makanan: MakananEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            makananDao.insertMakanan(makanan)
        }
    }

    fun getMakananByUser(username: String): LiveData<List<MakananEntity>> {
        return makananDao.getMakananByUser(username)
    }

    fun deleteMakanan(makanan: MakananEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            makananDao.deleteMakanan(makanan)
        }
    }

    fun updateMakanan(makanan: MakananEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            makananDao.updateMakanan(makanan)
        }
    }

}

