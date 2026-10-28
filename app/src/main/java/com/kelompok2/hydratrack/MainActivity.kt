package com.kelompok2.hydratrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kelompok2.hydratrack.ui.theme.HydraTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HydraTrackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HydraTrackScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HydraTrackScreen(modifier: Modifier = Modifier) {
    // ===== 1. STATE FORMULIR =====
    var waktu by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var jenis by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("2000") }
    var catatan by remember { mutableStateOf("") }

    // ===== 2. STATE PESAN ERROR (null = tidak ada error) =====
    var errorWaktu by remember { mutableStateOf<String?>(null) }
    var errorJumlah by remember { mutableStateOf<String?>(null) }
    var errorJenis by remember { mutableStateOf<String?>(null) }
    var errorTarget by remember { mutableStateOf<String?>(null) }

    // ===== 3. STATE DATA =====
    val daftarCatatan = remember { mutableStateListOf<CatatanMinum>() }
    var targetHarian by remember { mutableIntStateOf(2000) }
    var idBerikutnya by remember { mutableIntStateOf(1) }

    // ===== 4. RINGKASAN (otomatis dihitung ulang saat data berubah) =====
    val totalMl = hitungTotal(daftarCatatan)
    val progres = hitungProgres(totalMl, targetHarian)

    fun kosongkanError() {
        errorWaktu = null
        errorJumlah = null
        errorJenis = null
        errorTarget = null
    }

    fun resetForm() {
        waktu = ""
        jumlah = ""
        jenis = ""
        target = "2000"
        catatan = ""
        kosongkanError()
    }

    fun tambahCatatan() {
        errorWaktu = validasiWaktu(waktu)
        errorJumlah = validasiJumlah(jumlah)
        errorJenis = validasiJenis(jenis)
        errorTarget = validasiTarget(target)

        val semuaValid = errorWaktu == null && errorJumlah == null &&
                errorJenis == null && errorTarget == null
        if (!semuaValid) return

        val catatanBaru = CatatanMinum(
            id = idBerikutnya,
            waktu = waktu.trim(),
            jumlahMl = jumlah.trim().toInt(),
            jenisMinuman = jenis,
            catatan = catatan.trim()
        )
        daftarCatatan.add(0, catatanBaru)   // data terbaru di paling atas
        idBerikutnya++
        targetHarian = target.trim().toInt()

        // Kosongkan form, target dibiarkan
        waktu = ""
        jumlah = ""
        jenis = ""
        catatan = ""
    }

    // ===== 5. TAMPILAN (satu LazyColumn agar seluruh layar bisa di-scroll) =====
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { HeaderSection() }

        // --- Formulir ---
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Catat Minum",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = waktu,
                        onValueChange = {
                            waktu = it
                            errorWaktu = null
                        },
                        label = { Text("Waktu minum (JJ:MM)") },
                        placeholder = { Text("08:30") },
                        isError = errorWaktu != null,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    PesanError(errorWaktu)

                    OutlinedTextField(
                        value = jumlah,
                        onValueChange = { jumlah = it },
                        label = { Text("Jumlah air (mL)") },
                        placeholder = { Text("250") },
                        isError = errorJumlah != null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    PesanError(errorJumlah)

                    Text("Jenis minuman", style = MaterialTheme.typography.labelLarge)
                    daftarJenisMinuman.forEach { pilihan ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = jenis == pilihan,
                                    onClick = {
                                        jenis = pilihan
                                        errorJenis = null
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = jenis == pilihan, onClick = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(pilihan)
                        }
                    }
                    PesanError(errorJenis)

                    OutlinedTextField(
                        value = target,
                        onValueChange = {
                            target = it
                            errorTarget = null
                        },
                        label = { Text("Target pribadi harian (mL)") },
                        isError = errorTarget != null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    PesanError(errorTarget)

                    OutlinedTextField(
                        value = catatan,
                        onValueChange = { if (it.length <= 100) catatan = it },
                        label = { Text("Catatan (opsional)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { tambahCatatan() },
                            modifier = Modifier.weight(1f)
                        ) { Text("Tambah") }

                        OutlinedButton(
                            onClick = { resetForm() },
                            modifier = Modifier.weight(1f)
                        ) { Text("Reset") }
                    }
                }
            }
        }

        // --- Ringkasan ---
        item {
            RingkasanCard(
                totalMl = totalMl,
                jumlahCatatan = daftarCatatan.size,
                targetHarian = targetHarian,
                progres = progres
            )
        }

        // --- Riwayat ---
        item {
            Text(
                text = "Riwayat Minum",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (daftarCatatan.isEmpty()) {
            item { EmptyState() }
        } else {
            items(daftarCatatan, key = { it.id }) { catatanItem ->
                CatatanCard(catatanItem)
            }
        }
    }
}

// ===== KOMPONEN-KOMPONEN KECIL =====

@Composable
fun HeaderSection() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "💧 HydraTrack",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Catat konsumsi air harianmu dan pantau progres menuju target pribadi.",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Aplikasi edukatif. Tidak menentukan kebutuhan cairan medis dan " +
                    "tidak untuk pasien dengan pembatasan cairan.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PesanError(pesan: String?) {
    if (pesan != null) {
        Text(
            text = pesan,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun RingkasanCard(totalMl: Int, jumlahCatatan: Int, targetHarian: Int, progres: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Ringkasan Hari Ini",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ItemRingkasan(label = "Total", nilai = "$totalMl mL")
                ItemRingkasan(label = "Minum", nilai = "$jumlahCatatan kali")
                ItemRingkasan(label = "Target", nilai = "$targetHarian mL")
            }
            LinearProgressIndicator(
                progress = { (progres / 100f).coerceAtMost(1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
            Text(text = "Progres: $progres%", fontWeight = FontWeight.SemiBold)
            Text(text = pesanProgres(progres), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ItemRingkasan(label: String, nilai: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Text(
            text = nilai,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CatatanCard(item: CatatanMinum) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.jenisMinuman,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Pukul ${item.waktu}", style = MaterialTheme.typography.bodySmall)
                if (item.catatan.isNotBlank()) {
                    Text(
                        text = "Catatan: ${item.catatan}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Text(
                text = "${item.jumlahMl} mL",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun EmptyState() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "💧", style = MaterialTheme.typography.displaySmall)
            Text(text = "Belum ada catatan minum", fontWeight = FontWeight.SemiBold)
            Text(
                text = "Isi formulir di atas lalu tekan Tambah.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HydraTrackPreview() {
    HydraTrackTheme {
        HydraTrackScreen()
    }
}