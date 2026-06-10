package br.unifor.petcare__ong.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import br.unifor.petcare__ong.model.User

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
        nome: String,
        email: String,
        senha: String,
        tipoUsuario: String,
        callback: (Boolean, String?) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(
            email.trim(),
            senha
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                val uid = auth.currentUser?.uid ?: ""

                val usuario = User(
                    uid = uid,
                    nome = nome,
                    email = email,
                    tipo = tipoUsuario
                )

                FirebaseDatabase
                    .getInstance()
                    .getReference("usuarios")
                    .child(uid)
                    .setValue(usuario)
                    .addOnSuccessListener {

                        callback(true, null)

                    }
                    .addOnFailureListener {

                        callback(false, it.message)

                    }

            } else {

                callback(
                    false,
                    task.exception?.message
                )
            }
        }
    }
    fun buscarTipoUsuario(
        callback: (String?) -> Unit
    ) {

        val uid = auth.currentUser?.uid ?: run {
            callback(null)
            return
        }

        FirebaseDatabase
            .getInstance()
            .getReference("usuarios")
            .child(uid)
            .child("tipo")
            .get()
            .addOnSuccessListener {

                callback(it.value?.toString())

            }
            .addOnFailureListener {

                callback(null)

            }
    }
}