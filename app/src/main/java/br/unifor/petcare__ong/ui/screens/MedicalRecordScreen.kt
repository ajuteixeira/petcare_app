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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.unifor.petcare__ong.data.AnimalRepository
import br.unifor.petcare__ong.data.MedicalRecordRepository
import br.unifor.petcare__ong.model.Animal
import br.unifor.petcare__ong.model.MedicalRecord
import br.unifor.petcare__ong.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalRecordScreen(navController: NavController, animalId: String) {
    val darkBlue = Color(0xFF0D1B3E)
    val tealPrimary = Color(0xFF009688)
    val grayText = Color(0xFF707B81)
    val backgroundGray = Color(0xFFF8F9FA)

    val animalRepository = remember { AnimalRepository() }
    val recordRepository = remember { MedicalRecordRepository() }

    var animal by remember { mutableStateOf<Animal?>(null) }
    var records by remember { mutableStateOf<List<MedicalRecord>>(emptyList()) }

    LaunchedEffect(animalId) {
        animalRepository.buscarAnimalPorId(animalId) { animal = it }
        recordRepository.listRecords(animalId) { records = it }
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
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
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
                        recordRepository.deleteRecord(animalId, record.id, {}, {})
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
            // Left color strip
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
                    // Type Badge
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

                    // Date and Icons
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = record.date,
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(onClick = onEdit, modifier = Modifier.size(18.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Editar",
                                tint = Color.LightGray
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = onDelete, modifier = Modifier.size(18.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Excluir",
                                tint = Color.LightGray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = record.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = titleColor
                )
                if (record.description.isNotEmpty()) {
                    Text(
                        text = record.description,
                        fontSize = 14.sp,
                        color = subtitleColor
                    )
                }
            }
        }
    }
}
