# Rencana Presentasi — HydraTrack (HT-02)

Durasi total sekitar 10–12 menit.

## 1. Masalah, pengguna, dan batasan (2 menit) — Lucas Vincent Kurniawan Obedius Nobel

- Masalah: orang sibuk mudah lupa minum dan tidak tahu sudah minum berapa banyak.
- Pengguna: mahasiswa, pekerja, masyarakat umum.
- Solusi: mencatat setiap minum dan melihat progres terhadap target pribadi.
- Batasan: edukatif saja, bukan alat diagnosis, tidak menentukan kebutuhan cairan medis, tidak untuk pasien dengan pembatasan cairan, data simulasi.

## 2. Demonstrasi (4 menit) — Heka

1. Buka aplikasi: tunjukkan kartu target 0%, ringkasan 0, dan empty state.
2. Tekan **Tambah Catatan** dengan form kosong: tunjukkan pesan error.
3. Isi jumlah `abc`, lalu `-100`, lalu `3000`: tunjukkan aplikasi tidak crash.
4. Ketik ulang di kolom yang error: pesan merah langsung hilang.
5. Tekan **Jam Saat Ini**, pilih tombol cepat `250 mL`, pilih jenis, pilih catatan dari dropdown, lalu tambah. Ulangi sampai 3 data.
6. Tunjukkan LazyColumn, ringkasan, dan progres berubah otomatis.
7. Ubah target menjadi 500: tunjukkan pesan "tercapai".
8. Geser Switch mode gelap.
9. Tekan **Reset**: form kosong, target kembali 2000.

## 3. Defense individual dan live modification (4–6 menit)

### Pertanyaan yang mungkin muncul

| Pertanyaan                                                   | Poin jawaban                                                                                                           |
| ------------------------------------------------------------ | ---------------------------------------------------------------------------------------------------------------------- |
| Kenapa `jumlahMl` memakai `Int`?                             | Karena dijumlahkan dan dihitung persentasenya; String akan tersambung, bukan dijumlah                                  |
| Kenapa target tidak ada di data class?                       | Target berlaku untuk satu hari, bukan tiap catatan, jadi disimpan sebagai state terpisah                               |
| Kenapa logika dipisah ke `HydraLogic.kt`?                    | Aturan dan rumus ada di satu tempat; ubah `HydraRules` sekali, seluruh aplikasi ikut berubah                           |
| Bagaimana aplikasi tidak crash saat diisi huruf?             | `toIntOrNull()` menghasilkan `null`, lalu operator Elvis `?:` mengembalikan pesan error                                |
| Apa beda `remember` dan `mutableStateOf`?                    | `mutableStateOf` membuat nilai yang dipantau Compose; `remember` menjaga nilai itu tetap ada saat layar digambar ulang |
| Apa beda `remember` dan `rememberSaveable`?                  | `rememberSaveable` tetap tersimpan saat layar diputar (rotasi); dipakai untuk tema dan target                          |
| Kenapa memakai `mutableStateListOf`?                         | Supaya Compose tahu saat isi daftar bertambah, sehingga LazyColumn dan ringkasan ter-update otomatis                   |
| Kenapa seluruh layar memakai satu LazyColumn?                | LazyColumn di dalam Column yang bisa di-scroll akan error; jadi header, form, dan ringkasan dijadikan `item`           |
| Apa beda LazyColumn dan LazyRow?                             | LazyColumn menggulir vertikal (riwayat), LazyRow menggulir horizontal (tombol pilihan cepat)                           |
| Apa beda `kosongkanForm()` dan `resetForm()`?                | Setelah tambah data, form dikosongkan tapi target tetap; tombol Reset juga mengembalikan target ke 2000                |
| Bagaimana mode gelap bekerja?                                | State `isDarkTheme` menentukan `colorScheme` mana yang dipakai `MaterialTheme`; Switch mengubah state itu              |
| Kenapa `total * 100 / target`, bukan `total / target * 100`? | Pembagian Int dibulatkan ke bawah; jika dibagi duluan hasilnya selalu 0                                                |

### Latihan live modification

- Ubah batas maksimal jumlah menjadi 1500 mL (cukup ubah `HydraRules.MAX_ML`).
- Tambah jenis minuman baru "Kopi" (tambah di `daftarJenisMinuman`).
- Ubah target default dari 2000 menjadi 2500 (state `target`, `targetHarian`, dan `resetForm`).
- Tambah tombol cepat 750 mL (tambah di `pilihanCepat`).
- Tambah pilihan catatan "Saat bekerja" (tambah di `pilihanCatatanCepat`).
- Ubah teks pesan progres di fungsi `pesanProgres`.
