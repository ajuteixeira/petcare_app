package br.unifor.petcare__ong.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.unifor.petcare__ong.ui.components.AppBottomBar
import br.unifor.petcare__ong.ui.navigation.Routes
import br.unifor.petcare__ong.ui.session.SessionManager
import br.unifor.petcare__ong.ui.viewmodel.ExportViewModel
import br.unifor.petcare__ong.ui.viewmodel.ReportType
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportReportsScreen(navController: NavController, viewModel: ExportViewModel = viewModel()) {
    val state by viewModel.state
    val context = LocalContext.current
    val darkBlue = Color(0xFF0D1B3E)
    val tealPrimary = Color(0xFF009688)
    val grayText = Color(0xFF707B81)
    val lightBackground = Color(0xFFF8F9FA)

    if (SessionManager.tipoUsuario != "GESTOR") {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.AnimalList.route) {
                popUpTo(Routes.ExportReports.route) { inclusive = true }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Exportar Relatórios",
                        fontWeight = FontWeight.Bold,
                        color = darkBlue,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = darkBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = Routes.ExportReports.route,
                tipoUsuario = SessionManager.tipoUsuario
            )
        },
        containerColor = lightBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Tipo de Relatório
            ReportTypeSection(
                selectedType = state.selectedType,
                onTypeSelected = { viewModel.onTypeSelected(it) },
                darkBlue = darkBlue,
                tealColor = tealPrimary
            )

            // Section 2: Período
            PeriodSection(
                startDate = state.startDate,
                endDate = state.endDate,
                onDatesChanged = { start, end -> viewModel.onDateChanged(start, end) },
                darkBlue = darkBlue,
                grayText = grayText
            )

            // Section 3: Pré-visualização
            PreviewSection(
                animals = state.animals,
                reportType = state.selectedType,
                darkBlue = darkBlue,
                grayText = grayText,
                tealColor = tealPrimary
            )

            Button(
                onClick = { viewModel.exportToCsv(context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = tealPrimary)
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Gerar e Compartilhar CSV", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ReportTypeSection(
    selectedType: ReportType,
    onTypeSelected: (ReportType) -> Unit,
    darkBlue: Color,
    tealColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE0F2F1)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = tealColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Tipo de Relatório",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = darkBlue
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ReportType.entries.forEach { type ->
                    ReportOption(
                        label = type.label,
                        isSelected = selectedType == type,
                        onSelect = { onTypeSelected(type) },
                        tealColor = tealColor,
                        darkBlue = darkBlue
                    )
                }
            }
        }
    }
}

@Composable
fun ReportOption(label: String, isSelected: Boolean, onSelect: () -> Unit, tealColor: Color, darkBlue: Color) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) BorderStroke(1.5.dp, tealColor.copy(alpha = 0.5f)) else BorderStroke(1.dp, Color(0xFFEEEEEE)),
        color = if (isSelected) tealColor.copy(alpha = 0.05f) else Color.White
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(selectedColor = tealColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 14.sp, color = darkBlue, fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal)
        }
    }
}

@Composable
fun PeriodSection(
    startDate: String,
    endDate: String,
    onDatesChanged: (String, String) -> Unit,
    darkBlue: Color,
    grayText: Color
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    fun showDatePicker(currentDate: String, onDateSelected: (String) -> Unit) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val date = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                onDateSelected(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE8EAF6)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF3F51B5),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Período",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = darkBlue
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            DateField("Data Inicial", if (startDate.isEmpty()) "dd/mm/aaaa" else startDate, grayText) {
                showDatePicker(startDate) { onDatesChanged(it, endDate) }
            }
            Spacer(modifier = Modifier.height(16.dp))
            DateField("Data Final", if (endDate.isEmpty()) "dd/mm/aaaa" else endDate, grayText) {
                showDatePicker(endDate) { onDatesChanged(startDate, it) }
            }
        }
    }
}

@Composable
fun DateField(label: String, value: String, grayText: Color, onClick: () -> Unit) {
    Column {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF0D1B3E), fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF8F9FA),
            border = BorderStroke(1.dp, Color(0xFFEEEEEE))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = value, color = if (value.contains("d")) grayText else Color.Black, fontSize = 14.sp)
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = Color(0xFF0D1B3E),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun PreviewSection(
    animals: List<br.unifor.petcare__ong.model.Animal>,
    reportType: ReportType,
    darkBlue: Color,
    grayText: Color,
    tealColor: Color
) {
    Column {
        Text(
            text = "Pré-visualização",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = darkBlue
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFEEEEEE)),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = tealColor
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "PETCARE ONG", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = darkBlue)
                Text(text = "${reportType.label} - ${java.text.SimpleDateFormat("dd/MM/yyyy").format(java.util.Date())}", fontSize = 11.sp, color = grayText)
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Spacer(modifier = Modifier.height(8.dp))

                // Filtered List for preview
                val previewList = when (reportType) {
                    ReportType.FULL_LIST -> animals
                    ReportType.AVAILABLE -> animals.filter { it.status.equals("Disponível", ignoreCase = true) }
                    else -> animals
                }.take(3)

                previewList.forEach { animal ->
                    PreviewItem(
                        animal.nome,
                        "${animal.especie} · ${animal.raca}",
                        "${animal.idade} · ${animal.status}",
                        if (animal.especie.contains("Gato", true)) "🐱" else "🐶",
                        if (animal.especie.contains("Gato", true)) Color(0xFFFFF9E3) else Color(0xFFE0F7F9)
                    )
                }

                if (animals.size > 3) {
                    Text(text = "...", color = Color.LightGray, modifier = Modifier.padding(vertical = 4.dp))
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Total: ${animals.size} animais",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = darkBlue,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PreviewItem(name: String, breed: String, details: String, emoji: String, bgColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(bgColor, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF0D1B3E))
            Text(text = breed, fontSize = 10.sp, color = Color(0xFF707B81))
            Text(text = details, fontSize = 10.sp, color = Color(0xFF707B81))
        }
    }
}

// Extension to simulate withAlpha for Color
fun Color.withAlpha(alpha: Float): Color = this.copy(alpha = alpha)
