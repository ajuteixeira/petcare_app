package br.unifor.petcare__ong.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.unifor.petcare__ong.model.Movement
import br.unifor.petcare__ong.ui.navigation.Routes
import br.unifor.petcare__ong.ui.viewmodel.MovementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementHistoryScreen(
    navController: NavController,
    animalId: String,
    viewModel: MovementViewModel = viewModel()
) {
    val darkBlue = Color(0xFF0D1B3E)
    val tealPrimary = Color(0xFF009688)
    val grayText = Color(0xFF707B81)
    val backgroundGray = Color(0xFFF8F9FA)

    val movements by viewModel.movements
    val animal by viewModel.animal
    val isLoading by viewModel.isLoading

    LaunchedEffect(animalId) {
        viewModel.loadData(animalId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Histórico de Movimentações",
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
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = tealPrimary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Header Info Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(tealPrimary, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = animal?.nome ?: "Carregando...",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = darkBlue
                                )
                                Text(
                                    text = "${animal?.especie ?: ""} • ${animal?.raca ?: ""} • ${animal?.idade ?: ""}",
                                    fontSize = 14.sp,
                                    color = grayText
                                )
                            }
                        }
                    }
                }

                // Movements List
                items(movements) { movement ->
                    MovementItemCard(
                        movement = movement,
                        titleColor = darkBlue,
                        subtitleColor = grayText,
                        onEdit = {
                            navController.navigate(Routes.EditMovement.createRoute(animalId, movement.id))
                        },
                        onDelete = {
                            viewModel.deleteMovement(animalId, movement.id)
                        }
                    )
                }

                // Add Button
                item {
                    Button(
                        onClick = {
                            navController.navigate(Routes.NewMovement.createRoute(animalId))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = tealPrimary)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Adicionar Movimentação",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovementItemCard(
    movement: Movement,
    titleColor: Color,
    subtitleColor: Color,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val statusColors = getStatusColors(movement.status)
    
    val entradaOptions = listOf("Resgate", "Abandono", "Devolução", "Transferência")
    val isEntrada = entradaOptions.contains(movement.type)
    val category = if (isEntrada) "ENTRADA" else "SAÍDA"
    val categoryColor = if (isEntrada) Color(0xFF4CAF50) else Color(0xFFF44336)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(
                            text = movement.type,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = titleColor
                        )
                        Text(
                            text = category,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = categoryColor
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        color = statusColors.second,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = movement.status.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = statusColors.first
                        )
                    }
                }

                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Início: ${movement.startDateTime}",
                fontSize = 12.sp,
                color = Color.LightGray,
                fontWeight = FontWeight.Medium
            )

            if (movement.endDateTime.isNotEmpty()) {
                Text(
                    text = "Fim: ${movement.endDateTime}",
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = movement.notes,
                fontSize = 14.sp,
                color = subtitleColor,
                lineHeight = 20.sp
            )

            if (movement.responsible.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Responsável: ${movement.responsible}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun getStatusColors(status: String): Pair<Color, Color> {
    return when (status.uppercase()) {
        "CONCLUÍDO", "CONCLUIDO" -> Color(0xFF00BFA5) to Color(0xFFE0F2F1)
        "EM PROGRESSO" -> Color(0xFF5C6BC0) to Color(0xFFE8EAF6)
        "AGENDADO" -> Color(0xFFFFA726) to Color(0xFFFFF3E0)
        "CANCELADO" -> Color(0xFFEF5350) to Color(0xFFFFEBEE)
        else -> Color.Gray to Color(0xFFF1F3F4)
    }
}
