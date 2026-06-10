package br.unifor.petcare__ong.model

data class User(
    val uid: String = "",
    val nome: String = "",
    val email: String = "",
    val tipo: String = "VOLUNTARIO"
)