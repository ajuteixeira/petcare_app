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
            Crie um resumo geral, curto e objetivo para este animal em formato de TEXTO CORRIDO e amigável.
            O objetivo é consolidar as informações existentes de forma organizada em parágrafos, seguindo estritamente esta ordem:
            
            1. No primeiro parágrafo, apresente o PERFIL: características principais como nome, idade, raça e comportamento.
            
            2. No segundo parágrafo, apresente a SAÚDE: um resumo das informações médicas mais relevantes do prontuário (vacinas, consultas, tratamentos), destacando condições atuais ou observações importantes.
            
            3. No terceiro parágrafo, apresente a ÚLTIMA MOVIMENTAÇÃO registrada (baseando-se na data mais recente), mencionando:
            - O tipo da movimentação (ex: passeio, consulta, cirurgia, lar temporário, óbito, etc).
            - A data e hora em que ocorreu ou ocorrerá.
            - O status atual (se está agendada, em andamento ou se já foi concluída). 
            - EXCEÇÃO CRÍTICA: Se a movimentação for do tipo "Óbito", informe apenas a data, NUNCA o status.
            
            ATENÇÃO AO GÊNERO: O animal é do sexo ${animal.sexo}. Use obrigatoriamente os artigos e pronomes corretos (ele/ela, o/a).

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
            4. FORMATO: Responda APENAS com o texto final do resumo em formato de TEXTO CORRIDO, dividido em parágrafos.
            5. PROIBIÇÕES DE FORMATAÇÃO: É TERMINANTEMENTE PROIBIDO o uso de tópicos, listas com marcadores (bullets), Markdown, asteriscos (**), cabeçalhos ou rótulos (como "Perfil:", "Saúde:", etc.). Escreva apenas o texto corrido.
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
