package br.unifor.petcare__ong.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.unifor.petcare__ong.ui.components.AppBottomBar
import br.unifor.petcare__ong.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val darkBlue = Color(0xFF0D1B3E)
    val tealPrimary = Color(0xFF009688)
    val grayText = Color(0xFF707B81)
    val backgroundGray = Color(0xFFF8F9FA)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Painel Gerencial",
                        fontWeight = FontWeight.Bold,
                        color = darkBlue,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = Routes.Dashboard.route
            )
        },
        containerColor = backgroundGray
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Main Total Card
            MainTotalCard(tealPrimary)

            // Two Column Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = "42",
                    label = "DISPONÍVEIS",
                    icon = Icons.Default.CheckCircleOutline,
                    iconTint = Color(0xFF5C6BC0),
                    iconBg = Color(0xFFE8EAF6)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    value = "28",
                    label = "EM TRATAMENTO",
                    icon = Icons.Default.Timeline,
                    iconTint = Color(0xFFFFA726),
                    iconBg = Color(0xFFFFF3E0)
                )
            }

            // Adoptions and Entries Card
            AdoptionsCard(darkBlue, grayText)

            // Population by Species Card
            PopulationCard(darkBlue, grayText, tealPrimary)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MainTotalCard(tealColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = tealColor)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "87",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Total de Animais na ONG",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color
) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = RoundedCornerShape(8.dp),
                color = iconBg
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Column {
                Text(
                    text = value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D1B3E)
                )
                Text(
                    text = label,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF707B81)
                )
            }
        }
    }
}

@Composable
fun AdoptionsCard(darkBlue: Color, grayText: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = Color(0xFF00BFA5),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Adoções e Entradas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = darkBlue
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            
            MetricRow("Entradas (Maio)", "+12", Color(0xFF00BFA5))
            MetricRow("Adoções (Maio)", "+8", Color(0xFF5C6BC0))
            MetricRow("Óbitos (Maio)", "-1", Color(0xFFE57373))
        }
    }
}

@Composable
fun MetricRow(label: String, value: String, valueColor: Color) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        color = Color(0xFFF8F9FA),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, color = Color(0xFF707B81), fontSize = 14.sp)
            Text(
                text = value,
                color = valueColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .background(valueColor.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun PopulationCard(darkBlue: Color, grayText: Color, tealColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "População por Espécie",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = darkBlue
            )
            Spacer(modifier = Modifier.height(20.dp))
            
            SpeciesProgress("🐶 Cachorros", "54", 0.65f, tealColor)
            Spacer(modifier = Modifier.height(16.dp))
            SpeciesProgress("🐱 Gatos", "33", 0.35f, Color(0xFFFFA000))
        }
    }
}

@Composable
fun SpeciesProgress(label: String, count: String, progress: Float, color: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF0D1B3E))
            Text(text = count, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D1B3E))
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = color,
            trackColor = Color(0xFFECEFF1),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}
