package com.kelompok2.hydratrack

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvTargetPesan: TextView
    private lateinit var progressBarHydration: ProgressBar
    private lateinit var tvTotalVolume: TextView
    private lateinit var etJumlahAir: EditText
    private lateinit var etJenisMinuman: EditText
    private lateinit var btnSimpan: Button

    private var totalVolumeMiliLiter: Int = 0
    private var jumlahAktivitas: Int = 0
    private val targetHarianMiliLiter: Int = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTargetPesan = findViewById(R.id.tvTargetPesan)
        progressBarHydration = findViewById(R.id.progressBarHydration)
        tvTotalVolume = findViewById(R.id.tvTotalVolume)
        etJumlahAir = findViewById(R.id.etJumlahAir)
        etJenisMinuman = findViewById(R.id.etJenisMinuman)
        btnSimpan = findViewById(R.id.btnSimpan)

        updateTampilanProgres()

        btnSimpan.setOnClickListener {
            tambahCatatanMinum()
        }
    }

    private fun tambahCatatanMinum() {
        val inputAirStr = etJumlahAir.text.toString().trim()
        val jenisMinuman = etJenisMinuman.text.toString().trim()

        if (inputAirStr.isEmpty()) {
            etJumlahAir.error = "Jumlah air harus diisi!"
            return
        }

        if (jenisMinuman.isEmpty()) {
            etJenisMinuman.error = "Jenis minuman harus diisi!"
            return
        }

        val jumlahAir = inputAirStr.toIntOrNull()

        if (jumlahAir == null || jumlahAir <= 0) {
            etJumlahAir.error = "Masukkan angka yang valid!"
            return
        }

        if (jumlahAir > 2000) {
            Toast.makeText(this, "Batas wajar terlampaui! Sekali minum maksimal 2000 mL.", Toast.LENGTH_LONG).show()
            return
        }

        totalVolumeMiliLiter += jumlahAir
        jumlahAktivitas += 1

        updateTampilanProgres()

        etJumlahAir.text.clear()
        etJenisMinuman.text.clear()

        Toast.makeText(this, "Catatan berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
    }

    private fun updateTampilanProgres() {
        val persentase = if (targetHarianMiliLiter > 0) {
            ((totalVolumeMiliLiter.toFloat() / targetHarianMiliLiter) * 100).toInt()
        } else {
            0
        }

        progressBarHydration.progress = if (persentase > 100) 100 else persentase

        tvTotalVolume.text = "Terkumpul: $totalVolumeMiliLiter mL ($persentase%) | $jumlahAktivitas Aktivitas"

        when {
            totalVolumeMiliLiter == 0 -> {
                tvTargetPesan.text = "Target: $targetHarianMiliLiter mL | Ayo mulai minum air pertamamu!"
            }
            totalVolumeMiliLiter < targetHarianMiliLiter -> {
                val sisa = targetHarianMiliLiter - totalVolumeMiliLiter
                tvTargetPesan.text = "Target: $targetHarianMiliLiter mL | Kurang $sisa mL lagi, tetap semangat!"
            }
            else -> {
                tvTargetPesan.text = "Target: $targetHarianMiliLiter mL | Luar biasa! Target harianmu sudah tercapai! 🎉"
            }
        }
    }
}