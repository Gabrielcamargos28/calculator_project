package br.com.gabriel.calculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var editText: EditText

    var conta: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        editText = findViewById(R.id.editTextConta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
            }
    }
    fun positionButtom(view: View) = when (view.id) {
        R.id.btn0 -> digite("0")
        R.id.btn1 -> digite("1")
        R.id.btn2 -> digite("2")
        R.id.btn3 -> digite("3")
        R.id.btn4 -> digite("4")
        R.id.btn5 -> digite("5")
        R.id.btn6 -> digite("6")
        R.id.btn7 -> digite("7")
        R.id.btn8 -> digite("8")
        R.id.btn9 -> digite("9")
        R.id.btnMais -> digite("+")
        R.id.btnMenos -> digite("-")
        R.id.btnDividi -> digite("/")
        R.id.btnMult -> digite("*")
        R.id.btnPonto -> digite(".")
        R.id.btnLimparT -> limpar()
        R.id.btnIgual -> calcular()
        R.id.btnDeletar -> deletar(1)
        else -> Log.d("nada", "Algo diferente")
    }

    fun limpar() {
        editText.setText("")
        conta = ""
    }

    fun digite(s: String) {
        val lastChar = if (conta.isNotEmpty()) conta.last().toString() else ""
        if (s in setOf("+", "-", "*", "/")) {
            if (lastChar in setOf("+", "-", "*", "/")) {
                conta = conta.dropLast(1) // Remove the last operator if it exists
            }
            conta += " $s "
        } else {
            conta += s
        }
        editText.setText(conta)
    }

    fun executarOperacao(numero1: Double, operador: Char, numero2: Double): Double {
        var resultado: Double = 0.0
        if (operador == '+') {
            resultado = numero1 + numero2
        } else if (operador == '-') {
            resultado = numero1 - numero2
        } else if (operador == '/') {
            resultado = numero1 / numero2
        } else if (operador == '*') {
            resultado = numero1 * numero2
        }
        return resultado
    }

    fun calcular() {
        val expression = mutableListOf<Char>()
        val numbers = mutableListOf<Double>()
        val operators = setOf('+', '-', '*', '/')

        var tempNum = StringBuilder()
        for (char in conta) {
            if (char in operators) {
                if (tempNum.isNotEmpty()) {
                    numbers.add(tempNum.toString().toDouble())
                    tempNum.clear()
                }
                expression.add(char)
            } else {
                tempNum.append(char)
            }
        }
        if (tempNum.isNotEmpty()) {
            numbers.add(tempNum.toString().toDouble())
        }

        try {
            while (expression.isNotEmpty()) {
                val number1 = numbers.removeAt(0)
                val operator = expression.removeAt(0)
                val number2 = numbers.removeAt(0)
                val total = executarOperacao(number1, operator, number2)
                numbers.add(0, total) // Update the result in the list
            }
            editText.setText(numbers[0].toString())
            conta = numbers[0].toString() // Update conta
        } catch (e: Exception) {
            editText.setText("Error")
            conta = ""
        }
    }



    fun deletar(int: Int) {
        if (conta.length > 0) {
            conta = conta.substring(0, conta.length - int)
            editText.setText(conta)
        }
    }
    fun verificarOperacao(aux: String, operacao: String): String {
        var retorno: String = operacao
        if (aux == operacao) {
            retorno = ""
        } else if (operacao.equals("+")) {
            if (aux.equals("-") || aux.equals("/") || aux.equals("*")) {
                retorno = operacao + " "
                deletar(2)
            } else {
                retorno = " + "
            }
        } else if (operacao.equals("-")) {
            if (aux.equals("+") || aux.equals("/") || aux.equals("*")) {
                retorno = operacao + " "
                deletar(2)
            } else {
                retorno = " - "
            }
        } else if (operacao.equals("/")) {
            if (aux.equals("+") || aux.equals("-") || aux.equals("*")) {
                retorno = operacao + " "
                deletar(2)
            } else {
                retorno = " / "
            }
        } else if (operacao.equals("*")) {
            if (aux.equals("+") || aux.equals("/") || aux.equals("-")) {
                retorno = operacao + " "
                deletar(2)
            } else {
                retorno = " * "
            }
        } else {
            if (operacao.equals("+")) {
                retorno = " + "
            } else if (operacao.equals("-")) {
                retorno = " - "
            } else if (operacao.equals("/")) {
                retorno = " / "
            } else if (operacao.equals("*")) {
                retorno = " * "
            } else {
                retorno = operacao
            }
        }
        return retorno
    }
}