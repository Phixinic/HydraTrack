package com.kelompok2.hydratrack

// Batas wajar untuk tugas (bukan standar medis)
object HydraRules {
    const val MIN_ML = 1
    const val MAX_ML = 2000
    const val MIN_TARGET = 500
    const val MAX_TARGET = 5000
}

val daftarJenisMinuman = listOf("Air putih", "Teh", "Jus", "Susu", "Lainnya")

// ===== VALIDASI =====
// Mengembalikan pesan error, atau null kalau input valid

fun validasiWaktu(input: String): String? {
    if (input.isBlank()) return "Waktu minum wajib diisi"
    val formatJam = Regex("^([01]\\d|2[0-3]):[0-5]\\d$")
    if (!formatJam.matches(input.trim())) return "Format waktu harus JJ:MM, contoh 08:30"
    return null
}

fun validasiJumlah(input: String): String? {
    if (input.isBlank()) return "Jumlah air wajib diisi"
    val angka = input.trim().toIntOrNull() ?: return "Jumlah harus berupa angka bulat"
    if (angka < HydraRules.MIN_ML) return "Jumlah harus lebih dari 0 mL"
    if (angka > HydraRules.MAX_ML) return "Maksimal ${HydraRules.MAX_ML} mL per catatan"
    return null
}

fun validasiJenis(jenis: String): String? {
    return if (jenis.isBlank()) "Pilih jenis minuman" else null
}

fun validasiTarget(input: String): String? {
    if (input.isBlank()) return "Target harian wajib diisi"
    val angka = input.trim().toIntOrNull() ?: return "Target harus berupa angka bulat"
    if (angka < HydraRules.MIN_TARGET || angka > HydraRules.MAX_TARGET) {
        return "Target harus antara ${HydraRules.MIN_TARGET}–${HydraRules.MAX_TARGET} mL"
    }
    return null
}

// ===== PERHITUNGAN & RINGKASAN =====

fun hitungTotal(daftar: List<CatatanMinum>): Int {
    return daftar.sumOf { it.jumlahMl }
}

fun hitungProgres(total: Int, target: Int): Int {
    if (target <= 0) return 0
    return total * 100 / target
}

fun pesanProgres(persen: Int): String {
    return when {
        persen == 0 -> "Belum ada catatan minum. Yuk mulai minum!"
        persen < 50 -> "Masih jauh dari target, jangan lupa minum ya."
        persen < 100 -> "Sudah lebih dari setengah target, lanjutkan!"
        else -> "Target pribadi hari ini tercapai! 🎉"
    }
}