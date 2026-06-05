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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecordScreen(navController: NavController) {
    val darkBlue = Color(0xFF0D1B3E)
    val tealPrimary = Color(0xFF009688)
    val backgroundGray = Color(0xFFF8F9FA)
    
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Vacinas", "Consultas", "Tratamentos", "Observações")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Novo Registro",
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
        bottomBar = {

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
                            0 -> VacinasForm()
                            1 -> ConsultasForm()
                            2 -> TratamentosForm()
                            3 -> ObservacoesForm()
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Ações comuns a todas as abas
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
}

@Composable
fun VacinasForm() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormFieldItem("Nome da Vacina", "Ex: Antirrábica")
        FormFieldItem("Data de Aplicação", "dd/mm/aaaa", isDate = true)
        FormFieldItem("Lote", "Número do lote")
        FormFieldItem("Próxima Dose", "dd/mm/aaaa", isDate = true)
        LargeFormFieldItem("Observações", "Informações adicionais")
    }
}

@Composable
fun ConsultasForm() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormFieldItem("Data da Consulta", "dd/mm/aaaa", isDate = true)
        FormFieldItem("Veterinário", "Nome do veterinário")
        FormFieldItem("Motivo", "Motivo da consulta")
        LargeFormFieldItem("Diagnóstico", "Diagnóstico e observações")
        LargeFormFieldItem("Prescrição", "Medicamentos prescritos")
    }
}

@Composable
fun TratamentosForm() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormFieldItem("Tipo de Tratamento", "Ex: Vermífugo")
        FormFieldItem("Data de Início", "dd/mm/aaaa", isDate = true)
        FormFieldItem("Data de Término", "dd/mm/aaaa", isDate = true)
        FormFieldItem("Medicamento", "Nome do medicamento")
        FormFieldItem("Dosagem", "Ex: 1 comp. 2x ao dia")
        LargeFormFieldItem("Observações", "Informações adicionais")
    }
}

@Composable
fun ObservacoesForm() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormFieldItem("Data", "dd/mm/aaaa", isDate = true)
        FormFieldItem("Título", "Título da observação")
        LargeFormFieldItem("Descrição", "Descreva a observação...")
    }
}

@Composable
fun FormFieldItem(label: String, placeholder: String, isDate: Boolean = false) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D1B3E),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = placeholder, color = Color.LightGray, fontSize = 14.sp)
                if (isDate) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFFF1F3F4).copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LargeFormFieldItem(label: String, placeholder: String) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D1B3E),
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE))
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text(text = placeholder, color = Color.LightGray, fontSize = 14.sp)
            }
        }
    }
}



