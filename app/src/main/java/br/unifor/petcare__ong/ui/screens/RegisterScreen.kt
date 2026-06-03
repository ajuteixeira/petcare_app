package br.unifor.petcare__ong.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    // Mesma paleta de cores da LoginScreen
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE0F7F9), Color(0xFFFFFFFF), Color(0xFFFFF9E3))
    )
    val primaryTeal = Color(0xFF009688)
    val darkBlue = Color(0xFF0D1B3E)
    val lightGrayText = Color(0xFF707B81)
    val inputBackground = Color(0xFFF7F8F9)
    val inputBorder = Color(0xFFE8ECF4)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Cadastro",
                        fontWeight = FontWeight.Bold,
                        color = darkBlue,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Voltar */ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = darkBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent // Para mostrar o gradiente do Box
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundGradient)
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Card Principal
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
                        // Texto de instrução
                        Text(
                            text = "Crie sua conta para gerenciar os animais da ONG.",
                            fontSize = 14.sp,
                            color = lightGrayText,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Reutilização da estrutura de campo
                        RegistrationField(
                            "Nome Completo",
                            "Digite seu nome",
                            darkBlue,
                            inputBackground,
                            inputBorder
                        )
                        RegistrationField(
                            "E-mail",
                            "seu@email.com",
                            darkBlue,
                            inputBackground,
                            inputBorder
                        )
                        RegistrationField(
                            "Senha",
                            "********",
                            darkBlue,
                            inputBackground,
                            inputBorder
                        )
                        RegistrationField(
                            "Confirmar Senha",
                            "********",
                            darkBlue,
                            inputBackground,
                            inputBorder
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botão Criar Conta
                        Button(
                            onClick = { /* Criar Conta */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryTeal)
                        ) {
                            Text("Criar Conta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RegistrationField(
    label: String,
    placeholder: String,
    labelColor: Color,
    containerColor: Color,
    borderColor: Color
) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            color = labelColor,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(placeholder, color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                unfocusedBorderColor = borderColor
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}