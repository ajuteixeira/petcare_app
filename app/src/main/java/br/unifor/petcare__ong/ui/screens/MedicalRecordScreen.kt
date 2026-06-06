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
import br.unifor.petcare__ong.data.repository.AnimalRepository
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

    val repository = remember { AnimalRepository() }
    var animal by remember { mutableStateOf<Animal?>(null) }
    var records by remember { mutableStateOf<List<MedicalRecord>>(emptyList()) }

    LaunchedEffect(animalId) {
        repository.buscarAnimalPorId(animalId) {
            animal = it
        }
        repository.buscarRegistrosMedicos(animalId) {
            records = it
        }
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
        bottomBar = {

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
                        navController.navigate(Routes.NewRecord.createRoute(animalId, record.id))
                    },
                    onDelete = {
                        repository.deletarRegistroMedico(animalId, record.id, {}, {})
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
    val recordColor = when (record.tipo) {
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
                    .background(recordColor)
            )
            
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Type Badge
                    Surface(
                        color = recordColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = record.tipo,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = recordColor
                        )
                    }
                    
                    // Date and Icons
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = record.data,
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(onClick = onEdit, modifier = Modifier.size(18.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null,
                                tint = Color.LightGray
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = onDelete, modifier = Modifier.size(18.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null,
                                tint = Color.LightGray
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = if (record.titulo.isNotEmpty()) record.titulo else record.descricao,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = titleColor
                )

                if (record.observacoes.isNotEmpty()) {
                    Text(
                        text = record.observacoes,
                        fontSize = 14.sp,
                        color = subtitleColor
                    )
                }
            }
        }
    }
}