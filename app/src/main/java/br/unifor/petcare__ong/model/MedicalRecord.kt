package br.unifor.petcare__ong.model

data class MedicalRecord(
    val id: String = "",
    val type: String = "CONSULTA", // VACINA, CONSULTA, TRATAMENTO, OBSERVAÇÃO
    val title: String = "",
    val date: String = "",
    val time: String = "",
    val professional: String = "",
    val description: String = "",
    val prescription: String = "",
    val nextDose: String = "",
    val lot: String = ""
)
