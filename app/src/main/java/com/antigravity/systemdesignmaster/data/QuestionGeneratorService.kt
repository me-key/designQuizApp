package com.antigravity.systemdesignmaster.data

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class GeneratedQuestion(
    val question: String,
    val options: List<String>,
    val correct_index: Int,
    val explanation: String
)

class QuestionGeneratorService(private val apiKey: String) {

    private val json = Json { ignoreUnknownKeys = true }

    private val model = GenerativeModel(
        modelName = "gemini-2.5-flash-lite",
        apiKey = apiKey,
        generationConfig = generationConfig {
            responseMimeType = "application/json"
        }
    )

    suspend fun generateQuestions(topic: String): List<GeneratedQuestion> {
        val prompt = """
            Generate 30 expert-level multiple-choice questions about $topic for a System Design quiz. 
            Each question must have 4 options, a correct answer index, and a detailed explanation. 
            Return the result strictly as a JSON array matching this schema: 
            [{"question": "", "options": ["", "", "", ""], "correct_index": 0, "explanation": ""}]
        """.trimIndent()

        android.util.Log.d("QuestionGenerator", "Generating questions for topic: $topic")
        android.util.Log.d("QuestionGenerator", "Using API Key: ${apiKey.take(4)}...${apiKey.takeLast(4)} (Length: ${apiKey.length})")

        try {
            val response = model.generateContent(prompt)
            val responseText = response.text
            android.util.Log.d("QuestionGenerator", "Response received. Length: ${responseText?.length}")
            
            if (responseText == null) {
                 android.util.Log.e("QuestionGenerator", "Response text is null")
                 return emptyList()
            }
            
            return json.decodeFromString(responseText)
        } catch (e: Exception) {
            android.util.Log.e("QuestionGenerator", "Error generating questions", e)
            e.printStackTrace()
            // In a real app, propagate error or handle tailored exceptions
            throw e
        }
    }
}
