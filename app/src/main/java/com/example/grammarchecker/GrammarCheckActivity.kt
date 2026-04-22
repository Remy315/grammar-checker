package com.example.grammarchecker

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.BackgroundColorSpan
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator

class GrammarCheckActivity : AppCompatActivity() {

    private lateinit var inputText: EditText
    private lateinit var resultText: TextView
    private lateinit var checkButton: Button
    private lateinit var clearButton: Button
    private lateinit var languageToggle: Button
    private lateinit var practiceModeRadio: RadioButton
    private lateinit var autoModeRadio: RadioButton
    private lateinit var modeDescription: TextView
    private lateinit var resultLayout: LinearLayout

    private var isEnglishMode = true
    private var currentMode = GrammarMode.PRACTICE
    private var grammarChecker = GrammarChecker()
    private var userCorrections = mutableMapOf<Int, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grammar_check)

        initializeViews()
        setupClickListeners()
        updateModeDescription()
    }

    private fun initializeViews() {
        inputText = findViewById(R.id.inputText)
        resultText = findViewById(R.id.resultText)
        checkButton = findViewById(R.id.checkButton)
        clearButton = findViewById(R.id.clearButton)
        languageToggle = findViewById(R.id.languageToggle)
        practiceModeRadio = findViewById(R.id.practiceModeRadio)
        autoModeRadio = findViewById(R.id.autoModeRadio)
        modeDescription = findViewById(R.id.modeDescription)
        resultLayout = findViewById(R.id.resultLayout)
    }

    private fun setupClickListeners() {
        checkButton.setOnClickListener {
            checkGrammar()
        }

        clearButton.setOnClickListener {
            clearAll()
        }

        languageToggle.setOnClickListener {
            toggleLanguage()
        }

        practiceModeRadio.setOnClickListener {
            currentMode = GrammarMode.PRACTICE
            updateModeDescription()
            if (resultLayout.visibility == View.VISIBLE) {
                checkGrammar() // Re-check in new mode
            }
        }

        autoModeRadio.setOnClickListener {
            currentMode = GrammarMode.AUTO_CORRECT
            updateModeDescription()
            if (resultLayout.visibility == View.VISIBLE) {
                checkGrammar() // Re-check in new mode
            }
        }

        resultText.setOnClickListener {
            if (currentMode == GrammarMode.AUTO_CORRECT) {
                copyToClipboard()
            } else {
                toggleLanguageForResults()
            }
        }
        
        resultText.setOnLongClickListener {
            if (currentMode == GrammarMode.PRACTICE) {
                showCorrectionDialog()
                true
            } else {
                false
            }
        }
    }

    private fun checkGrammar() {
        val input = inputText.text.toString().trim()
        if (input.isEmpty()) {
            Toast.makeText(this, "Please enter some text to check", Toast.LENGTH_SHORT).show()
            return
        }

        val errors = grammarChecker.checkGrammar(input)
        
        when (currentMode) {
            GrammarMode.PRACTICE -> {
                displayPracticeModeResults(input, errors)
            }
            GrammarMode.AUTO_CORRECT -> {
                displayAutoCorrectResults(input, errors)
            }
        }

        resultLayout.visibility = View.VISIBLE
    }

    private fun displayPracticeModeResults(originalText: String, errors: List<GrammarError>) {
        val spannableString = SpannableString(originalText)
        val displayText = if (isEnglishMode) originalText else "单击英文显示中文"
        
        // Highlight errors in red
        errors.forEach { error ->
            val redSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.error_red))
            spannableString.setSpan(
                redSpan,
                error.startIndex,
                error.endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            
            // Add background highlight
            val backgroundSpan = BackgroundColorSpan(ContextCompat.getColor(this, R.color.highlight_yellow))
            spannableString.setSpan(
                backgroundSpan,
                error.startIndex,
                error.endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        resultText.text = spannableString
        
        // Add error summary
        val errorCount = errors.size
        val statusText = if (isEnglishMode) {
            "Found $errorCount grammar error${if (errorCount == 1) "" else "s"}. Red text needs correction."
        } else {
            "发现$errorCount个语法错误。红色文字需要修改。"
        }
        
        Toast.makeText(this, statusText, Toast.LENGTH_LONG).show()
    }

    private fun displayAutoCorrectResults(originalText: String, errors: List<GrammarError>) {
        var correctedText = originalText
        val corrections = mutableListOf<String>()
        
        // Apply corrections in reverse order to maintain indices
        errors.sortedByDescending { it.startIndex }.forEach { error ->
            val originalPart = originalText.substring(error.startIndex, error.endIndex)
            correctedText = correctedText.replaceRange(error.startIndex, error.endIndex, error.suggestedCorrection)
            
            val errorDesc = if (isEnglishMode) error.description else error.descriptionChinese
            corrections.add("'$originalPart' → '${error.suggestedCorrection}' ($errorDesc)")
        }

        val displayText = if (isEnglishMode) {
            "Click to copy corrected text\n\nCorrected: $correctedText"
        } else {
            "点击复制修正后的文本\n\n修正后: $correctedText"
        }

        resultText.text = displayText
        
        // Show corrections list
        if (corrections.isNotEmpty()) {
            val correctionsText = corrections.joinToString("\n")
            Toast.makeText(this, "Corrections:\n$correctionsText", Toast.LENGTH_LONG).show()
        }
    }

    private fun copyToClipboard() {
        val input = inputText.text.toString().trim()
        val errors = grammarChecker.checkGrammar(input)
        
        var correctedText = input
        errors.sortedByDescending { it.startIndex }.forEach { error ->
            correctedText = correctedText.replaceRange(error.startIndex, error.endIndex, error.suggestedCorrection)
        }

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Corrected Text", correctedText)
        clipboard.setPrimaryClip(clip)
        
        Toast.makeText(this, "Corrected text copied to clipboard!", Toast.LENGTH_SHORT).show()
    }

    private fun toggleLanguageForResults() {
        isEnglishMode = !isEnglishMode
        checkGrammar() // Re-check with new language
    }

    private fun toggleLanguage() {
        isEnglishMode = !isEnglishMode
        
        if (isEnglishMode) {
            languageToggle.text = "EN"
            checkButton.text = "Check Grammar"
            clearButton.text = "Clear"
        } else {
            languageToggle.text = "中文"
            checkButton.text = "检查语法"
            clearButton.text = "清空"
        }
        
        updateModeDescription()
        
        // If results are visible, toggle their language too
        if (resultLayout.visibility == View.VISIBLE) {
            checkGrammar()
        }
    }

    private fun updateModeDescription() {
        val description = when (currentMode) {
            GrammarMode.PRACTICE -> {
                if (isEnglishMode) {
                    "Practice mode: Red highlights let you fix errors yourself. Correct answers turn green with animations!"
                } else {
                    "练习模式：红色高亮显示错误，自己动手修改。答对了会变绿并有动画效果！"
                }
            }
            GrammarMode.AUTO_CORRECT -> {
                if (isEnglishMode) {
                    "Auto-correct mode: Get suggestions and copy corrected text with one click"
                } else {
                    "自动修正模式：获取建议并一键复制修正后的文本"
                }
            }
        }
        
        modeDescription.text = description
        
        // Update radio button texts
        if (isEnglishMode) {
            practiceModeRadio.text = "Practice Mode"
            autoModeRadio.text = "Auto Correct Mode"
        } else {
            practiceModeRadio.text = "练习模式"
            autoModeRadio.text = "自动修正模式"
        }
    }

    private fun clearAll() {
        inputText.text.clear()
        resultText.text = ""
        resultLayout.visibility = View.GONE
        userCorrections.clear()
        Toast.makeText(this, 
            if (isEnglishMode) "Cleared" else "已清空", 
            Toast.LENGTH_SHORT).show()
    }

    private fun showCorrectionDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        val input = EditText(this)
        input.hint = if (isEnglishMode) "Enter your correction" else "输入你的修改"
        
        builder.setTitle(if (isEnglishMode) "Make Correction" else "进行修改")
        builder.setView(input)
        
        builder.setPositiveButton(if (isEnglishMode) "Correct" else "修改") { _, _ ->
            val correction = input.text.toString().trim()
            if (correction.isNotEmpty()) {
                applyUserCorrection(correction)
            }
        }
        
        builder.setNegativeButton(if (isEnglishMode) "Cancel" else "取消") { dialog, _ ->
            dialog.cancel()
        }
        
        builder.show()
    }
    
    private fun applyUserCorrection(correction: String) {
        val originalText = inputText.text.toString()
        val errors = grammarChecker.checkGrammar(originalText)
        
        if (errors.isNotEmpty()) {
            // Apply correction to the first error found
            val error = errors.first()
            val correctedText = originalText.replaceRange(
                error.startIndex,
                error.endIndex,
                correction
            )
            
            inputText.setText(correctedText)
            userCorrections[error.startIndex] = correction
            
            // Check if correction is correct
            if (correction.equals(error.suggestedCorrection, ignoreCase = true)) {
                // Correct answer - show animations
                showCorrectAnswerAnimation()
            } else {
                // Incorrect answer
                Toast.makeText(this, 
                    if (isEnglishMode) "Try again! The correct answer is: ${error.suggestedCorrection}" 
                    else "再试试！正确答案是：${error.suggestedCorrection}", 
                    Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun showCorrectAnswerAnimation() {
        // Create green slide animation
        AnimationHelper.createGreenSlideAnimation(resultLayout)
        
        // Create confetti animation
        AnimationHelper.createConfettiAnimation(resultLayout.parent as ViewGroup)
        
        // Show success message
        val message = if (isEnglishMode) {
            "🎉 Correct! Well done!"
        } else {
            "🎉 正确！做得好！"
        }
        
        AnimationHelper.showSuccessToast(resultLayout, message)
        
        // Also animate the result text background
        AnimationHelper.animateCorrectAnswer(resultText)
    }

    enum class GrammarMode {
        PRACTICE,
        AUTO_CORRECT
    }
}