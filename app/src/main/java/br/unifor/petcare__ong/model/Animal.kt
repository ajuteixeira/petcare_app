package br.unifor.petcare__ong.model

data class Animal(
    val id: String = "",
    val nome: String = "",
    val especie: String = "",
    val raca: String = "",
    val idade: String = "",
    val sexo: String = "",
    val porte: String = "",
    val status: String = "Disponível",
    val comportamento: String = "",
    val fotoUrl: String? = null,
    val descricao: String = ""
)
