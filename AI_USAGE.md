# Catatan Penggunaan AI (AI Usage Log)

| Tanggal         | Fitur / Komponen           | Alat AI | Deskripsi Bantuan AI                                         |
| :-------------- | :------------------------- | :------ | :----------------------------------------------------------- |
| 29 Oktober 2026 | Konversi Layout UI         | Gemini  | Pembentukan struktur UI Jetpack Compose & Material 3         |
| 29 Oktober 2026 | Fitur Pilihan Cepat Volume | Gemini  | Pembuatan `LazyRow` tombol cepat volume air (50 - 500 mL)    |
| 29 Oktober 2026 | Dropdown Catatan           | Gemini  | Implementasi `ExposedDropdownMenuBox` untuk catatan otomatis |
| 29 Oktober 2026 | Asset Ikon Aplikasi        | Gemini  | Panduan pembuatan Adaptive Icon melalui Image Asset          |

# Penggunaan AI — HydraTrack (HT-02)

Dokumen ini mencatat seluruh penggunaan AI yang memengaruhi proyek, sesuai kebijakan penggunaan AI pada Proyek UTS.

> Kolom **Modifikasi dan pemahaman** wajib ditulis dengan kata-kata sendiri oleh anggota yang menggunakan AI.

## Farid Heka Magfirio

| Tanggal     | Alat AI            | Prompt (ringkas)                                                                      | Bagian yang digunakan                                                                                                         | Modifikasi dan pemahaman mahasiswa                                          |
| ----------- | ------------------ | ------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------- |
| 21 Okt 2026 | Claude (Anthropic) | Meminta penjelasan isi dokumen tugas UTS dan pilihan studi kasus                      | Pemahaman spesifikasi tugas                                                                                                   | [tulis sendiri]                                                             |
| 21 Okt 2026 | Claude (Anthropic) | Meminta rencana pengerjaan dan rancangan model data HydraTrack                        | Rencana tahapan, rancangan `data class CatatanMinum`, batas validasi                                                          | [contoh: memahami alasan jumlahMl memakai Int dan target disimpan terpisah] |
| 26 Okt 2026 | Claude (Anthropic) | Meminta kode aplikasi HydraTrack dari awal sampai jadi                                | Kode awal `CatatanMinum.kt`, `HydraLogic.kt`, `MainActivity.kt`                                                               | [contoh: mengetik ulang kode, mempelajari state, LazyColumn, dan validasi]  |
| 27 Okt 2026 | Claude (Anthropic) | Meminta penjelasan fungsi validasi                                                    | Penjelasan `toIntOrNull`, operator Elvis, Regex                                                                               | [tulis sendiri]                                                             |
| 28 Okt 2026 | Claude (Anthropic) | Meminta bantuan memperbaiki error "Unresolved reference" dan "No parameter with name" | Penjelasan penyebab error dan cara perbaikan                                                                                  | [contoh: menambahkan fungsi yang belum ada dan parameter rataRata]          |
| 28 Okt 2026 | Claude (Anthropic) | Meminta fitur tambahan dan rencana commit                                             | Kode error hilang saat mengetik, rata-rata, sisa target                                                                       | [tulis sendiri]                                                             |
| 29 Okt 2026 | Claude (Anthropic) | Meminta pengecekan repository setelah dirombak                                        | Temuan: data class dan logika tidak dipakai, batas validasi berbeda, layout XML tidak terpakai                                | [tulis sendiri]                                                             |
| 29 Okt 2026 | Claude (Anthropic) | Meminta perbaikan agar `MainActivity` memakai `CatatanMinum` dan `HydraLogic`         | Refactor `MainActivity.kt`, `build.gradle.kts`, pemisahan `kosongkanForm`/`resetForm`, import `ExposedDropdownMenuAnchorType` | [tulis sendiri]                                                             |
| 29 Okt 2026 | Claude (Anthropic) | Meminta dokumen pengumpulan                                                           | Kerangka README, test case, CONTRIBUTIONS, PRESENTATION, AI_USAGE, wireframe                                                  | [contoh: mengisi NIM, hasil uji aktual, dan screenshot sendiri]             |

## Lucas Vincent Kurniawan Obedius Nobel

| Tanggal         | Fitur / Komponen           | Alat AI | Deskripsi Bantuan AI                                         |
| :-------------- | :------------------------- | :------ | :----------------------------------------------------------- |
| 29 Oktober 2026 | Konversi Layout UI         | Gemini  | Pembentukan struktur UI Jetpack Compose & Material 3         |
| 29 Oktober 2026 | Fitur Pilihan Cepat Volume | Gemini  | Pembuatan `LazyRow` tombol cepat volume air (50 - 500 mL)    |
| 29 Oktober 2026 | Dropdown Catatan           | Gemini  | Implementasi `ExposedDropdownMenuBox` untuk catatan otomatis |
| 29 Oktober 2026 | Asset Ikon Aplikasi        | Gemini  | Panduan pembuatan Adaptive Icon melalui Image Asset          |
