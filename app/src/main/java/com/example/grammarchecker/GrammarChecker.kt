package com.example.grammarchecker

import com.example.grammarchecker.model.ErrorType
import com.example.grammarchecker.model.GrammarError

class GrammarChecker {

    fun checkGrammar(text: String): List<GrammarError> {
        val errors = mutableListOf<GrammarError>()
        
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
                errors.add(GrammarError(index, index + incorrect.length, ErrorType.SUBJECT_VERB, 
                    "Subject-verb disagreement", "主谓不一致", correct))
            }
        }
    }

    private fun checkMissingArticles(text: String, errors: MutableList<GrammarError>) {
        val words = text.split(" ")
        var currentIndex = 0
        val articles = listOf("the", "a", "an")
        val countableNouns = listOf("book", "car", "house", "dog", "cat", "phone", "computer", "table", "chair")
        words.forEachIndexed { i, word ->
            val cleanWord = word.replace(Regex("[^a-zA-Z]"), "")
            if (cleanWord.isNotEmpty()) {
                val hasArticle = i > 0 && articles.any { words[i-1].replace(Regex("[^a-zA-Z]"), "").equals(it, ignoreCase=true) }
                if (!hasArticle && countableNouns.any { cleanWord.equals(it, ignoreCase=true) }) {
                    val article = if (cleanWord.first().lowercase() in "aeiou") "an" else "a"
                    errors.add(GrammarError(currentIndex, currentIndex + word.length, ErrorType.ARTICLE_MISSING,
                        "Missing article", "缺少冠词", "$article $word"))
                }
            }
            currentIndex += word.length + 1
        }
    }

    private fun checkPrepositions(text: String, errors: MutableList<GrammarError>) {
        val list = listOf("depend from" to "depend on", "interested to" to "interested in",
            "good in" to "good at", "afraid from" to "afraid of", "angry to" to "angry with", "married with" to "married to")
        list.forEach { (incorrect, correct) ->
            val i = text.indexOf(incorrect, ignoreCase = true)
            if (i >= 0) errors.add(GrammarError(i, i+incorrect.length, ErrorType.PREPOSITION, "Wrong preposition", "介词错误", correct))
        }
    }

    private fun checkPluralForms(text: String, errors: MutableList<GrammarError>) {
        val list = listOf("childs" to "children", "mans" to "men", "womans" to "women", "foots" to "feet", "gooses" to "geese")
        list.forEach { (incorrect, correct) ->
            val i = text.indexOf(incorrect, ignoreCase = true)
            if (i >= 0) errors.add(GrammarError(i, i+incorrect.length, ErrorType.PLURAL, "Incorrect plural form", "复数形式错误", correct))
        }
    }

    private fun checkTenses(text: String, errors: MutableList<GrammarError>) {
        val list = listOf("I go yesterday" to "I went yesterday", "he go to school" to "he goes to school",
            "she have" to "she has", "it have" to "it has")
        list.forEach { (incorrect, correct) ->
            val i = text.indexOf(incorrect, ignoreCase = true)
            if (i >= 0) errors.add(GrammarError(i, i+incorrect.length, ErrorType.TENSE, "Wrong tense", "时态错误", correct))
        }
    }

    private fun checkSpelling(text: String, errors: MutableList<GrammarError>) {
        val list = listOf("recieve" to "receive", "seperate" to "separate", "definately" to "definitely",
            "occured" to "occurred", "accomodate" to "accommodate", "beleive" to "believe", "freind" to "friend",
            "teh" to "the", "adn" to "and")
        list.forEach { (incorrect, correct) ->
            val i = text.indexOf(incorrect, ignoreCase = true)
            if (i >= 0) errors.add(GrammarError(i, i+incorrect.length, ErrorType.SPELLING, "Spelling mistake", "拼写错误", correct))
        }
    }

    private fun checkPunctuation(text: String, errors: MutableList<GrammarError>) {
        Regex("[.!?][A-Z]").findAll(text).forEach { match ->
            if (!match.value.contains(" ")) {
                val idx = match.range.first + 1
                errors.add(GrammarError(idx, idx+1, ErrorType.PUNCTUATION, "Missing space after punctuation", "标点符号后缺少空格", " "))
            }
        }
        var idx = text.indexOf("  ")
        while (idx >= 0) {
            errors.add(GrammarError(idx, idx+2, ErrorType.PUNCTUATION, "Extra space", "多余的空格", " "))
            idx = text.indexOf("  ", idx+1)
        }
    }
}