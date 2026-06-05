package br.unifor.petcare__ong.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.unifor.petcare__ong.ui.components.AppBottomBar
import br.unifor.petcare__ong.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMovementScreen(navController: NavController) {
    val darkBlue = Color(0xFF0D1B3E)
    val tealPrimary = Color(0xFF009688)
    val backgroundGray = Color(0xFFF8F9FA)
    val inputBorderColor = Color(0xFFEEEEEE)
    val placeholderColor = Color(0xFFB0B0B0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nova Movimentação",
                        fontWeight = FontWeight.Bold,
                        color = darkBlue,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
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
        containerColor = backgroundGray
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Tipo de Movimentação
                    MovementDropdownField("Tipo de Movimentação", "Adoção", darkBlue, inputBorderColor)

                    // Data
                    MovementInputField("Data", "dd/mm/aaaa", darkBlue, placeholderColor, inputBorderColor, Icons.Default.CalendarToday)

                    // Hora de Início
                    MovementInputField("Hora de Início", "--:--", darkBlue, placeholderColor, inputBorderColor, Icons.Default.AccessTime)

                    // Status
                    MovementDropdownField("Status", "Agendado", darkBlue, inputBorderColor)

                    // Responsável
                    MovementInputField("Responsável", "Nome do responsável", darkBlue, placeholderColor, inputBorderColor)

                    // Data e Hora de Finalização
                    MovementInputField("Data e Hora de Finalização", "dd/mm/aaaa --:--", darkBlue, placeholderColor, inputBorderColor, Icons.Default.CalendarMonth)

                    // Anotações
                    Column {
                        Text(
                            text = "Anotações",
                            fontWeight = FontWeight.Bold,
                            color = darkBlue,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, inputBorderColor)
                        ) {
                            Box(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Informações sobre o evento de adoção",
                                    color = placeholderColor,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F3F4))
                        ) {
                            Text("Cancelar", color = darkBlue, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = tealPrimary)
                        ) {
                            Text("Salvar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovementInputField(
    label: String,
    placeholder: String,
    labelColor: Color,
    placeholderColor: Color,
    borderColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = labelColor,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = placeholder, color = placeholderColor, fontSize = 14.sp)
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFFF1F3F4),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MovementDropdownField(
    label: String,
    value: String,
    labelColor: Color,
    borderColor: Color
) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = labelColor,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = value, color = labelColor, fontSize = 14.sp)
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

