package br.unifor.petcare__ong.model

import com.google.firebase.database.PropertyName

data class MedicalRecord(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("animalId")
    @set:PropertyName("animalId")
    var animalId: String = "",

    @get:PropertyName("tipo")
    @set:PropertyName("tipo")
    var type: String = "", // VACINA, CONSULTA, TRATAMENTO, OBSERVAÇÃO

    @get:PropertyName("data")
    @set:PropertyName("data")
    var date: String = "",

    @get:PropertyName("titulo")
    @set:PropertyName("titulo")
    var title: String = "",

    @get:PropertyName("descricao")
    @set:PropertyName("descricao")
    var description: String = "",

    // Campos específicos para vacinas
    @get:PropertyName("lote")
    @set:PropertyName("lote")
    var batch: String? = null,

    @get:PropertyName("proximaDose")
    @set:PropertyName("proximaDose")
    var nextDose: String? = null,

    // Campos específicos para consultas
    @get:PropertyName("veterinario")
    @set:PropertyName("veterinario")
    var veterinarian: String? = null,

    @get:PropertyName("motivo")
    @set:PropertyName("motivo")
    var reason: String? = null,

    @get:PropertyName("diagnostico")
    @set:PropertyName("diagnostico")
    var diagnosis: String? = null,

    @get:PropertyName("prescricao")
    @set:PropertyName("prescricao")
    var prescription: String? = null,

    // Campos específicos para tratamentos
    @get:PropertyName("dataFim")
    @set:PropertyName("dataFim")
    var endDate: String? = null,

    @get:PropertyName("medicamento")
    @set:PropertyName("medicamento")
    var medication: String? = null,

    @get:PropertyName("dosagem")
    @set:PropertyName("dosagem")
    var dosage: String? = null
)
