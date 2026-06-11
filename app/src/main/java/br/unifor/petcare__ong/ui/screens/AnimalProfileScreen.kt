package br.unifor.petcare__ong.ui.screens

import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.unifor.petcare__ong.data.AiRepository
import br.unifor.petcare__ong.data.MovementRepository
import br.unifor.petcare__ong.data.MedicalRecordRepository
import br.unifor.petcare__ong.model.Animal
import br.unifor.petcare__ong.ui.navigation.Routes
import br.unifor.petcare__ong.utils.PdfHelper
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import br.unifor.petcare__ong.ui.session.SessionManager
import br.unifor.petcare__ong.ui.viewmodel.AnimalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalProfileScreen(
    navController: NavController, 
    animalId: String,
    viewModel: AnimalViewModel = viewModel()
) {
    val darkBlue = Color(0xFF0D1B3E)
    val tealPrimary = Color(0xFF009688)
    val grayText = Color(0xFF707B81)
    val backgroundGray = Color(0xFFF8F9FA)
    val deleteRed = Color(0xFFFF2D55)

    val context = LocalContext.current
    val aiRepository = remember { AiRepository() }
    val movementRepository = remember { MovementRepository() }
    val medicalRecordRepository = remember { MedicalRecordRepository() }
    val scope = rememberCoroutineScope()

    val animal by viewModel.animalSelecionado.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var aiDescription by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(animalId) {
        viewModel.buscarAnimalPorId(animalId)
    }

    LaunchedEffect(animal) {
        animal?.let {
            aiDescription = it.descricao
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Tem certeza que deseja excluir este animal? Esta ação não poderá ser desfeita.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deletarAnimal(
                            id = animalId,
                            onSuccess = {
                                Toast.makeText(context, "Animal excluído!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            },
                            onFailure = { e ->
                                Toast.makeText(context, "Erro ao excluir: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = deleteRed)
                ) {
                    Text("Excluir", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", color = darkBlue)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Perfil do Animal",
                        fontWeight = FontWeight.Bold,
                        color = darkBlue,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = darkBlue)
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
        } else if (animal == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Animal não encontrado", color = grayText)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(Color(0xFFB2EBF2), Color(0xFF00796B))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (!animal?.fotoUrl.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = animal?.fotoUrl,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop,
                                        colorFilter = if (animal?.status == "Falecido") {
                                            ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                                        } else null
                                    )
                                } else {
                                    Icon(imageVector = Icons.Default.Pets, contentDescription = null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(80.dp))
                                }
                            }

                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(text = animal?.nome ?: "", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = darkBlue)
                                
                                Surface(
                                    color = tealPrimary.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = animal?.status?.uppercase() ?: "",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = tealPrimary
                                    )
                                }

                                Row(modifier = Modifier.fillMaxWidth()) {
                                    DetailItem(label = "ESPÉCIE", value = animal?.especie ?: "", modifier = Modifier.weight(1f), grayText, darkBlue)
                                    DetailItem(label = "RAÇA", value = animal?.raca ?: "", modifier = Modifier.weight(1f), grayText, darkBlue)
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    DetailItem(label = "IDADE", value = animal?.idade ?: "", modifier = Modifier.weight(1f), grayText, darkBlue)
                                    DetailItem(label = "SEXO", value = animal?.sexo ?: "", modifier = Modifier.weight(1f), grayText, darkBlue)
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    DetailItem(label = "PORTE", value = animal?.porte ?: "", modifier = Modifier.weight(1f), grayText = grayText, valueColor = darkBlue)
                                    DetailItem(label = "COMPORTAMENTO", value = animal?.comportamento ?: "N/A", modifier = Modifier.weight(1f), grayText = grayText, valueColor = darkBlue)
                                }
                                
                                Spacer(modifier = Modifier.height(24.dp))

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = Color(0xFFF8F9FA),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "DESCRIÇÃO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB0B0B0))
                                            
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.clickable { 
                                                    if (!isGenerating && animal != null) {
                                                        isGenerating = true
                                                        movementRepository.listMovements(animalId) { movements ->
                                                            medicalRecordRepository.listRecords(animalId) { records ->
                                                                scope.launch {
                                                                    val result = aiRepository.generateDescription(animal!!, movements, records)
                                                                    if (result != null) {
                                                                        aiDescription = result
                                                                        viewModel.atualizarDescricao(
                                                                            id = animalId,
                                                                            descricao = result,
                                                                            onSuccess = { Toast.makeText(context, "Descrição salva!", Toast.LENGTH_SHORT).show() },
                                                                            onFailure = { e -> Toast.makeText(context, "Erro ao salvar: ${e.message}", Toast.LENGTH_SHORT).show() }
                                                                        )
                                                                    } else {
                                                                        Toast.makeText(context, "Erro ao gerar descrição", Toast.LENGTH_SHORT).show()
                                                                    }
                                                                    isGenerating = false
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            ) {
                                                if (isGenerating) {
                                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = tealPrimary)
                                                } else {
                                                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = tealPrimary, modifier = Modifier.size(16.dp))
                                                }
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(text = "Gerar com IA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = tealPrimary)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = if (isGenerating) "Gerando descrição..." else if (aiDescription.isNotEmpty()) aiDescription else "Gere uma descrição automática para este animal clicando no botão acima.",
                                            fontSize = 14.sp,
                                            color = Color(0xFF455A64),
                                            lineHeight = 20.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate(Routes.EditAnimal.createRoute(animalId)) },
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F3F4))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Edit, contentDescription = null, tint = darkBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Editar", color = darkBlue, fontWeight = FontWeight.Bold)
                            }
                        }
                        if (SessionManager.tipoUsuario?.uppercase() == "GESTOR") {
                            Button(
                                onClick = { showDeleteDialog = true },
                                modifier = Modifier.weight(1f).height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = deleteRed)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Outlined.Delete, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Excluir", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item {
                    ActionButton(
                        text = "Prontuário Médico",
                        icon = Icons.Default.Timeline,
                        containerColor = tealPrimary,
                        onClick = { navController.navigate(Routes.MedicalRecord.createRoute(animalId)) }
                    )
                }

                item {
                    ActionButton(
                        text = "Histórico de Movimentações",
                        icon = Icons.Default.Map,
                        containerColor = tealPrimary,
                        onClick = { navController.navigate(Routes.MovementHistory.createRoute(animalId)) }
                    )
                }

                item {
                    var isExporting by remember { mutableStateOf(false) }
                    if (SessionManager.tipoUsuario?.uppercase() == "GESTOR") {
                        ActionButton(
                            text = if (isExporting) "Gerando PDF..." else "Exportar Prontuário PDF",
                            icon = Icons.Default.PictureAsPdf,
                            containerColor = Color(0xFFE91E63),
                            onClick = {
                                if (!isExporting && animal != null) {
                                    isExporting = true
                                    medicalRecordRepository.listRecords(animalId) { records ->
                                        scope.launch {
                                            var bitmap: android.graphics.Bitmap? = null
                                            if (!animal?.fotoUrl.isNullOrEmpty()) {
                                                val request = ImageRequest.Builder(context).data(animal?.fotoUrl).allowHardware(false).build()
                                                val result = context.imageLoader.execute(request)
                                                if (result is SuccessResult) {
                                                    bitmap = (result.drawable as? BitmapDrawable)?.bitmap
                                                }
                                            }
                                            PdfHelper.generateAndSharePdf(context, animal!!, records, bitmap)
                                            isExporting = false
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, modifier: Modifier = Modifier, grayText: Color, valueColor: Color) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 10.sp, color = grayText, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 16.sp, color = valueColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ActionButton(text: String, icon: ImageVector, containerColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
