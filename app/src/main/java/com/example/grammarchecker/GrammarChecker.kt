package com.example.grammarchecker

class GrammarChecker {

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

    fun checkGrammar(text: String): List<GrammarError> {
        val errors = mutableListOf<GrammarError>()
        
        // Simple grammar checking patterns
        checkSubjectVerbAgreement(text, errors)
        checkMissingArticles(text, errors)
        checkPrepositions(text, errors)
        checkPluralForms(text, errors)
        checkTenses(text, errors)
        checkSpelling(text, errors)
        checkPunctuation(text, errors)
        
        return errors.distinctBy { it.startIndex to it.endIndex }
    }

    private fun checkSubjectVerbAgreement(text: String, errors: MutableList<GrammarError>) {
        // Common subject-verb disagreement patterns
        val patterns = listOf(
            "I am" to "I are",
            "He are" to "He is",
            "She are" to "She is",
            "It are" to "It is",
            "They is" to "They are",
            "We is" to "We are",
            "You is" to "You are"
        )
        
        patterns.forEach { (correct, incorrect) ->
            val index = text.indexOf(incorrect, ignoreCase = true)
            if (index >= 0) {
                errors.add(
                    GrammarError(
                        startIndex = index,
                        endIndex = index + incorrect.length,
                        errorType = ErrorType.SUBJECT_VERB,
                        description = "Subject-verb disagreement",
                        descriptionChinese = "主谓不一致",
                        suggestedCorrection = correct
                    )
                )
            }
        }
    }

    private fun checkMissingArticles(text: String, errors: MutableList<GrammarError>) {
        val words = text.split(" ")
        var currentIndex = 0
        
        words.forEachIndexed { i, word ->
            val cleanWord = word.replace(Regex("[^a-zA-Z]"), "")
            if (cleanWord.isNotEmpty()) {
                // Check for missing articles before countable nouns
                val articles = listOf("the", "a", "an")
                val hasArticle = i > 0 && articles.any { 
                    words[i - 1].replace(Regex("[^a-zA-Z]"), "").equals(it, ignoreCase = true)
                }
                
                val countableNouns = listOf("book", "car", "house", "dog", "cat", "phone", "computer", "table", "chair")
                
                if (!hasArticle && countableNouns.any { cleanWord.equals(it, ignoreCase = true) }) {
                    // Check if it should have "a" or "an"
                    val shouldHaveArticle = when {
                        cleanWord.startsWith("a", ignoreCase = true) || 
                        cleanWord.startsWith("e", ignoreCase = true) ||
                        cleanWord.startsWith("i", ignoreCase = true) ||
                        cleanWord.startsWith("o", ignoreCase = true) ||
                        cleanWord.startsWith("u", ignoreCase = true) -> "an"
                        else -> "a"
                    }
                    
                    errors.add(
                        GrammarError(
                            startIndex = currentIndex,
                            endIndex = currentIndex + word.length,
                            errorType = ErrorType.ARTICLE_MISSING,
                            description = "Missing article",
                            descriptionChinese = "缺少冠词",
                            suggestedCorrection = "$shouldHaveArticle $word"
                        )
                    )
                }
            }
            currentIndex += word.length + 1
        }
    }

    private fun checkPrepositions(text: String, errors: MutableList<GrammarError>) {
        val incorrectPrepositions = listOf(
            "depend from" to "depend on",
            "interested to" to "interested in",
            "good in" to "good at",
            "afraid from" to "afraid of",
            "angry to" to "angry with",
            "married with" to "married to"
        )
        
        incorrectPrepositions.forEach { (incorrect, correct) ->
            val index = text.indexOf(incorrect, ignoreCase = true)
            if (index >= 0) {
                errors.add(
                    GrammarError(
                        startIndex = index,
                        endIndex = index + incorrect.length,
                        errorType = ErrorType.PREPOSITION,
                        description = "Wrong preposition",
                        descriptionChinese = "介词错误",
                        suggestedCorrection = correct
                    )
                )
            }
        }
    }

    private fun checkPluralForms(text: String, errors: MutableList<GrammarError>) {
        // Common plural mistakes
        val pluralMistakes = listOf(
            "childs" to "children",
            "mans" to "men",
            "womans" to "women",
            "foots" to "feet",
            "gooses" to "geese"
        )
        
        pluralMistakes.forEach { (incorrect, correct) ->
            val index = text.indexOf(incorrect, ignoreCase = true)
            if (index >= 0) {
                errors.add(
                    GrammarError(
                        startIndex = index,
                        endIndex = index + incorrect.length,
                        errorType = ErrorType.PLURAL,
                        description = "Incorrect plural form",
                        descriptionChinese = "复数形式错误",
                        suggestedCorrection = correct
                    )
                )
            }
        }
    }

    private fun checkTenses(text: String, errors: MutableList<GrammarError>) {
        // Simple tense checking
        val tensePatterns = listOf(
            "I go yesterday" to "I went yesterday",
            "he go to school" to "he goes to school",
            "she have" to "she has",
            "it have" to "it has"
        )
        
        tensePatterns.forEach { (incorrect, correct) ->
            val index = text.indexOf(incorrect, ignoreCase = true)
            if (index >= 0) {
                errors.add(
                    GrammarError(
                        startIndex = index,
                        endIndex = index + incorrect.length,
                        errorType = ErrorType.TENSE,
                        description = "Wrong tense",
                        descriptionChinese = "时态错误",
                        suggestedCorrection = correct
                    )
                )
            }
        }
    }

    private fun checkSpelling(text: String, errors: MutableList<GrammarError>) {
        // Common spelling mistakes
        val spellingMistakes = listOf(
            "recieve" to "receive",
            "seperate" to "separate",
            "definately" to "definitely",
            "occured" to "occurred",
            "accomodate" to "accommodate",
            "beleive" to "believe",
            "freind" to "friend",
            "teh" to "the",
            "adn" to "and"
        )
        
        spellingMistakes.forEach { (incorrect, correct) ->
            val index = text.indexOf(incorrect, ignoreCase = true)
            if (index >= 0) {
                errors.add(
                    GrammarError(
                        startIndex = index,
                        endIndex = index + incorrect.length,
                        errorType = ErrorType.SPELLING,
                        description = "Spelling mistake",
                        descriptionChinese = "拼写错误",
                        suggestedCorrection = correct
                    )
                )
            }
        }
    }

    private fun checkPunctuation(text: String, errors: MutableList<GrammarError>) {
        // Check for missing spaces after punctuation
        val punctuationPattern = Regex("[.!?][A-Z]")
        val matches = punctuationPattern.findAll(text)
        
        matches.forEach { match ->
            val actualText = match.value
            if (!actualText.contains(" ")) {
                val index = match.range.first + 1
                errors.add(
                    GrammarError(
                        startIndex = index,
                        endIndex = index + 1,
                        errorType = ErrorType.PUNCTUATION,
                        description = "Missing space after punctuation",
                        descriptionChinese = "标点符号后缺少空格",
                        suggestedCorrection = " "
                    )
                )
            }
        }
        
        // Check for double spaces
        var index = text.indexOf("  ")
        while (index >= 0) {
            errors.add(
                GrammarError(
                    startIndex = index,
                    endIndex = index + 2,
                    errorType = ErrorType.PUNCTUATION,
                    description = "Extra space",
                    descriptionChinese = "多余的空格",
                    suggestedCorrection = " "
                )
            )
            index = text.indexOf("  ", index + 1)
        }
    }
}