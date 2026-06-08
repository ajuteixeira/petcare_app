package br.unifor.petcare__ong.data

import com.google.ai.client.generativeai.GenerativeModel

class AiRepository {
    private val apiKey = "AIzaSyABz7rXHC1ZeYqub0hUL1ygJbMy0P2q97A"
    
    private val generativeModel = GenerativeModel(
        modelName = "gemini-3.1-flash-lite-preview",
        apiKey = apiKey
    )

    suspend fun generateDescription(name: String, species: String, behavior: String): String? {
        val prompt = "Crie uma descrição carinhosa e curta para um anúncio de adoção. " +
                     "Nome: $name, Espécie: $species, Comportamento: $behavior. " +
                     "Responda apenas com a descrição, em português."

        return try {
            val response = generativeModel.generateContent(prompt)
            response.text
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
