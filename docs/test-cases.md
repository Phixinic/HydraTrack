# Test Case — HydraTrack (HT-02)

Pengujian manual dilakukan di perangkat **TECNO POVA 6** dan **VIVO**, tanggal **29 Oktober 2026**.

| No  | Skenario                                                          | Hasil yang Diharapkan                                                                                     | Hasil Aktual / Status                                                   |
| --- | ----------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| 1   | Membuka aplikasi pertama kali                                     | Aplikasi terbuka tanpa crash dan menampilkan judul, kartu target, formulir, ringkasan, serta empty state. | ✅ Sesuai ([Gambar A.1](screenshot/1-awal.jpeg))                        |
| 2   | Mengisi waktu, jumlah, dan jenis yang valid lalu menambah catatan | Catatan muncul di riwayat, formulir kosong kembali, dan ringkasan berubah.                                | ✅ Sesuai ([Gambar A.1](screenshot/1-awal.jpeg))                        |
| 3   | Mengosongkan waktu, jumlah, dan jenis lalu menambah catatan       | Pesan kesalahan muncul pada ketiga input dan catatan tidak ditambahkan.                                   | ✅ Sesuai ([Gambar A.3](screenshot/2-error.jpeg))                       |
| 4   | Mengisi jumlah dengan huruf                                       | Pesan "Jumlah harus berupa angka bulat" muncul dan aplikasi tidak crash.                                  | ✅ Sesuai ([Gambar A.3](screenshot/2-error.jpeg))                       |
| 5   | Mengisi jumlah 0, negatif, atau lebih dari 2.000 mL               | Pesan kesalahan jumlah muncul dan catatan ditolak.                                                        | ✅ Sesuai (hasilnya sama seperti [Gambar A.3](screenshot/2-error.jpeg)) |
| 6   | Menambahkan lebih dari satu catatan                               | Semua catatan muncul, yang terbaru berada di atas.                                                        | ✅ Sesuai ([Gambar A.2](screenshot/3-data.jpeg))                        |
| 7   | Mengubah target dan menambah konsumsi                             | Progres, sisa target, dan pesan progres mengikuti total serta target.                                     | ✅ Sesuai                                                               |
| 8   | Menekan Reset                                                     | Formulir kosong, pesan kesalahan hilang, dan target kembali 2.000 mL.                                     | ✅ Sesuai                                                               |
| 9   | Mengganti tema cerah/gelap                                        | Warna komponen mengikuti tema yang dipilih dan data tidak hilang.                                         | ✅ Sesuai ([Gambar A.4](screenshot/4-gelap.jpeg))                       |
| 10  | Menggulir layar ke bagian riwayat                                 | Konten tetap bisa dijangkau dan tidak menutupi bagian lain.                                               | ✅ Sesuai ([Gambar A.2](screenshot/3-data.jpeg))                        |

**Ringkasan:** 10 dari 10 skenario sesuai dengan hasil yang diharapkan.

Nomor gambar mengikuti Lampiran A pada laporan.

## Defect yang Ditemukan dan Perbaikannya

| No  | Defect                                                                                               | Perbaikan                                                                                                         | Commit                                            |
| --- | ---------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------- | ------------------------------------------------- |
| 1   | Pesan kesalahan tetap tampil walaupun input sudah diperbaiki                                         | State error di-set `null` setiap kali input berubah                                                               | `clear error message when input changes`          |
| 2   | Batas validasi pada layar berbeda dengan `HydraLogic.kt` dan data class `CatatanMinum` tidak dipakai | Layar memakai `CatatanMinum` serta fungsi validasi dan perhitungan di `HydraLogic.kt`                             | `use CatatanMinum and HydraLogic in MainActivity` |
| 3   | Tombol Reset tidak mengembalikan target ke nilai awal                                                | Dipisah menjadi `kosongkanForm()` (setelah tambah data) dan `resetForm()` (juga mengembalikan target ke 2.000 mL) | `use CatatanMinum and HydraLogic in MainActivity` |
