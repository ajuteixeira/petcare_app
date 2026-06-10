package br.unifor.petcare__ong.model

// Importa a anotação que permite mapear os nomes dos campos
// corretamente entre o Kotlin e o Firebase.
import com.google.firebase.database.PropertyName

// Data class utilizada para representar um registro médico do animal.
data class MedicalRecord(

    // Identificador único do registro médico.
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    // ID do animal ao qual este registro pertence.
    @get:PropertyName("animalId")
    @set:PropertyName("animalId")
    var animalId: String = "",

    // Tipo do registro médico.
    // Exemplos: VACINA, CONSULTA, TRATAMENTO ou OBSERVAÇÃO.
    @get:PropertyName("tipo")
    @set:PropertyName("tipo")
    var type: String = "",

    // Data em que o registro foi criado ou ocorreu.
    @get:PropertyName("data")
    @set:PropertyName("data")
    var date: String = "",

    // Título resumido do registro.
    @get:PropertyName("titulo")
    @set:PropertyName("titulo")
    var title: String = "",

    // Descrição detalhada do procedimento ou observação.
    @get:PropertyName("descricao")
    @set:PropertyName("descricao")
    var description: String = "",

    // ==========================
    // CAMPOS ESPECÍFICOS DE VACINA
    // ==========================

    // Número do lote da vacina aplicada.
    @get:PropertyName("lote")
    @set:PropertyName("lote")
    var batch: String? = null,

    // Data prevista para a próxima dose da vacina.
    @get:PropertyName("proximaDose")
    @set:PropertyName("proximaDose")
    var nextDose: String? = null,

    // ==========================
    // CAMPOS ESPECÍFICOS DE CONSULTA
    // ==========================

    // Nome do médico veterinário responsável.
    @get:PropertyName("veterinario")
    @set:PropertyName("veterinario")
    var veterinarian: String? = null,

    // Motivo que levou à consulta.
    @get:PropertyName("motivo")
    @set:PropertyName("motivo")
    var reason: String? = null,

    // Diagnóstico fornecido pelo veterinário.
    @get:PropertyName("diagnostico")
    @set:PropertyName("diagnostico")
    var diagnosis: String? = null,

    // Prescrição ou recomendação médica.
    @get:PropertyName("prescricao")
    @set:PropertyName("prescricao")
    var prescription: String? = null,

    // ==========================
    // CAMPOS ESPECÍFICOS DE TRATAMENTO
    // ==========================

    // Data prevista para encerramento do tratamento.
    @get:PropertyName("dataFim")
    @set:PropertyName("dataFim")
    var endDate: String? = null,

    // Nome do medicamento utilizado.
    @get:PropertyName("medicamento")
    @set:PropertyName("medicamento")
    var medication: String? = null,

    // Dosagem do medicamento.
    @get:PropertyName("dosagem")
    @set:PropertyName("dosagem")
    var dosage: String? = null
)