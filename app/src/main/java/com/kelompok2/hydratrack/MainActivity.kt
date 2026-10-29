package com.kelompok2.hydratrack

import android.app.TimePickerDialog
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Locale

private val HydraTrackLightColors = lightColorScheme(
    primary = Color(0xFF1769AA),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD8ECFA),
    onPrimaryContainer = Color(0xFF164B73),
    background = Color(0xFFEAF6FF),
    surface = Color.White,
    onSurface = Color(0xFF164B73),
    surfaceVariant = Color(0xFFDDECF5),
    onSurfaceVariant = Color(0xFF426982),
    outline = Color(0xFFB6D6EA)
)

private val HydraTrackDarkColors = darkColorScheme(
    primary = Color(0xFF8CCBFF),
    onPrimary = Color(0xFF003354),
    primaryContainer = Color(0xFF164B73),
    onPrimaryContainer = Color(0xFFD8ECFA),
    background = Color(0xFF101A24),
    surface = Color(0xFF182735),
    onSurface = Color(0xFFE2F2FF),
    surfaceVariant = Color(0xFF263B4C),
    onSurfaceVariant = Color(0xFFC0D5E4),
    outline = Color(0xFF466276)
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var isDarkTheme by rememberSaveable {
                mutableStateOf(false)
            }

            MaterialTheme(
                colorScheme = if (isDarkTheme) {
                    HydraTrackDarkColors
                } else {
                    HydraTrackLightColors
                }
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->

                    HydraTrackScreen(
                        modifier = Modifier.padding(innerPadding),
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { pilihan ->
                            isDarkTheme = pilihan
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydraTrackScreen(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    onThemeChange: (Boolean) -> Unit = {}
) {
    var waktu by remember { mutableStateOf("") }
    var jumlah by remember { mutableStateOf("") }
    var jenis by remember { mutableStateOf("") }
    var target by rememberSaveable { mutableStateOf("2000") }
    var catatan by remember { mutableStateOf("") }
    var expandedCatatan by remember { mutableStateOf(false) }

    var errorWaktu by remember { mutableStateOf<String?>(null) }
    var errorJumlah by remember { mutableStateOf<String?>(null) }
    var errorJenis by remember { mutableStateOf<String?>(null) }
    var errorTarget by remember { mutableStateOf<String?>(null) }

    val daftarCatatan = remember {
        mutableStateListOf<CatatanMinum>()
    }

    var targetHarian by rememberSaveable {
        mutableIntStateOf(2000)
    }

    var idBerikutnya by remember {
        mutableIntStateOf(1)
    }

    val context = LocalContext.current

    // Ringkasan: semua rumus diambil dari HydraLogic.kt
    val totalMl = hitungTotal(daftarCatatan)
    val jumlahAktivitas = daftarCatatan.size
    val rataRata = hitungRataRata(daftarCatatan)

    fun setJamSaatIni() {
        val calendar = Calendar.getInstance()

        waktu = String.format(
            Locale.getDefault(),
            "%02d:%02d",
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )

        errorWaktu = null
    }

    fun bukaTimePicker() {
        val calendar = Calendar.getInstance()
        val bagianWaktu = waktu.split(":")

        val jam = if (bagianWaktu.size == 2) {
            bagianWaktu[0].toIntOrNull()
                ?: calendar.get(Calendar.HOUR_OF_DAY)
        } else {
            calendar.get(Calendar.HOUR_OF_DAY)
        }

        val menit = if (bagianWaktu.size == 2) {
            bagianWaktu[1].toIntOrNull()
                ?: calendar.get(Calendar.MINUTE)
        } else {
            calendar.get(Calendar.MINUTE)
        }

        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                waktu = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    hourOfDay,
                    minute
                )
                errorWaktu = null
            },
            jam,
            menit,
            true
        ).show()
    }

    // Validasi memakai fungsi di HydraLogic.kt (validasiWaktu, validasiJumlah, dst.)

    // Dipakai setelah data berhasil ditambah: form kosong, target tetap
    fun kosongkanForm() {
        waktu = ""
        jumlah = ""
        jenis = ""
        catatan = ""
        expandedCatatan = false

        errorWaktu = null
        errorJumlah = null
        errorJenis = null
    }

    // Dipakai tombol Reset: semua kembali ke kondisi awal, termasuk target
    fun resetForm() {
        kosongkanForm()
        target = "2000"
        targetHarian = 2000
        errorTarget = null
    }

    fun tambahCatatan() {
        errorWaktu = validasiWaktu(waktu)
        errorJumlah = validasiJumlah(jumlah)
        errorJenis = validasiJenis(jenis)
        errorTarget = validasiTarget(target)

        val semuaValid =
            errorWaktu == null &&
                    errorJumlah == null &&
                    errorJenis == null &&
                    errorTarget == null

        if (!semuaValid) return

        val jumlahAngka = jumlah.trim().toIntOrNull()
            ?: return

        val targetAngka = target.trim().toIntOrNull()
            ?: return

        val catatanBaru = CatatanMinum(
            id = idBerikutnya,
            waktu = waktu.trim(),
            jumlahMl = jumlahAngka,
            jenisMinuman = jenis,
            catatan = catatan.trim()
        )

        daftarCatatan.add(0, catatanBaru)
        idBerikutnya++

        targetHarian = targetAngka

        kosongkanForm()
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            HydraHeaderSection(
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange
            )
        }

        item {
            HydraTargetProgressCard(
                target = target,
                onTargetChange = { nilai ->
                    target = nilai
                    errorTarget = validasiTarget(nilai)

                    val angka = nilai.trim().toIntOrNull()

                    if (angka != null && angka in HydraRules.MIN_TARGET..HydraRules.MAX_TARGET) {
                        targetHarian = angka
                    }
                },
                errorTarget = errorTarget,
                totalMl = totalMl,
                targetHarian = targetHarian
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Catat Minum",
                        style = MaterialTheme.typography.titleLarge,
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { setJamSaatIni() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Jam Saat Ini")
                        }

                        OutlinedButton(
                            onClick = { bukaTimePicker() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Pilih Jam")
                        }
                    }

                    HydraErrorText(errorWaktu)

                    OutlinedTextField(
                        value = jumlah,
                        onValueChange = {
                            jumlah = it
                            errorJumlah = null
                        },
                        label = { Text("Jumlah air (mL)") },
                        placeholder = { Text("250") },
                        isError = errorJumlah != null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val pilihanCepat = listOf("50", "100", "200", "250", "300", "500")

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(pilihanCepat) { volume ->
                            OutlinedButton(
                                onClick = {
                                    jumlah = volume
                                    errorJumlah = null
                                }
                            ) {
                                Text("$volume mL")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    HydraErrorText(errorJumlah)

                    Text(
                        text = "Jenis minuman",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )

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
                            RadioButton(
                                selected = jenis == pilihan,
                                onClick = {
                                    jenis = pilihan
                                    errorJenis = null
                                }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(text = pilihan)
                        }
                    }

                    HydraErrorText(errorJenis)

                    val pilihanCatatanCepat = listOf("Bangun tidur", "Setelah olahraga", "Habis makan", "Cuaca panas", "Sebelum tidur")

                    ExposedDropdownMenuBox(
                        expanded = expandedCatatan,
                        onExpandedChange = { expandedCatatan = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = catatan,
                            onValueChange = {
                                if (it.length <= 100) {
                                    catatan = it
                                }
                            },
                            label = { Text("Catatan (opsional)") },
                            placeholder = { Text("Ketik atau pilih dari daftar") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCatatan)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                        )

                        ExposedDropdownMenu(
                            expanded = expandedCatatan,
                            onDismissRequest = { expandedCatatan = false }
                        ) {
                            pilihanCatatanCepat.forEach { teksCatatan ->
                                DropdownMenuItem(
                                    text = { Text(teksCatatan) },
                                    onClick = {
                                        catatan = teksCatatan
                                        expandedCatatan = false
                                    }
                                )
                            }
                        }
                    }

                    Text(
                        text = "${catatan.length}/100 karakter",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { tambahCatatan() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Tambah Catatan")
                        }

                        OutlinedButton(
                            onClick = { resetForm() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Reset")
                        }
                    }
                }
            }
        }

        item {
            HydraSummaryCard(
                totalMl = totalMl,
                jumlahAktivitas = jumlahAktivitas,
                targetHarian = targetHarian,
                rataRata = rataRata
            )
        }

        item {
            Text(
                text = "Riwayat Minum",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (daftarCatatan.isEmpty()) {
            item {
                HydraEmptyState()
            }
        } else {
            items(
                items = daftarCatatan,
                key = { it.id }
            ) { item ->
                HydraRecordCard(item)
            }
        }
    }
}

@Composable
private fun HydraHeaderSection(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "💧 HydraTrack",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isDarkTheme) "Gelap" else "Cerah",
                    style = MaterialTheme.typography.labelMedium
                )

                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = onThemeChange
                )
            }
        }

        Text(
            text = "Pemantauan konsumsi air",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Catat konsumsi air harianmu dan pantau progres menuju target pribadi.",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Aplikasi ini bersifat edukatif, bukan alat diagnosis atau penentu kebutuhan cairan medis.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun HydraTargetProgressCard(
    target: String,
    onTargetChange: (String) -> Unit,
    errorTarget: String?,
    totalMl: Int,
    targetHarian: Int
) {
    val progresAktual = hitungProgres(totalMl, targetHarian)

    val progresBar = (
            totalMl.toFloat() / targetHarian
            ).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Target pribadi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = target,
                onValueChange = onTargetChange,
                label = { Text("Target harian (mL)") },
                placeholder = { Text("2000") },
                isError = errorTarget != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier.fillMaxWidth()
            )

            HydraErrorText(errorTarget)

            Text(
                text = "${formatHydraNumber(totalMl)} / ${formatHydraNumber(targetHarian)} mL",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            LinearProgressIndicator(
                progress = { progresBar },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Text(
                text = "$progresAktual% dari target",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = pesanProgres(progresAktual),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun HydraErrorText(pesan: String?) {
    if (pesan != null) {
        Text(
            text = pesan,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun HydraSummaryCard(
    totalMl: Int,
    jumlahAktivitas: Int,
    targetHarian: Int,
    rataRata: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Ringkasan Hari Ini",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HydraSummaryItem(
                    label = "Total",
                    value = "${formatHydraNumber(totalMl)} mL"
                )

                HydraSummaryItem(
                    label = "Aktivitas",
                    value = "$jumlahAktivitas kali"
                )
            }

            Text(
                text = "Rata-rata per minum: ${formatHydraNumber(rataRata)} mL",
                style = MaterialTheme.typography.bodyMedium
            )

            val sisa = hitungSisa(totalMl, targetHarian)

            Text(
                text = if (sisa > 0) {
                    "Sisa ${formatHydraNumber(sisa)} mL menuju target."
                } else {
                    "Target pribadi sudah tercapai."
                },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun HydraSummaryItem(
    label: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun HydraRecordCard(item: CatatanMinum) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.jenisMinuman,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Pukul ${item.waktu}",
                    style = MaterialTheme.typography.bodySmall
                )

                if (item.catatan.isNotBlank()) {
                    Text(
                        text = "Catatan: ${item.catatan}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "${formatHydraNumber(item.jumlahMl)} mL",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun HydraEmptyState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "💧",
                style = MaterialTheme.typography.displaySmall
            )

            Text(
                text = "Belum ada catatan minum",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Isi formulir di atas, lalu tekan Tambah Catatan.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

private fun formatHydraNumber(number: Int): String {
    return number
        .toString()
        .reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()
}

@Preview(showBackground = true)
@Composable
private fun HydraTrackPreview() {
    MaterialTheme(colorScheme = HydraTrackLightColors) {
        HydraTrackScreen()
    }
}