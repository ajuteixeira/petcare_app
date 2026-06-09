package br.unifor.petcare__ong.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
fun AnimalListScreen(navController: NavController) {
    val darkBlue = Color(0xFF0D1B3E)
    val grayText = Color(0xFF707B81)
    val tealPrimary = Color(0xFF009688)

    val repository = remember { AnimalRepository() }
    var animals by remember { mutableStateOf(listOf<Animal>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        repository.listarAnimais { list ->
            animals = list
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lista de Animais",
                        fontWeight = FontWeight.Bold,
                        color = darkBlue,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            AppBottomBar(
                navController = navController,
                currentRoute = Routes.AnimalList.route
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = tealPrimary)
            }
        } else if (animals.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nenhum animal cadastrado", color = grayText)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(animals) { animal ->
                    AnimalItemCard(
                        animal,
                        darkBlue,
                        grayText,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun AnimalItemCard(
    animal: Animal,
    titleColor: Color,
    subtitleColor: Color,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { 
                navController.navigate(Routes.AnimalProfile.createRoute(animal.id)) 
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (animal.especie == "Gato") Color(0xFFFFF9E3) else Color(0xFFE0F7F9)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (!animal.fotoUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = animal.fotoUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = if (animal.especie == "Gato") "🐱" else "🐶",
                        fontSize = 28.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = animal.nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = titleColor
                )
                Text(
                    text = "${animal.especie} • ${animal.raca}",
                    fontSize = 14.sp,
                    color = subtitleColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Surface(
                    color = Color(0xFFF1F3F4),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = animal.status.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = subtitleColor
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFD1D1D6),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
