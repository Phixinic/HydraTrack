package com.kelompok2.hydratrack

// Satu object = satu kali aktivitas minum
data class CatatanMinum(
    val id: Int,
    val waktu: String,          // format "08:30"
    val jumlahMl: Int,          // angka, bukan String
    val jenisMinuman: String,
    val catatan: String
)