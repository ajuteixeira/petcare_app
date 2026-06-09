package br.unifor.petcare__ong.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.unifor.petcare__ong.model.MedicalRecord
import br.unifor.petcare__ong.ui.viewmodel.MedicalRecordViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecordScreen(
    navController: NavController,
    animalId: String,
    recordId: String? = null,
    viewModel: MedicalRecordViewModel = viewModel()
) {
    val darkBlue = Color(0xFF0D1B3E)
    val tealPrimary = Color(0xFF009688)
    val backgroundGray = Color(0xFFF8F9FA)

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Vacinas", "Consultas", "Tratamentos", "Observações")
    val types = listOf("VACINA", "CONSULTA", "TRATAMENTO", "OBSERVAÇÃO")

    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var professional by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var prescription by remember { mutableStateOf("") }
    var nextDose by remember { mutableStateOf("") }
    var lot by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    LaunchedEffect(recordId) {
        if (!recordId.isNullOrEmpty()) {
            viewModel.fetchRecordById(animalId, recordId) { record ->
                record?.let {
                    selectedTabIndex = types.indexOf(it.type).coerceAtLeast(0)
                    title = it.title
                    date = it.date
                    professional = it.professional
                    description = it.description
                    prescription = it.prescription
                    nextDose = it.nextDose
                    lot = it.lot
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (recordId.isNullOrEmpty()) "Novo Registro" else "Editar Registro",
                        fontWeight = FontWeight.Bold,
                        color = darkBlue,
                        fontSize = 18.sp
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
        containerColor = backgroundGray
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = tealPrimary,
                edgePadding = 16.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = tealPrimary
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, tabTitle ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = tabTitle,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) tealPrimary else Color.Gray
                            )
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                        when (selectedTabIndex) {
                            0 -> { // Vacinas
                                FormField("Nome da Vacina", title, { title = it }, "Ex: Antirrábica")
                                FormField("Data de Aplicação", date, { }, "dd/mm/aaaa", isDate = true, onClick = { showDatePicker { date = it } })
                                FormField("Lote", lot, { lot = it }, "Número do lote")
                                FormField("Próxima Dose", nextDose, { }, "dd/mm/aaaa", isDate = true, onClick = { showDatePicker { nextDose = it } })
                                LargeFormField("Observações", description, { description = it }, "Informações adicionais")
                            }
                            1 -> { // Consultas
                                FormField("Data da Consulta", date, { }, "dd/mm/aaaa", isDate = true, onClick = { showDatePicker { date = it } })
                                FormField("Veterinário", professional, { professional = it }, "Nome do veterinário")
                                FormField("Motivo", title, { title = it }, "Motivo da consulta")
                                LargeFormField("Diagnóstico", description, { description = it }, "Diagnóstico e observações")
                                LargeFormField("Prescrição", prescription, { prescription = it }, "Medicamentos prescritos")
                            }
                            2 -> { // Tratamentos
                                FormField("Tipo de Tratamento", title, { title = it }, "Ex: Vermífugo")
                                FormField("Data de Início", date, { }, "dd/mm/aaaa", isDate = true, onClick = { showDatePicker { date = it } })
                                FormField("Data de Término", nextDose, { }, "dd/mm/aaaa", isDate = true, onClick = { showDatePicker { nextDose = it } })
                                FormField("Medicamento", prescription, { prescription = it }, "Nome do medicamento")
                                FormField("Dosagem", lot, { lot = it }, "Ex: 1 comp. 2x ao dia")
                                LargeFormField("Observações", description, { description = it }, "Informações adicionais")
                            }
                            3 -> { // Observações
                                FormField("Data", date, { }, "dd/mm/aaaa", isDate = true, onClick = { showDatePicker { date = it } })
                                FormField("Título", title, { title = it }, "Título da observação")
                                LargeFormField("Descrição", description, { description = it }, "Descreva a observação...")
                            }
                        }

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
                                    val newRecord = MedicalRecord(
                                        id = recordId ?: "",
                                        type = types[selectedTabIndex],
                                        title = title,
                                        date = date,
                                        professional = professional,
                                        description = description,
                                        prescription = prescription,
                                        nextDose = nextDose,
                                        lot = lot
                                    )
                                    viewModel.saveRecord(animalId, newRecord, {
                                        navController.navigateUp()
                                    }, { })
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = tealPrimary),
                                enabled = date.isNotEmpty() && title.isNotEmpty()
                            ) {
                                Text(
                                    if (recordId.isNullOrEmpty()) "Salvar" else "Atualizar",
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
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isDate: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Column {
        Text(text = label, fontWeight = FontWeight.Bold, color = Color(0xFF0D1B3E), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick?.invoke() },
            placeholder = { Text(placeholder, fontSize = 14.sp) },
            shape = RoundedCornerShape(12.dp),
            readOnly = isDate,
            enabled = !isDate,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledBorderColor = Color(0xFFEEEEEE),
                disabledLabelColor = Color.Gray,
                disabledPlaceholderColor = Color.LightGray
            ),
            trailingIcon = {
                if (isDate) {
                    IconButton(onClick = { onClick?.invoke() }) {
                        Icon(Icons.Default.CalendarToday, null, modifier = Modifier.size(20.dp))
                    }
                }
            }
        )
    }
}

@Composable
fun LargeFormField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String) {
    Column {
        Text(text = label, fontWeight = FontWeight.Bold, color = Color(0xFF0D1B3E), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text(placeholder, fontSize = 14.sp) },
            shape = RoundedCornerShape(12.dp),
            maxLines = 5
        )
    }
}
