package br.unifor.petcare__ong.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import br.unifor.petcare__ong.data.AnimalRepository
import br.unifor.petcare__ong.model.Animal
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ReportType(val label: String) {
    FULL_LIST("Lista Completa de Animais"),
    AVAILABLE("Animais Disponíveis"),
    ADOPTIONS("Histórico de Adoções"),
    MONTHLY_SUMMARY("Resumo Mensal")
}

data class ExportState(
    val selectedType: ReportType = ReportType.FULL_LIST,
    val startDate: String = "",
    val endDate: String = "",
    val animals: List<Animal> = emptyList(),
    val isLoading: Boolean = false
)

class ExportViewModel : ViewModel() {
    private val animalRepository = AnimalRepository()
    private val _state = mutableStateOf(ExportState())
    val state: State<ExportState> = _state

    init {
        loadAnimals()
    }

    private fun loadAnimals() {
        _state.value = _state.value.copy(isLoading = true)
        animalRepository.listarAnimais { list ->
            _state.value = _state.value.copy(animals = list, isLoading = false)
        }
    }

    fun onTypeSelected(type: ReportType) {
        _state.value = _state.value.copy(selectedType = type)
    }

    fun onDateChanged(start: String, end: String) {
        _state.value = _state.value.copy(startDate = start, endDate = end)
    }

    fun exportToCsv(context: Context) {
        val currentState = _state.value
        val filteredList = when (currentState.selectedType) {
            ReportType.FULL_LIST -> currentState.animals
            ReportType.AVAILABLE -> currentState.animals.filter { it.status.equals("Disponível", ignoreCase = true) }
            else -> currentState.animals // Simplified for now
        }

        val csvHeader = "Nome,Especie,Raca,Idade,Sexo,Status,Descricao\n"
        val csvContent = StringBuilder(csvHeader)
        
        filteredList.forEach { animal ->
            csvContent.append("${animal.nome},${animal.especie},${animal.raca},${animal.idade},${animal.sexo},${animal.status},${animal.descricao}\n")
        }

        try {
            val fileName = "relatorio_${currentState.selectedType.name.lowercase()}_${SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())}.csv"
            val file = File(context.cacheDir, fileName)
            FileOutputStream(file).use { 
                it.write(csvContent.toString().toByteArray())
            }

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_SUBJECT, "Relatório PetCare ONG")
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Exportar Relatório"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
