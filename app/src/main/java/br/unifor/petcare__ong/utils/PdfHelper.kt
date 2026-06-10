package br.unifor.petcare__ong.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import br.unifor.petcare__ong.model.Animal
import br.unifor.petcare__ong.model.MedicalRecord
import java.io.File
import java.io.FileOutputStream

object PdfHelper {

    fun generateAndSharePdf(
        context: Context,
        animal: Animal,
        records: List<MedicalRecord>,
        photo: Bitmap? = null
    ) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        val paint = Paint()
        var yPosition = 40f

        // Title
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 24f
        paint.color = Color.BLACK
        canvas.drawText("Prontuário Animal: ${animal.nome}", 40f, yPosition, paint)
        yPosition += 40f

        // Photo
        if (photo != null) {
            val scaledPhoto = scaleBitmap(photo, 150, 150)
            canvas.drawBitmap(scaledPhoto, 40f, yPosition, paint)
            yPosition += 170f
        }

        // Registration Info
        paint.textSize = 18f
        paint.color = Color.BLUE
        canvas.drawText("Informações Cadastrais", 40f, yPosition, paint)
        yPosition += 25f

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        paint.textSize = 14f
        paint.color = Color.BLACK
        
        val infoList = listOf(
            "Espécie: ${animal.especie}",
            "Raça: ${animal.raca}",
            "Idade: ${animal.idade}",
            "Sexo: ${animal.sexo}",
            "Porte: ${animal.porte}",
            "Status: ${animal.status}",
            "Comportamento: ${animal.comportamento}"
        )

        for (info in infoList) {
            canvas.drawText(info, 50f, yPosition, paint)
            yPosition += 20f
        }
        
        if (animal.descricao.isNotEmpty()) {
            yPosition += 10f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText("Descrição Geral:", 50f, yPosition, paint)
            yPosition += 18f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            
            // Basic line wrapping for description
            val words = animal.descricao.split(" ")
            var line = ""
            for (word in words) {
                if (paint.measureText("$line$word ") < 500) {
                    line += "$word "
                } else {
                    canvas.drawText(line, 60f, yPosition, paint)
                    yPosition += 15f
                    line = "$word "
                }
            }
            canvas.drawText(line, 60f, yPosition, paint)
            yPosition += 20f
        }
        yPosition += 20f

        // Medical Records Section
        val sections = listOf(
            "VACINA" to "Vacinas",
            "CONSULTA" to "Consultas / Histórico Médico",
            "TRATAMENTO" to "Tratamentos",
            "OBSERVAÇÃO" to "Observações Clínicas e Comportamentais"
        )

        for ((type, title) in sections) {
            val sectionRecords = records.filter { it.type == type }
            if (sectionRecords.isNotEmpty()) {
                // Check if we need a new page
                if (yPosition > 750) {
                    pdfDocument.finishPage(page)
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    yPosition = 40f
                }

                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textSize = 18f
                paint.color = Color.BLUE
                canvas.drawText(title, 40f, yPosition, paint)
                yPosition += 25f

                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textSize = 12f
                paint.color = Color.BLACK

                for (record in sectionRecords) {
                    if (yPosition > 780) {
                        pdfDocument.finishPage(page)
                        page = pdfDocument.startPage(pageInfo)
                        canvas = page.canvas
                        yPosition = 40f
                    }

                    val recordText = "${record.date} - ${record.title}: ${record.description}"
                    canvas.drawText(recordText, 50f, yPosition, paint)
                    yPosition += 18f
                    
                    // Specific fields
                    when (type) {
                        "VACINA" -> {
                            if (!record.batch.isNullOrEmpty()) {
                                canvas.drawText("  Lote: ${record.batch} | Próxima Dose: ${record.nextDose ?: "N/A"}", 60f, yPosition, paint)
                                yPosition += 15f
                            }
                        }
                        "CONSULTA" -> {
                            if (!record.veterinarian.isNullOrEmpty() || !record.diagnosis.isNullOrEmpty()) {
                                canvas.drawText("  Vet: ${record.veterinarian ?: "N/A"} | Diagnóstico: ${record.diagnosis ?: "N/A"}", 60f, yPosition, paint)
                                yPosition += 15f
                            }
                        }
                        "TRATAMENTO" -> {
                            if (!record.medication.isNullOrEmpty()) {
                                canvas.drawText("  Medicamento: ${record.medication} | Dosagem: ${record.dosage ?: "N/A"}", 60f, yPosition, paint)
                                yPosition += 15f
                            }
                        }
                    }
                    yPosition += 10f
                }
                yPosition += 15f
            }
        }

        pdfDocument.finishPage(page)

        // Save and share
        val fileName = "Prontuario_${animal.nome.replace(" ", "_")}.pdf"
        val file = File(context.cacheDir, fileName)

        try {
            pdfDocument.writeTo(FileOutputStream(file))
            pdfDocument.close()
            sharePdf(context, file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun scaleBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val ratio = Math.min(maxWidth.toFloat() / bitmap.width, maxHeight.toFloat() / bitmap.height)
        val width = (bitmap.width * ratio).toInt()
        val height = (bitmap.height * ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    private fun sharePdf(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Exportar Prontuário"))
    }
}
