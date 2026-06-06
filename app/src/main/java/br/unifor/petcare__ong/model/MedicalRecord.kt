package br.unifor.petcare__ong.model

data class MedicalRecord(
    val id: String = "",
    val animalId: String = "",
    val tipo: String = "", // VACINA, CONSULTA, TRATAMENTO, OBSERVAÇÃO
    val titulo: String = "",
    val descricao: String = "",
    val data: String = "",
    val hora: String = "",
    val veterinario: String = "",
    val lote: String = "",
    val proximaDose: String = "",
    val diagnostico: String = "",
    val prescricao: String = "",
    val observacoes: String = ""
)