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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.unifor.petcare__ong.data.repository.AnimalRepository
import br.unifor.petcare__ong.model.MedicalRecord

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecordScreen(navController: NavController, animalId: String, recordId: String? = null) {
    val darkBlue = Color(0xFF0D1B3E)
    val tealPrimary = Color(0xFF009688)
    val backgroundGray = Color(0xFFF8F9FA)
    
    val repository = remember { AnimalRepository() }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Vacinas", "Consultas", "Tratamentos", "Observações")

    // Form states
    var titulo by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var veterinario by remember { mutableStateOf("") }
    var lote by remember { mutableStateOf("") }
    var proximaDose by remember { mutableStateOf("") }
    var diagnostico by remember { mutableStateOf("") }
    var prescricao by remember { mutableStateOf("") }
    var observacoes by remember { mutableStateOf("") }

    LaunchedEffect(recordId) {
        if (recordId != null) {
            repository.buscarRegistrosMedicos(animalId) { records ->
                val record = records.find { it.id == recordId }
                if (record != null) {
                    titulo = record.titulo
                    data = record.data
                    hora = record.hora
                    veterinario = record.veterinario
                    lote = record.lote
                    proximaDose = record.proximaDose
                    diagnostico = record.diagnostico
                    prescricao = record.prescricao
                    observacoes = record.observacoes
                    selectedTabIndex = when(record.tipo) {
                        "VACINA" -> 0
                        "CONSULTA" -> 1
                        "TRATAMENTO" -> 2
                        else -> 3
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (recordId == null) "Novo Registro" else "Editar Registro",
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
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) tealPrimary else Color.Gray,
                                maxLines = 1,
                                softWrap = false
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
                                titulo, { titulo = it },
                                data, { data = it },
                                lote, { lote = it },
                                proximaDose, { proximaDose = it },
                                observacoes, { observacoes = it }
                            )
                            1 -> ConsultasForm(
                                data, { data = it },
                                hora, { hora = it },
                                veterinario, { veterinario = it },
                                titulo, { titulo = it }, // Motivo
                                diagnostico, { diagnostico = it },
                                prescricao, { prescricao = it }
                            )
                            2 -> TratamentosForm(
                                titulo, { titulo = it }, // Tipo
                                data, { data = it }, // Início
                                proximaDose, { proximaDose = it }, // Término
                                prescricao, { prescricao = it }, // Medicamento
                                lote, { lote = it }, // Dosagem
                                observacoes, { observacoes = it }
                            )
                            3 -> ObservacoesForm(
                                data, { data = it },
                                titulo, { titulo = it },
                                observacoes, { observacoes = it }
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
                                    val tipo = when(selectedTabIndex) {
                                        0 -> "VACINA"
                                        1 -> "CONSULTA"
                                        2 -> "TRATAMENTO"
                                        else -> "OBSERVAÇÃO"
                                    }
                                    val record = MedicalRecord(
                                        id = recordId ?: "",
                                        animalId = animalId,
                                        tipo = tipo,
                                        titulo = titulo,
                                        data = data,
                                        hora = hora,
                                        veterinario = veterinario,
                                        lote = lote,
                                        proximaDose = proximaDose,
                                        diagnostico = diagnostico,
                                        prescricao = prescricao,
                                        observacoes = observacoes
                                    )
                                    if (recordId == null) {
                                        repository.adicionarRegistroMedico(animalId, record, {
                                            navController.navigateUp()
                                        }, {})
                                    } else {
                                        repository.atualizarRegistroMedico(animalId, record, {
                                            navController.navigateUp()
                                        }, {})
                                    }
                                },
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
}

@Composable
fun VacinasForm(
    titulo: String, onTituloChange: (String) -> Unit,
    data: String, onDataChange: (String) -> Unit,
    lote: String, onLoteChange: (String) -> Unit,
    proximaDose: String, onProximaDoseChange: (String) -> Unit,
    observacoes: String, onObservacoesChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormFieldItem("Nome da Vacina", "Ex: Antirrábica", titulo, onTituloChange)
        FormFieldItem("Data de Aplicação", "dd/mm/aaaa", data, onDataChange, isDate = true)
        FormFieldItem("Lote", "Número do lote", lote, onLoteChange)
        FormFieldItem("Próxima Dose", "dd/mm/aaaa", proximaDose, onProximaDoseChange, isDate = true)
        LargeFormFieldItem("Observações", "Informações adicionais", observacoes, onObservacoesChange)
    }
}

@Composable
fun ConsultasForm(
    data: String, onDataChange: (String) -> Unit,
    hora: String, onHoraChange: (String) -> Unit,
    veterinario: String, onVeterinarioChange: (String) -> Unit,
    motivo: String, onMotivoChange: (String) -> Unit,
    diagnostico: String, onDiagnosticoChange: (String) -> Unit,
    prescricao: String, onPrescricaoChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormFieldItem("Data da Consulta", "dd/mm/aaaa", data, onDataChange, isDate = true)
        FormFieldItem("Horário", "HH:mm", hora, onHoraChange)
        FormFieldItem("Veterinário", "Nome do veterinário", veterinario, onVeterinarioChange)
        FormFieldItem("Motivo", "Motivo da consulta", motivo, onMotivoChange)
        LargeFormFieldItem("Diagnóstico", "Diagnóstico e observações", diagnostico, onDiagnosticoChange)
        LargeFormFieldItem("Prescrição", "Medicamentos prescritos", prescricao, onPrescricaoChange)
    }
}

@Composable
fun TratamentosForm(
    tipo: String, onTipoChange: (String) -> Unit,
    dataInicio: String, onDataInicioChange: (String) -> Unit,
    dataTermino: String, onDataTerminoChange: (String) -> Unit,
    medicamento: String, onMedicamentoChange: (String) -> Unit,
    dosagem: String, onDosagemChange: (String) -> Unit,
    observacoes: String, onObservacoesChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormFieldItem("Tipo de Tratamento", "Ex: Vermífugo", tipo, onTipoChange)
        FormFieldItem("Data de Início", "dd/mm/aaaa", dataInicio, onDataInicioChange, isDate = true)
        FormFieldItem("Data de Término", "dd/mm/aaaa", dataTermino, onDataTerminoChange, isDate = true)
        FormFieldItem("Medicamento", "Nome do medicamento", medicamento, onMedicamentoChange)
        FormFieldItem("Dosagem", "Ex: 1 comp. 2x ao dia", dosagem, onDosagemChange)
        LargeFormFieldItem("Observações", "Informações adicionais", observacoes, onObservacoesChange)
    }
}

@Composable
fun ObservacoesForm(
    data: String, onDataChange: (String) -> Unit,
    titulo: String, onTituloChange: (String) -> Unit,
    descricao: String, onDescricaoChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormFieldItem("Data", "dd/mm/aaaa", data, onDataChange, isDate = true)
        FormFieldItem("Título", "Título da observação", titulo, onTituloChange)
        LargeFormFieldItem("Descrição", "Descreva a observação...", descricao, onDescricaoChange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormFieldItem(label: String, placeholder: String, value: String, onValueChange: (String) -> Unit, isDate: Boolean = false) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D1B3E),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = placeholder, color = Color.LightGray, fontSize = 14.sp) },
            trailingIcon = {
                if (isDate) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color.Gray.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF009688),
                unfocusedBorderColor = Color(0xFFEEEEEE)
            ),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeFormFieldItem(label: String, placeholder: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D1B3E),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            placeholder = { Text(text = placeholder, color = Color.LightGray, fontSize = 14.sp) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF009688),
                unfocusedBorderColor = Color(0xFFEEEEEE)
            )
        )
    }
}