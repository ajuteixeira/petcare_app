package br.unifor.petcare__ong.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.unifor.petcare__ong.data.AuthRepository

class LoginViewModel : ViewModel() {


    var erroLogin by mutableStateOf<String?>(null)
        private set

    fun login(
        email: String,
        senha: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        AuthRepository().login(email, senha) { sucesso, erro ->
            onResult(sucesso, erro)
        }
    }

    fun validar(email: String, senha: String): Boolean {
        if (email.isBlank()) {
            erroLogin = "Email obrigatório"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            erroLogin = "Email inválido"
            return false
        }

        if (senha.length < 6) {
            erroLogin = "Senha deve ter no mínimo 6 caracteres"
            return false
        }

        erroLogin = null
        return true
    }

    fun definirErro(mensagem: String) {
        erroLogin = mensagem
    }

    fun buscarTipoUsuario(onResult: (String?) -> Unit) {
        AuthRepository().buscarTipoUsuario(onResult)
    }

    fun redefinirSenha(
        email: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        AuthRepository().redefinirSenha(email, onResult)
    }
}