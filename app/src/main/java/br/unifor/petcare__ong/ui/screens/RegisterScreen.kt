package br.unifor.petcare__ong.ui.screens

import br.unifor.petcare__ong.data.AuthRepository
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import br.unifor.petcare__ong.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {

    val authRepository = AuthRepository()
    val context = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

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
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
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
                            label = "Nome Completo",
                            value = nome,
                            onValueChange = { nome = it },
                            placeholder = "Digite seu nome",
                            labelColor = darkBlue,
                            containerColor = inputBackground,
                            borderColor = inputBorder
                        )

                        RegistrationField(
                            label = "E-mail",
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "seu@email.com",
                            labelColor = darkBlue,
                            containerColor = inputBackground,
                            borderColor = inputBorder
                        )

                        RegistrationField(
                            label = "Senha",
                            value = senha,
                            onValueChange = { senha = it },
                            placeholder = "********",
                            labelColor = darkBlue,
                            containerColor = inputBackground,
                            borderColor = inputBorder
                        )

                        RegistrationField(
                            label = "Confirmar Senha",
                            value = confirmarSenha,
                            onValueChange = { confirmarSenha = it },
                            placeholder = "********",
                            labelColor = darkBlue,
                            containerColor = inputBackground,
                            borderColor = inputBorder
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botão Criar Conta
                        Button(
                            onClick = {    // logica de auth

                                if (senha != confirmarSenha) {

                                    Toast.makeText(
                                        context,
                                        "As senhas não coincidem",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    return@Button
                                }

                                authRepository.cadastrar(
                                    email,
                                    senha
                                ) { sucesso, erro ->

                                    if (sucesso) {

                                        Toast.makeText(
                                            context,
                                            "Conta criada com sucesso!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        navController.navigate(
                                            Routes.Login.route
                                        )

                                    } else {

                                        Toast.makeText(
                                            context,
                                            erro ?: "Erro ao cadastrar",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryTeal
                            )
                        ) {
                            Text(
                                "Criar Conta",
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
fun RegistrationField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
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
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
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
