package br.unifor.petcare__ong.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.unifor.petcare__ong.data.AnimalRepository
import br.unifor.petcare__ong.model.Animal
import br.unifor.petcare__ong.ui.components.AppBottomBar
import br.unifor.petcare__ong.ui.navigation.Routes
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAnimalScreen(navController: NavController, animalId: String? = null) {
    val darkBlue = Color(0xFF0D1B3E)
    val tealPrimary = Color(0xFF009688)
    val grayText = Color(0xFF707B81)
    val backgroundGray = Color(0xFFF8F9FA)
    val inputBorderColor = Color(0xFFE0E0E0)

    val context = LocalContext.current
    val repository = remember { AnimalRepository() }
    val isEditing = animalId != null

    var nome by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("Cachorro") }
    var raca by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("Macho") }
    var porte by remember { mutableStateOf("Pequeno") }
    var comportamento by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Disponível") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var existingFotoUrl by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    LaunchedEffect(animalId) {
        if (isEditing) {
            repository.buscarAnimalPorId(animalId!!) { animal ->
                animal?.let {
                    nome = it.nome
                    especie = it.especie
                    raca = it.raca
                    idade = it.idade
                    sexo = it.sexo
                    porte = it.porte
                    comportamento = it.comportamento
                    status = it.status
                    existingFotoUrl = it.fotoUrl
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditing) "Editar Animal" else "Cadastro de Animal",
                        fontWeight = FontWeight.Bold,
                        color = darkBlue,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    if (isEditing) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = darkBlue)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            if (!isEditing) {
                AppBottomBar(
                    navController = navController,
                    currentRoute = Routes.AddAnimal.route
                )
            }
        },
        containerColor = backgroundGray
    ) { padding ->
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
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column {
                            Text("Foto", fontWeight = FontWeight.Bold, color = darkBlue, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            PhotoUploadBox(
                                imageUri = imageUri,
                                existingUrl = existingFotoUrl,
                                onClick = { launcher.launch("image/*") },
                                tealColor = tealPrimary,
                                grayText = grayText
                            )
                        }

                        FormField(
                            label = "Nome",
                            value = nome,
                            onValueChange = { nome = it },
                            placeholder = "Nome do animal",
                            labelColor = darkBlue,
                            inputBorderColor = inputBorderColor
                        )

                        FormDropdown(
                            label = "Espécie",
                            options = listOf("Cachorro", "Gato"),
                            selectedOption = especie,
                            onOptionSelected = { especie = it },
                            labelColor = darkBlue,
                            borderColor = inputBorderColor
                        )

                        FormField(
                            label = "Raça",
                            value = raca,
                            onValueChange = { raca = it },
                            placeholder = "Raça do animal",
                            labelColor = darkBlue,
                            inputBorderColor = inputBorderColor
                        )

                        FormField(
                            label = "Idade",
                            value = idade,
                            onValueChange = { idade = it },
                            placeholder = "Ex: 2 anos",
                            labelColor = darkBlue,
                            inputBorderColor = inputBorderColor
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                FormDropdown(
                                    label = "Sexo",
                                    options = listOf("Macho", "Fêmea"),
                                    selectedOption = sexo,
                                    onOptionSelected = { sexo = it },
                                    labelColor = darkBlue,
                                    borderColor = inputBorderColor
                                )
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                FormDropdown(
                                    label = "Porte",
                                    options = listOf("Pequeno", "Médio", "Grande"),
                                    selectedOption = porte,
                                    onOptionSelected = { porte = it },
                                    labelColor = darkBlue,
                                    borderColor = inputBorderColor
                                )
                            }
                        }

                        if (isEditing) {
                            FormDropdown(
                                label = "Status",
                                options = listOf("Disponível", "Em Tratamento", "Adotado"),
                                selectedOption = status,
                                onOptionSelected = { status = it },
                                labelColor = darkBlue,
                                borderColor = inputBorderColor
                            )
                        }

                        Column {
                            Text("Comportamento", fontWeight = FontWeight.Bold, color = darkBlue, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = comportamento,
                                onValueChange = { comportamento = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                placeholder = { Text("Descreva o comportamento do animal", fontSize = 14.sp) },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = tealPrimary,
                                    unfocusedBorderColor = inputBorderColor,
                                    unfocusedContainerColor = Color.White,
                                    focusedContainerColor = Color.White
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { navController.popBackStack() },
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
                                    if (nome.isBlank()) {
                                        Toast.makeText(context, "Por favor, insira o nome", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }

                                    val finalFotoUrl = imageUri?.toString() ?: existingFotoUrl
                                    val animalToSave = Animal(
                                        id = animalId ?: "",
                                        nome = nome,
                                        especie = especie,
                                        raca = raca,
                                        idade = idade,
                                        sexo = sexo,
                                        porte = porte,
                                        comportamento = comportamento,
                                        status = status,
                                        fotoUrl = finalFotoUrl
                                    )

                                    if (isEditing) {
                                        repository.atualizarAnimal(
                                            animal = animalToSave,
                                            onSuccess = {
                                                Toast.makeText(context, "Animal atualizado!", Toast.LENGTH_SHORT).show()
                                                navController.popBackStack()
                                            },
                                            onFailure = {
                                                Toast.makeText(context, "Erro: ${it.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    } else {
                                        repository.salvarAnimal(
                                            animal = animalToSave,
                                            onSuccess = {
                                                Toast.makeText(context, "Animal cadastrado!", Toast.LENGTH_SHORT).show()
                                                navController.navigate(Routes.AnimalList.route) {
                                                    popUpTo(Routes.AddAnimal.route) { inclusive = true }
                                                }
                                            },
                                            onFailure = {
                                                Toast.makeText(context, "Erro: ${it.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = tealPrimary)
                            ) {
                                Text(if (isEditing) "Atualizar" else "Salvar", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoUploadBox(imageUri: Uri?, existingUrl: String?, onClick: () -> Unit, tealColor: Color, grayText: Color) {
    val stroke = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick() }
            .drawWithContent {
                drawContent()
                if (imageUri == null && existingUrl == null) {
                    drawRoundRect(
                        color = Color.LightGray,
                        style = stroke,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                    )
                }
            }
            .background(Color(0xFFF8F9FA), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        val modelToDisplay = imageUri ?: existingUrl
        if (modelToDisplay != null) {
            AsyncImage(
                model = modelToDisplay,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null,
                            tint = tealColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Toque para adicionar foto",
                    color = grayText,
                    fontSize = 12.sp
                )
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
    labelColor: Color,
    inputBorderColor: Color
) {
    Column {
        Text(label, fontWeight = FontWeight.Bold, color = labelColor, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            placeholder = { Text(placeholder, fontSize = 14.sp) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF009688),
                unfocusedBorderColor = inputBorderColor,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    labelColor: Color,
    borderColor: Color
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, fontWeight = FontWeight.Bold, color = labelColor, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .menuAnchor(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF009688),
                    unfocusedBorderColor = borderColor,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
