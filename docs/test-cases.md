# Test Case — HydraTrack (HT-02)

Pengujian manual dilakukan di perangkat: **[isi nama HP / emulator]**, tanggal: **[isi tanggal]**.

Kolom **Hasil Aktual** dan **Status** diisi setelah benar-benar mencoba di aplikasi.

| No | Skenario | Langkah | Input | Hasil yang Diharapkan | Hasil Aktual | Status |
|---|---|---|---|---|---|---|
| 1 | Aplikasi pertama dibuka | Buka aplikasi | - | Judul, kartu target (0 / 2.000 mL, 0%), form, ringkasan (0 mL, 0 kali), dan empty state "Belum ada catatan minum" tampil | | ☐ |
| 2 | Seluruh input valid | Isi semua kolom, tekan **Tambah Catatan** | Waktu `08:30`, jumlah `250`, jenis Air putih | Data muncul di riwayat, form kosong kembali, target tetap 2000, total 250 mL, progres 12%, pesan "Masih jauh dari target" | | ☐ |
| 3 | Input wajib kosong | Kosongkan waktu, jumlah, jenis; tekan **Tambah Catatan** | Semua kosong | Pesan error muncul di waktu, jumlah, dan jenis minuman; data tidak ditambahkan | | ☐ |
| 4 | Angka berisi huruf | Isi jumlah dengan huruf, tekan **Tambah Catatan** | Jumlah `abc` | Pesan "Jumlah harus berupa angka bulat", aplikasi tidak crash | | ☐ |
| 5 | Nilai negatif / nol | Isi jumlah negatif, lalu nol | Jumlah `-100` lalu `0` | Pesan "Jumlah harus lebih dari 0 mL", data ditolak | | ☐ |
| 6 | Menambahkan beberapa data | Tambahkan 3 data valid berturut-turut | 250, 300, 500 mL | Ketiga data tampil di LazyColumn, data terbaru paling atas | | ☐ |
| 7 | Memeriksa ringkasan | Lihat ringkasan setelah skenario 6 (target 2000) | - | Total 1.050 mL, 3 kali, rata-rata 350 mL, progres 52%, sisa 950 mL, pesan "Sudah lebih dari setengah target" | | ☐ |
| 8 | Tombol reset | Isi beberapa kolom dan ubah target, tekan **Reset** | Sembarang | Semua kolom kosong, target kembali 2000, pesan error hilang | | ☐ |
| 9 | Jumlah di luar batas | Isi jumlah terlalu besar | Jumlah `3000` | Pesan "Maksimal 2000 mL per catatan" | | ☐ |
| 10 | Format waktu salah | Isi waktu tidak sesuai format | `25:00` dan `8.30` | Pesan "Format waktu harus JJ:MM, contoh 08:30" | | ☐ |
| 11 | Target di luar batas | Isi target terlalu kecil / besar | Target `100` lalu `6000` | Pesan "Target harus antara 500–5000 mL" | | ☐ |
| 12 | Target tercapai | Ubah target, tambah data sampai total ≥ target | Target `500`, jumlah `500` | Progres 100%, pesan "Target pribadi hari ini tercapai!", ringkasan "Target pribadi sudah tercapai." | | ☐ |
| 13 | Tombol waktu | Tekan **Jam Saat Ini**, lalu **Pilih Jam** | - | Kolom waktu terisi jam sekarang; TimePicker muncul dan jam terpilih masuk ke kolom | | ☐ |
| 14 | Pilihan cepat & catatan | Tekan tombol `250 mL`, pilih catatan dari dropdown | - | Kolom jumlah berisi 250, kolom catatan terisi, penghitung karakter berubah | | ☐ |
| 15 | Mode gelap | Geser Switch di pojok kanan atas | - | Warna aplikasi berubah ke tema gelap, data tidak hilang | | ☐ |

## Defect yang ditemukan dan perbaikannya

| No | Defect | Perbaikan | Commit |
|---|---|---|---|
| 1 | Pesan error tetap tampil walaupun input sudah diperbaiki | State error di-set `null` setiap input berubah | `clear error message when input changes` |
| 2 | Batas validasi di layar berbeda dengan `HydraLogic.kt` (5000 / 10000 mL), dan data class buatan tidak dipakai | Layar memakai `CatatanMinum` dan fungsi di `HydraLogic.kt` | `refactor: use CatatanMinum and HydraLogic in MainActivity` |
| 3 | Tombol Reset tidak mengembalikan target ke awal | Dipisah: `kosongkanForm()` setelah tambah, `resetForm()` juga mengembalikan target ke 2000 | `refactor: use CatatanMinum and HydraLogic in MainActivity` |
| 4 | [isi jika menemukan bug lain] | | |
