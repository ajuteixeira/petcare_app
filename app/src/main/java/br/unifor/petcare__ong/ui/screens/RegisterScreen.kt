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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import br.unifor.petcare__ong.ui.navigation.Routes
import br.unifor.petcare__ong.ui.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {

    val context = LocalContext.current
    val viewModel: RegisterViewModel = viewModel()


    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var tipoUsuario by remember { mutableStateOf("VOLUNTARIO") }

    // Mesma paleta de cores da LoginScreen
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFE0F7F9), Color(0xFFFFFFFF), Color(0xFFFFF9E3))
    )
    val primaryTeal = Color(0xFF009688)
    val darkBlue = Color(0xFF0D1B3E)
    val lightGrayText = Color(0xFF707B81)
    val inputBackground = Color(0xFFF7F8F9)
    val inputBorder = Color(0xFFE8ECF4)

    if (viewModel.sucessoCadastro) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Cadastro realizado!", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.Login.route) {
                popUpTo(Routes.Register.route) {
                    inclusive = true
                }
            }
            viewModel.resetarCadastro()
        }
    }

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
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState()),
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
                            modifier = Modifier.padding(bottom = 8.dp),

                            )

                        // Reutilização da estrutura de campo
                        RegistrationField(
                            label = "Nome Completo",
                            value = nome,
                            onValueChange = { nome = it },
                            placeholder = "Digite seu nome",
                            labelColor = darkBlue,
                            containerColor = inputBackground,
                            borderColor = inputBorder,
                            erro = viewModel.erroNome
                        )

                        RegistrationField(
                            label = "E-mail",
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "seu@email.com",
                            labelColor = darkBlue,
                            containerColor = inputBackground,
                            borderColor = inputBorder,
                            erro = viewModel.erroEmail
                        )

                        RegistrationField(
                            label = "Senha",
                            value = senha,
                            onValueChange = { senha = it },
                            placeholder = "********",
                            labelColor = darkBlue,
                            containerColor = inputBackground,
                            borderColor = inputBorder,
                            erro = viewModel.erroSenha,
                            isPassword = true
                        )

                        RegistrationField(
                            label = "Confirmar Senha",
                            value = confirmarSenha,
                            onValueChange = { confirmarSenha = it },
                            placeholder = "********",
                            labelColor = darkBlue,
                            containerColor = inputBackground,
                            borderColor = inputBorder,
                            erro = viewModel.erroConfirmarSenha,
                            isPassword = true
                        )

                        Text(
                            text = "Tipo de Usuário",
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                selected = tipoUsuario == "VOLUNTARIO",
                                onClick = {
                                    tipoUsuario = "VOLUNTARIO"
                                }
                            )

                            Text("Voluntário")

                            Spacer(modifier = Modifier.width(16.dp))

                            RadioButton(
                                selected = tipoUsuario == "GESTOR",
                                onClick = { tipoUsuario = "GESTOR" }
                            )

                            Text("Gestor")
                        }


                        Spacer(modifier = Modifier.height(8.dp))

                        // Botão Criar Conta
                        Button(

                            onClick = {
                                viewModel.registrar(nome, email, senha, confirmarSenha, tipoUsuario)
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
                        viewModel.erroGeral?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(250.dp))
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
    borderColor: Color,
    erro: String? = null,
    isPassword: Boolean = false
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
            isError = erro != null,
            visualTransformation =
                if (isPassword)
                    PasswordVisualTransformation()
                else
                    VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                unfocusedBorderColor = borderColor
            )
        )
        if (erro != null) {
            Text(
                text = erro,
                color = Color.Red,
                fontSize = 12.sp
            )
        }
    }
}
