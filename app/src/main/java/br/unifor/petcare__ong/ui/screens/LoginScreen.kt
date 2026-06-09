package br.unifor.petcare__ong.ui.screens

import br.unifor.petcare__ong.data.AuthRepository
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.unifor.petcare__ong.ui.navigation.Routes

@Composable
fun LoginScreen(navController: NavController) {
    val authRepository = AuthRepository()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }


    // Cores baseadas na imagem
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE0F7F9), Color(0xFFFFFFFF), Color(0xFFFFF9E3))
    )
    val primaryTeal = Color(0xFF009688)
    val darkBlue = Color(0xFF0D1B3E)
    val lightGrayText = Color(0xFF707B81)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logotipo (Simulado com uma Box branca e ícone)
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.size(100.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(text = "🐾", fontSize = 40.sp) // Representação do ícone de patas
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título
            Text(
                text = "PetCare ONG",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = darkBlue
            )
            Text(
                text = "Sistema de Gerenciamento",
                fontSize = 14.sp,
                color = lightGrayText
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Card do Formulário
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Campo E-mail
                    Column {
                        Text("E-mail", fontWeight = FontWeight.SemiBold, color = darkBlue, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it},
                            placeholder = { Text("seu@email.com", color = Color.LightGray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF7F8F9),
                                unfocusedContainerColor = Color(0xFFF7F8F9),
                                unfocusedBorderColor = Color(0xFFE8ECF4)
                            )
                        )
                    }

                    // Campo Senha
                    Column {
                        Text("Senha", fontWeight = FontWeight.SemiBold, color = darkBlue, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = senha,
                            onValueChange = { senha = it},
                            placeholder = { Text("********", color = Color.LightGray) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF7F8F9),
                                unfocusedContainerColor = Color(0xFFF7F8F9),
                                unfocusedBorderColor = Color(0xFFE8ECF4)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botão Entrar
                    Button(
                        onClick = {    // logica de auth

                            authRepository.login(
                                email,
                                senha
                            ) { sucesso, erro ->

                                if (sucesso) {

                                    Toast.makeText(   // pesquisar
                                        context,
                                        "Login realizado com sucesso!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.navigate(Routes.Dashboard.route)

                                } else {

                                    Toast.makeText(
                                        context,
                                        erro ?: "Erro ao fazer login",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    ){
                        Text("Entrar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    // Link de Cadastro
                    Text(
                        text = "Não tem uma conta? Cadastre-se",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable{
                            navController.navigate(
                                Routes.Register.route
                            )
                        },
                        textAlign = TextAlign.Center,
                        color = primaryTeal,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}