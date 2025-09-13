package com.example.simplecalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var mathOperationTextView: TextView
    private lateinit var resultTextView: TextView
    private var currentInput = StringBuilder()
    private var currentOperation: String? = null
    private var firstOperand: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mathOperationTextView = findViewById(R.id.math_operation)
        resultTextView = findViewById(R.id.result_text)

        // Инициализация кнопок цифр
        val digitButtons = listOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9
        )

        // Обработчики для цифровых кнопок
        digitButtons.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                onDigitButtonClick((it as Button).text.toString())
            }
        }

        // Обработчики для операций
        findViewById<Button>(R.id.btn_plus).setOnClickListener { onOperationButtonClick("+") }
        findViewById<Button>(R.id.btn_minus).setOnClickListener { onOperationButtonClick("-") }
        findViewById<Button>(R.id.btn_mult).setOnClickListener { onOperationButtonClick("*") }
        findViewById<Button>(R.id.btn_del).setOnClickListener { onOperationButtonClick("/") }

        // Кнопка равно
        findViewById<Button>(R.id.btn_equal).setOnClickListener { onEqualsButtonClick() }

        // Кнопка очистки
        findViewById<Button>(R.id.btn_C).setOnClickListener { onClearButtonClick() }

        // Кнопка back (удаление последнего символа)
        findViewById<Button>(R.id.btn_back).setOnClickListener { onBackButtonClick() }
    }
    private fun onDigitButtonClick(digit: String) {
        currentInput.append(digit)
        resultTextView.text = currentInput.toString()
    }
    private fun onOperationButtonClick(operation: String) {
        if (currentInput.isNotEmpty()) {
            firstOperand = currentInput.toString().toDouble()
            currentOperation = operation
            currentInput.clear()
            resultTextView.text = "0"

            // Показываем операцию в mathOperationTextView
            mathOperationTextView.text = "$firstOperand $operation"
        }
    }
    private fun onBackButtonClick() {
        if (currentInput.isNotEmpty()) {
            currentInput.deleteCharAt(currentInput.length - 1)
            if (currentInput.isEmpty()) {
                resultTextView.text = "0"
            } else {
                resultTextView.text = currentInput.toString()
            }

            // Обновляем отображение операции если нужно
            if (currentOperation != null) {
                mathOperationTextView.text = "$firstOperand $currentOperation"
            }
        }
    }
    private fun onEqualsButtonClick() {
        if (currentInput.isNotEmpty() && currentOperation != null) {
            val secondOperand = currentInput.toString().toDouble()
            val result = performCalculation(secondOperand)

            resultTextView.text = formatResult(result)
            currentInput.clear()
            currentInput.append(if (result.isNaN()) "0" else result.toString())

            // Показываем полное выражение
            mathOperationTextView.text = "$firstOperand $currentOperation $secondOperand ="
            currentOperation = null
        }
    }
    private fun performCalculation(secondOperand: Double): Double {
        return when (currentOperation) {
            "+" -> firstOperand + secondOperand
            "-" -> firstOperand - secondOperand
            "*" -> firstOperand * secondOperand
            "/" -> if (secondOperand != 0.0) firstOperand / secondOperand else Double.NaN
            else -> Double.NaN
        }
    }
    private fun formatResult(result: Double): String {
        return when {
            result.isNaN() -> "Error"
            result % 1 == 0.0 -> result.toInt().toString()
            else -> String.format("%.2f", result).trimEnd('0').trimEnd('.')
        }
    }
    private fun onClearButtonClick() {
        currentInput.clear()
        currentOperation = null
        resultTextView.text = "0"

        // Очищаем отображение операции
        mathOperationTextView.text = ""
    }
}