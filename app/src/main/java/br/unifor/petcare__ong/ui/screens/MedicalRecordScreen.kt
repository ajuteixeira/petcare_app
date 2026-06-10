package br.unifor.petcare__ong.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.unifor.petcare__ong.model.MedicalRecord
import br.unifor.petcare__ong.ui.navigation.Routes
import br.unifor.petcare__ong.ui.viewmodel.MedicalRecordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalRecordScreen(
    navController: NavController,
    animalId: String,
    viewModel: MedicalRecordViewModel = viewModel()
) {
    val darkBlue = Color(0xFF0D1B3E)
    val tealPrimary = Color(0xFF009688)
    val grayText = Color(0xFF707B81)
    val backgroundGray = Color(0xFFF8F9FA)

    val records by viewModel.records
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
                        text = "Prontuário Médico",
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
                // Header Card
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

                // Records List
                items(records) { record ->
                    RecordItem(
                        record = record,
                        titleColor = darkBlue,
                        subtitleColor = grayText,
                        onEdit = {
                            navController.navigate(Routes.EditRecord.createRoute(animalId, record.id))
                        },
                        onDelete = {
                            viewModel.deleteRecord(animalId, record.id) {
                                // Deletado com sucesso
                            }
                        }
                    )
                }

                // Add Button
                item {
                    Button(
                        onClick = {
                            navController.navigate(Routes.NewRecord.createRoute(animalId))
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
                                text = "Adicionar Registro",
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
fun RecordItem(
    record: MedicalRecord,
    titleColor: Color,
    subtitleColor: Color,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val typeColor = when (record.type) {
        "VACINA" -> Color(0xFF5C6BC0)
        "CONSULTA" -> Color(0xFF00BFA5)
        "TRATAMENTO" -> Color(0xFFFFA726)
        else -> Color(0xFF78909C)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(typeColor)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = typeColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = record.type,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = typeColor
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = record.date,
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(onClick = onEdit, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Editar",
                                tint = Color.LightGray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Excluir",
                                tint = Color.LightGray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = record.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )

                // Detalhes específicos por tipo
                when (record.type) {
                    "VACINA" -> {
                        if (!record.batch.isNullOrEmpty()) {
                            RecordDetail(Icons.Default.ConfirmationNumber, "Lote: ${record.batch}", subtitleColor)
                        }
                        if (!record.nextDose.isNullOrEmpty()) {
                            RecordDetail(Icons.Default.Event, "Próxima Dose: ${record.nextDose}", Color(0xFF009688))
                        }
                    }
                    "CONSULTA" -> {
                        if (!record.veterinarian.isNullOrEmpty()) {
                            RecordDetail(Icons.Default.Person, "Veterinário: ${record.veterinarian}", subtitleColor)
                        }
                        if (!record.reason.isNullOrEmpty()) {
                            RecordDetail(Icons.Default.Info, "Motivo: ${record.reason}", subtitleColor)
                        }
                        if (!record.diagnosis.isNullOrEmpty()) {
                            RecordDetail(Icons.Default.Healing, "Diagnóstico: ${record.diagnosis}", subtitleColor)
                        }
                        if (!record.prescription.isNullOrEmpty()) {
                            RecordDetail(Icons.AutoMirrored.Filled.Assignment, "Prescrição: ${record.prescription}", subtitleColor)
                        }
                    }
                    "TRATAMENTO" -> {
                        if (!record.medication.isNullOrEmpty()) {
                            RecordDetail(Icons.Default.Medication, "Medicamento: ${record.medication}", subtitleColor)
                        }
                        if (!record.dosage.isNullOrEmpty()) {
                            RecordDetail(Icons.Default.Scale, "Dosagem: ${record.dosage}", subtitleColor)
                        }
                        if (!record.endDate.isNullOrEmpty()) {
                            RecordDetail(Icons.Default.DateRange, "Término: ${record.endDate}", subtitleColor)
                        }
                    }
                }

                if (record.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = record.description,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
private fun RecordDetail(icon: ImageVector, text: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = color.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = color
        )
    }
}
