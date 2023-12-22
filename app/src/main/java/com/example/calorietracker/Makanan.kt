package com.example.calorietracker

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Makanan(
    @set: Exclude @get:Exclude var id: String = "",
    var namaMakanan : String = "",
    var jumlahKalori : String = ""
) : Serializable
