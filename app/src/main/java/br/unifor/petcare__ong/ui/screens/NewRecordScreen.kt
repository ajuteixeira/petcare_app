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

    val isEditing = recordId != null
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("VACINA", "CONSULTA", "TRATAMENTO", "OBSERVAÇÃO")

    // Form State
    var date by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var batch by remember { mutableStateOf("") }
    var nextDose by remember { mutableStateOf("") }
    var veterinarian by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var diagnosis by remember { mutableStateOf("") }
    var prescription by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var medication by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }

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
        if (isEditing && recordId != null) {
            viewModel.fetchRecordById(animalId, recordId) { record ->
                record?.let {
                    date = it.date
                    title = it.title
                    description = it.description
                    batch = it.batch ?: ""
                    nextDose = it.nextDose ?: ""
                    veterinarian = it.veterinarian ?: ""
                    reason = it.reason ?: ""
                    diagnosis = it.diagnosis ?: ""
                    prescription = it.prescription ?: ""
                    endDate = it.endDate ?: ""
                    medication = it.medication ?: ""
                    dosage = it.dosage ?: ""
                    selectedTabIndex = tabs.indexOf(it.type).coerceAtLeast(0)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "Editar Registro" else "Novo Registro",
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
                tabs.forEachIndexed { index, titleText ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { if (!isEditing) selectedTabIndex = index },
                        text = {
                            Text(
                                text = titleText,
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
                            0 -> VacinasForm(
                                title, { title = it },
                                date, { showDatePicker { date = it } },
                                batch, { batch = it },
                                nextDose, { showDatePicker { nextDose = it } },
                                description, { description = it }
                            )
                            1 -> ConsultasForm(
                                date, { showDatePicker { date = it } },
                                veterinarian, { veterinarian = it },
                                reason, { reason = it },
                                diagnosis, { diagnosis = it },
                                prescription, { prescription = it }
                            )
                            2 -> TratamentosForm(
                                title, { title = it },
                                date, { showDatePicker { date = it } },
                                endDate, { showDatePicker { endDate = it } },
                                medication, { medication = it },
                                dosage, { dosage = it },
                                description, { description = it }
                            )
                            3 -> ObservacoesForm(
                                date, { showDatePicker { date = it } },
                                title, { title = it },
                                description, { description = it }
                            )
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
                                    val finalTitle = if (selectedTabIndex == 1) "Consulta com $veterinarian" else title
                                    val record = MedicalRecord(
                                        id = recordId ?: "",
                                        animalId = animalId,
                                        type = tabs[selectedTabIndex],
                                        date = date,
                                        title = finalTitle,
                                        description = description,
                                        batch = batch,
                                        nextDose = nextDose,
                                        veterinarian = veterinarian,
                                        reason = reason,
                                        diagnosis = diagnosis,
                                        prescription = prescription,
                                        endDate = endDate,
                                        medication = medication,
                                        dosage = dosage
                                    )
                                    viewModel.saveRecord(animalId, record, {
                                        navController.navigateUp()
                                    }, {
                                        // Handle error
                                    })
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = tealPrimary),
                                enabled = date.isNotEmpty() && (title.isNotEmpty() || (selectedTabIndex == 1 && veterinarian.isNotEmpty()))
                            ) {
                                Text(
                                    if (!isEditing) "Salvar" else "Atualizar",
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
fun VacinasForm(
    name: String, onNameChange: (String) -> Unit,
    date: String, onDateClick: () -> Unit,
    batch: String, onBatchChange: (String) -> Unit,
    nextDose: String, onNextDoseClick: () -> Unit,
    obs: String, onObsChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormFieldItem("Nome da Vacina", "Ex: Antirrábica", name, onNameChange)
        FormFieldItem("Data de Aplicação", "dd/mm/aaaa", date, {}, isDate = true, onClick = onDateClick)
        FormFieldItem("Lote", "Número do lote", batch, onBatchChange)
        FormFieldItem("Próxima Dose", "dd/mm/aaaa", nextDose, {}, isDate = true, onClick = onNextDoseClick)
        LargeFormFieldItem("Observações", "Informações adicionais", obs, onObsChange)
    }
}

@Composable
fun ConsultasForm(
    date: String, onDateClick: () -> Unit,
    vet: String, onVetChange: (String) -> Unit,
    reason: String, onReasonChange: (String) -> Unit,
    diag: String, onDiagChange: (String) -> Unit,
    presc: String, onPrescChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormFieldItem("Data da Consulta", "dd/mm/aaaa", date, {}, isDate = true, onClick = onDateClick)
        FormFieldItem("Veterinário", "Nome do veterinário", vet, onVetChange)
        FormFieldItem("Motivo", "Motivo da consulta", reason, onReasonChange)
        LargeFormFieldItem("Diagnóstico", "Diagnóstico e observações", diag, onDiagChange)
        LargeFormFieldItem("Prescrição", "Medicamentos prescritos", presc, onPrescChange)
    }
}

@Composable
fun TratamentosForm(
    type: String, onTypeChange: (String) -> Unit,
    startDate: String, onStartDateClick: () -> Unit,
    endDate: String, onEndDateClick: () -> Unit,
    med: String, onMedChange: (String) -> Unit,
    dosage: String, onDosageChange: (String) -> Unit,
    obs: String, onObsChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormFieldItem("Tipo de Tratamento", "Ex: Vermífugo", type, onTypeChange)
        FormFieldItem("Data de Início", "dd/mm/aaaa", startDate, {}, isDate = true, onClick = onStartDateClick)
        FormFieldItem("Data de Término", "dd/mm/aaaa", endDate, {}, isDate = true, onClick = onEndDateClick)
        FormFieldItem("Medicamento", "Nome do medicamento", med, onMedChange)
        FormFieldItem("Dosagem", "Ex: 1 comp. 2x ao dia", dosage, onDosageChange)
        LargeFormFieldItem("Observações", "Informações adicionais", obs, onObsChange)
    }
}

@Composable
fun ObservacoesForm(
    date: String, onDateClick: () -> Unit,
    title: String, onTitleChange: (String) -> Unit,
    desc: String, onDescChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormFieldItem("Data", "dd/mm/aaaa", date, {}, isDate = true, onClick = onDateClick)
        FormFieldItem("Título", "Título da observação", title, onTitleChange)
        LargeFormFieldItem("Descrição", "Descreva a observação...", desc, onDescChange)
    }
}

@Composable
fun FormFieldItem(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
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
                .then(if (isDate) Modifier.clickable { onClick?.invoke() } else Modifier),
            placeholder = { Text(text = placeholder, color = Color.LightGray, fontSize = 14.sp) },
            shape = RoundedCornerShape(12.dp),
            readOnly = isDate,
            enabled = !isDate,
            trailingIcon = {
                if (isDate) {
                    IconButton(onClick = { onClick?.invoke() }) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color.Gray.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF009688),
                unfocusedBorderColor = Color(0xFFEEEEEE),
                disabledTextColor = Color.Black,
                disabledBorderColor = Color(0xFFEEEEEE),
                disabledPlaceholderColor = Color.LightGray
            )
        )
    }
}

@Composable
fun LargeFormFieldItem(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(text = label, fontWeight = FontWeight.Bold, color = Color(0xFF0D1B3E), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text(text = placeholder, color = Color.LightGray, fontSize = 14.sp) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF009688),
                unfocusedBorderColor = Color(0xFFEEEEEE)
            )
        )
    }
}
