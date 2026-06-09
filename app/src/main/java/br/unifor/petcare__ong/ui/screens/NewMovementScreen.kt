package br.unifor.petcare__ong.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.unifor.petcare__ong.model.Movement
import br.unifor.petcare__ong.ui.viewmodel.MovementViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMovementScreen(
    navController: NavController,
    animalId: String,
    movementId: String? = null,
    viewModel: MovementViewModel = viewModel()
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val darkBlue = Color(0xFF0D1B3E)
    val tealPrimary = Color(0xFF009688)
    val backgroundGray = Color(0xFFF8F9FA)

    val entradaOptions = listOf("Resgate", "Abandono", "Devolução", "Transferência", "Outro")
    val saidaOptions = listOf("Evento de adoção", "Cirurgia", "Consulta", "Lar temporário", "Passeio", "Óbito", "Outro")

    var category by remember { mutableStateOf("Entrada") }
    var type by remember { mutableStateOf("") }
    var startDateTime by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Agendado") }
    var responsible by remember { mutableStateOf("") }
    var endDateTime by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    LaunchedEffect(movementId) {
        if (!movementId.isNullOrEmpty()) {
            viewModel.fetchMovementById(animalId, movementId) { movement ->
                movement?.let {
                    if (entradaOptions.contains(it.type)) {
                        category = "Entrada"
                    } else if (saidaOptions.contains(it.type)) {
                        category = "Saída"
                    }
                    type = it.type
                    startDateTime = it.startDateTime
                    status = it.status
                    responsible = it.responsible
                    endDateTime = it.endDateTime
                    notes = it.notes
                }
            }
        }
    }

    var categoryExpanded by remember { mutableStateOf(false) }
    val categoryOptions = listOf("Entrada", "Saída")

    var typeExpanded by remember { mutableStateOf(false) }
    val typeOptions = if (category == "Entrada") entradaOptions else saidaOptions

    var statusExpanded by remember { mutableStateOf(false) }
    val statusOptions = listOf("Agendado", "Em progresso", "Concluído")

    // Date and Time Picker for 'startDateTime'
    val startDateTimePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    startDateTime = "$selectedDate " + String.format("%02d:%02d", hour, minute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Date and Time Picker for 'endDateTime'
    val endDateTimePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    endDateTime = "$selectedDate " + String.format("%02d:%02d", hour, minute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (movementId.isNullOrEmpty()) "Nova Movimentação" else "Editar Movimentação",
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
                    // Categoria Dropdown
                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoria") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            categoryOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        if (category != option) {
                                            category = option
                                            type = "" // Clear type when category changes
                                        }
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Tipo de Movimentação Dropdown
                    ExposedDropdownMenuBox(
                        expanded = typeExpanded,
                        onExpandedChange = { typeExpanded = !typeExpanded }
                    ) {
                        OutlinedTextField(
                            value = type,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de Movimentação") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = { typeExpanded = false }
                        ) {
                            typeOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        type = option
                                        typeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Data e Hora de Início Input with Native Picker
                    OutlinedTextField(
                        value = startDateTime,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Data e Hora de Início") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { startDateTimePickerDialog.show() },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray,
                            disabledLabelColor = Color.Gray,
                            disabledTrailingIconColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = { 
                            IconButton(onClick = { startDateTimePickerDialog.show() }) {
                                Icon(Icons.Default.CalendarToday, null)
                            }
                        }
                    )

                    // Status Dropdown
                    ExposedDropdownMenuBox(
                        expanded = statusExpanded,
                        onExpandedChange = { statusExpanded = !statusExpanded }
                    ) {
                        OutlinedTextField(
                            value = status,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Status") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = statusExpanded,
                            onDismissRequest = { statusExpanded = false }
                        ) {
                            statusOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        status = option
                                        statusExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = responsible,
                        onValueChange = { responsible = it },
                        label = { Text("Responsável") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Data e Hora de Finalização with Native Picker
                    OutlinedTextField(
                        value = endDateTime,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Data e Hora de Finalização") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { endDateTimePickerDialog.show() },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Gray,
                            disabledLabelColor = Color.Gray,
                            disabledTrailingIconColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = { 
                            IconButton(onClick = { endDateTimePickerDialog.show() }) {
                                Icon(Icons.Default.CalendarMonth, null)
                            }
                        }
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Anotações") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F3F4))
                        ) {
                            Text("Cancelar", color = darkBlue, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = {
                                val updatedMovement = Movement(
                                    id = movementId ?: "",
                                    type = type,
                                    startDateTime = startDateTime,
                                    status = status,
                                    responsible = responsible,
                                    endDateTime = endDateTime,
                                    notes = notes
                                )
                                viewModel.saveMovement(
                                    animalId = animalId,
                                    movement = updatedMovement,
                                    onSuccess = { navController.navigateUp() },
                                    onFailure = { /* Handle error */ }
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = tealPrimary),
                            enabled = type.isNotEmpty() && startDateTime.isNotEmpty()
                        ) {
                            Text(
                                text = if (movementId.isNullOrEmpty()) "Salvar" else "Atualizar",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
