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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.unifor.petcare__ong.ui.components.AppBottomBar
import br.unifor.petcare__ong.ui.navigation.Routes

data class Animal(
    val name: String,
    val species: String,
    val status: String,
    val color: Color,
    val emoji: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalListScreen(navController: NavController) {
    val darkBlue = Color(0xFF0D1B3E)
    val grayText = Color(0xFF707B81)
    val tealPrimary = Color(0xFF009688)

    val animals = listOf(
        Animal("Rex", "Cachorro", "DISPONÍVEL", Color(0xFFE0F7F9), "🐶"),
        Animal("Mia", "Gato", "EM TRATAMENTO", Color(0xFFFFF9E3), "🐱"),
        Animal("Bob", "Cachorro", "DISPONÍVEL", Color(0xFFE0F7F9), "🐶"),
        Animal("Luna", "Gato", "ADOTADO", Color(0xFFFFEBEE), "🐱"),
        Animal("Max", "Cachorro", "DISPONÍVEL", Color(0xFFE0F7F9), "🐶")
    )

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
            .clickable { navController.navigate(Routes.AnimalProfile.route) },
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
            // Box do ícone/emoji
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(animal.color, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(animal.emoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Textos informativos
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = animal.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = titleColor
                )
                Text(
                    text = animal.species,
                    fontSize = 14.sp,
                    color = subtitleColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Badge de Status
                Surface(
                    color = Color(0xFFF1F3F4),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = animal.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = subtitleColor
                    )
                }
            }

            // Seta lateral
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFD1D1D6),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}