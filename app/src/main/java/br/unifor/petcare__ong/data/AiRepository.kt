package br.unifor.petcare__ong.data

import com.google.ai.client.generativeai.GenerativeModel
import br.unifor.petcare__ong.model.Animal
import br.unifor.petcare__ong.model.Movement
import br.unifor.petcare__ong.model.MedicalRecord
import br.unifor.petcare__ong.BuildConfig

class AiRepository {
    private val apiKey = BuildConfig.GEMINI_API_KEY
    
    private val generativeModel = GenerativeModel(
        modelName = "gemini-3.1-flash-lite-preview",
        apiKey = apiKey
    )

    suspend fun generateDescription(
        animal: Animal,
        movements: List<Movement>,
        medicalRecords: List<MedicalRecord>
    ): String? {
        val movementsContext = if (movements.isEmpty()) "Nenhuma movimentação registrada." 
        else movements.joinToString("\n") { 
            "- Tipo: ${it.type}, Data/Hora: ${it.startDateTime}, Status: ${it.status}, Notas: ${it.notes}"
        }

        val medicalRecordsContext = if (medicalRecords.isEmpty()) "Nenhum prontuário médico registrado."
        else medicalRecords.joinToString("\n") {
            "- Tipo: ${it.type}, Data: ${it.date}, Título: ${it.title}, Descrição: ${it.description}${it.diagnosis?.let { d -> ", Diagnóstico: $d" } ?: ""}${it.medication?.let { m -> ", Medicamento: $m" } ?: ""}"
        }
        
        val prompt = """
            Crie um resumo geral, curto e objetivo para este animal.
            O objetivo é consolidar as informações existentes de forma organizada e gentil.
            
            Apresente as características principais: nome, idade, raça e comportamento.
            
            ATENÇÃO AO GÊNERO: O animal é do sexo ${animal.sexo}. Use obrigatoriamente os artigos e pronomes corretos (ele/ela, o/a).
            
            INFORMAÇÃO DE MOVIMENTAÇÃO: 
            Você DEVE informar qual foi a última movimentação registrada do animal (baseando-se na data mais recente), mencionando:
            - O tipo da movimentação (ex: passeio, consulta, cirurgia, lar temporário, etc).
            - A data e hora em que ocorreu ou ocorrerá.
            - O status atual (se está agendada, em andamento ou se já foi concluída).

            INFORMAÇÃO DE SAÚDE E PRONTUÁRIO:
            Você DEVE incluir um resumo das informações médicas mais relevantes (vacinas, consultas, tratamentos), destacando condições de saúde atuais ou observações importantes presentes no prontuário.

            FONTES DE INFORMAÇÃO (PROIBIDO INVENTAR DADOS):
            1. PERFIL DO ANIMAL:
            - Nome: ${animal.nome}
            - Espécie: ${animal.especie}
            - Raça: ${animal.raca}
            - Idade: ${animal.idade}
            - Sexo: ${animal.sexo}
            - Porte: ${animal.porte}
            - Comportamento: ${animal.comportamento}

            2. HISTÓRICO DE MOVIMENTAÇÕES:
            $movementsContext

            3. PRONTUÁRIO MÉDICO:
            $medicalRecordsContext

            REGRAS CRÍTICAS:
            1. Baseie-se EXCLUSIVAMENTE nas fontes fornecidas. Não adicione informações externas ou fictícias.
            2. Não tente convencer ninguém à adoção.
            3. O tom deve ser informativo e amigável.
            4. FORMATO: Responda APENAS com o texto final do resumo em formato de TEXTO CORRIDO (parágrafos normais). 
            5. PROIBIÇÕES DE FORMATAÇÃO: É PROIBIDO o uso de Markdown, asteriscos (**), listas com marcadores (bullets) ou cabeçalhos.
            6. Idioma: Português.
        """.trimIndent()

        return try {
            val response = generativeModel.generateContent(prompt)
            response.text
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
