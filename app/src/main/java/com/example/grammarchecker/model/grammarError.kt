package com.example.grammarchecker.model

data class GrammarError(
    val startIndex: Int,
    val endIndex: Int,
    val errorType: ErrorType,
    val description: String,
    val descriptionChinese: String,
    val suggestedCorrection: String
)

enum class ErrorType {
    SUBJECT_VERB,
    ARTICLE_MISSING,
    PREPOSITION,
    PLURAL,
    TENSE,
    SPELLING,
    PUNCTUATION
}