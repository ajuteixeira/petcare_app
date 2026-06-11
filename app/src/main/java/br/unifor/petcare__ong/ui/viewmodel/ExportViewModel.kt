package br.unifor.petcare__ong.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import br.unifor.petcare__ong.data.AnimalRepository
import br.unifor.petcare__ong.model.Animal
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.io.File
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

    fun exportToPdf(context: Context) {
        val currentState = _state.value
        val filteredList = when (currentState.selectedType) {
            ReportType.FULL_LIST -> currentState.animals
            ReportType.AVAILABLE -> currentState.animals.filter { it.status.equals("Disponível", ignoreCase = true) }
            else -> currentState.animals
        }

        try {
            val fileName = "relatorio_${currentState.selectedType.name.lowercase()}_${SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())}.pdf"
            val file = File(context.cacheDir, fileName)
            
            val pdfWriter = PdfWriter(file)
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            // Title
            document.add(Paragraph("PETCARE ONG").setBold().setFontSize(20f).setTextAlignment(TextAlignment.CENTER))
            document.add(Paragraph("Relatório: ${currentState.selectedType.label}").setFontSize(14f).setTextAlignment(TextAlignment.CENTER))
            document.add(Paragraph("Gerado em: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}").setFontSize(10f).setTextAlignment(TextAlignment.CENTER))
            document.add(Paragraph("\n"))

            // Table
            val table = Table(UnitValue.createPercentArray(floatArrayOf(3f, 2f, 2f, 1f, 2f)))
            table.width = UnitValue.createPercentValue(100f)

            // Headers
            val headers = listOf("Nome", "Espécie", "Raça", "Idade", "Status")
            headers.forEach { header ->
                table.addHeaderCell(Cell().add(Paragraph(header).setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY))
            }

            // Data
            filteredList.forEach { animal ->
                table.addCell(Cell().add(Paragraph(animal.nome)))
                table.addCell(Cell().add(Paragraph(animal.especie)))
                table.addCell(Cell().add(Paragraph(animal.raca)))
                table.addCell(Cell().add(Paragraph(animal.idade)))
                table.addCell(Cell().add(Paragraph(animal.status)))
            }

            document.add(table)
            document.add(Paragraph("\nTotal de registros: ${filteredList.size}").setBold())
            
            document.close()

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_SUBJECT, "Relatório PetCare ONG - PDF")
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Exportar Relatório PDF"))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
