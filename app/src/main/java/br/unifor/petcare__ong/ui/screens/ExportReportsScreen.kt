package br.unifor.petcare__ong.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportReportsScreen() {
    val darkBlue = Color(0xFF0D1B3E)
    val tealPrimary = Color(0xFF009688)
    val grayText = Color(0xFF707B81)
    val lightBackground = Color(0xFFF8F9FA)

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
                    IconButton(onClick = { }) {
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
            ExportBottomNavigation(tealPrimary)
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
            ReportTypeSection(darkBlue, tealPrimary, grayText)

            // Section 2: Período
            PeriodSection(darkBlue, grayText)

            // Section 3: Pré-visualização
            PreviewSection(darkBlue, grayText, tealPrimary)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ReportTypeSection(darkBlue: Color, tealColor: Color, grayText: Color) {
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
                ReportOption("Lista Completa de Animais", isSelected = true, tealColor, darkBlue)
                ReportOption("Animais Disponíveis", isSelected = false, tealColor, darkBlue)
                ReportOption("Histórico de Adoções", isSelected = false, tealColor, darkBlue)
                ReportOption("Resumo Mensal", isSelected = false, tealColor, darkBlue)
            }
        }
    }
}

@Composable
fun ReportOption(label: String, isSelected: Boolean, tealColor: Color, darkBlue: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) BorderStroke(1.5.dp, tealColor.copy(alpha = 0.5f)) else BorderStroke(1.dp, Color(0xFFEEEEEE)),
        color = if (isSelected) tealColor.withAlpha(0.05f) else Color.White
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(selectedColor = tealColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 14.sp, color = darkBlue, fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal)
        }
    }
}

@Composable
fun PeriodSection(darkBlue: Color, grayText: Color) {
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

            DateField("Data Inicial", grayText)
            Spacer(modifier = Modifier.height(16.dp))
            DateField("Data Final", grayText)
        }
    }
}

@Composable
fun DateField(label: String, grayText: Color) {
    Column {
        Text(text = label, fontSize = 12.sp, color = Color(0xFF0D1B3E), fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF8F9FA),
            border = BorderStroke(1.dp, Color(0xFFEEEEEE))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "dd/mm/aaaa", color = grayText, fontSize = 14.sp)
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = Color(0xFFEEEEEE),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun PreviewSection(darkBlue: Color, grayText: Color, tealColor: Color) {
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
                Text(text = "Relatório de Animais - 09/05/2026", fontSize = 11.sp, color = grayText)
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Spacer(modifier = Modifier.height(8.dp))

                // List Items
                PreviewItem("Rex", "Cachorro · Labrador", "2 anos · Disponível", "🐶", Color(0xFFE0F7F9))
                PreviewItem("Mia", "Gato · Siamês", "1 ano · Em tratamento", "🐱", Color(0xFFFFF9E3))
                PreviewItem("Bob", "Cachorro · Vira-lata", "3 anos · Disponível", "🐶", Color(0xFFE0F7F9))

                Text(text = "...", color = Color.LightGray, modifier = Modifier.padding(vertical = 4.dp))
                
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Total: 87 animais",
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

@Composable
fun ExportBottomNavigation(tealPrimary: Color) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Animais", fontSize = 10.sp) },
            selected = false,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color(0xFFB0B0B0),
                unselectedTextColor = Color(0xFFB0B0B0)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
            label = { Text("Dashboard", fontSize = 10.sp) },
            selected = false,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color(0xFFB0B0B0),
                unselectedTextColor = Color(0xFFB0B0B0)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.FileDownload, contentDescription = null) },
            label = { Text("Exportar", fontSize = 10.sp) },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = tealPrimary,
                selectedTextColor = tealPrimary,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AddCircleOutline, contentDescription = null) },
            label = { Text("Adicionar", fontSize = 10.sp) },
            selected = false,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color(0xFFB0B0B0),
                unselectedTextColor = Color(0xFFB0B0B0)
            )
        )
    }
}

// Extension to simulate withAlpha for Color
fun Color.withAlpha(alpha: Float): Color = this.copy(alpha = alpha)

@Preview(showBackground = true)
@Composable
fun ExportReportsScreenPreview() {
    ExportReportsScreen()
}
