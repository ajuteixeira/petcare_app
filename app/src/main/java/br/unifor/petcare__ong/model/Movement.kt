package br.unifor.petcare__ong.model

import com.google.firebase.database.PropertyName

data class Movement(
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("tipo")
    @set:PropertyName("tipo")
    var type: String = "",

    @get:PropertyName("data")
    @set:PropertyName("data")
    var date: String = "",

    @get:PropertyName("horaInicio")
    @set:PropertyName("horaInicio")
    var startTime: String = "",

    @get:PropertyName("status")
    @set:PropertyName("status")
    var status: String = "",

    @get:PropertyName("responsavel")
    @set:PropertyName("responsavel")
    var responsible: String = "",

    @get:PropertyName("dataHoraFim")
    @set:PropertyName("dataHoraFim")
    var endTime: String = "",

    @get:PropertyName("anotacoes")
    @set:PropertyName("anotacoes")
    var notes: String = ""
)
