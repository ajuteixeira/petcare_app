package br.unifor.petcare__ong.data

import com.google.firebase.auth.FirebaseAuth

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    fun login(
        email: String,
        senha: String,
        callback: (Boolean, String?) -> Unit
    ) {

        auth.signInWithEmailAndPassword(
            email.trim(),
            senha
        ).addOnCompleteListener { task ->

            callback(
                task.isSuccessful,
                task.exception?.message
            )
        }
    }

    fun cadastrar(
        email: String,
        senha: String,
        callback: (Boolean, String?) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(
            email.trim(),
            senha
        ).addOnCompleteListener { task ->

            callback(
                task.isSuccessful,
                task.exception?.message
            )
        }
    }
}