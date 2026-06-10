package br.unifor.petcare__ong.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.unifor.petcare__ong.data.AuthRepository

class RegisterViewModel : ViewModel() {

    var erroNome by mutableStateOf<String?>(null)
    var erroEmail by mutableStateOf<String?>(null)
    var erroSenha by mutableStateOf<String?>(null)
    var erroConfirmarSenha by mutableStateOf<String?>(null)

    fun validar(nome: String, email: String, senha: String, confirmar: String): Boolean {

        erroNome = if (nome.isEmpty()) "Nome obrigatório" else null

        erroEmail = if (!email.contains("@")) "Email inválido" else null

        erroSenha = if (senha.length < 6) "Mínimo 6 caracteres" else null

        erroConfirmarSenha =
            if (senha != confirmar) "Senhas não coincidem" else null

        return erroNome == null &&
                erroEmail == null &&
                erroSenha == null &&
                erroConfirmarSenha == null
    }

    var erroGeral by mutableStateOf<String?>(null)
        private set

    var sucessoCadastro by mutableStateOf(false)
        private set

    fun registrar(
        nome: String,
        email: String,
        senha: String,
        confirmar: String
    ) {

        sucessoCadastro = false
        erroGeral = null

        val valido = validar(nome, email, senha, confirmar)

        if (!valido) return

        AuthRepository().cadastrar(email, senha) { sucesso, erro ->

            if (sucesso) {
                sucessoCadastro = true
            } else {
                erroGeral = erro ?: "Erro ao cadastrar"
            }
        }
    }

    fun resetarCadastro() {
        sucessoCadastro = false
    }
}