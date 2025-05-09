package ru.lisdevs.calcapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.lisdevs.calcapp.R

class MainActivity : ComponentActivity() {

    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

        setStatusBarColor(isDarkTheme = false)
        setNavigationBarColor(isDarkTheme = false)

        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            LaunchedEffect(isDarkTheme) {
                setStatusBarColor(isDarkTheme)
                setNavigationBarColor(isDarkTheme)
            }

            CalculatorApp(isDarkTheme) { newTheme ->
                isDarkTheme = newTheme
            }
        }
    }

    private fun setStatusBarColor(isDarkTheme: Boolean) {
        window.statusBarColor = if (isDarkTheme) {
            Color(0xFF1C1B1F).toArgb()
        } else {
            Color(0xFFFFFBFF).toArgb()
        }

        val decorView = window.decorView
        if (isDarkTheme) {
            decorView.systemUiVisibility = 0
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun setNavigationBarColor(isDarkTheme: Boolean) {
        window.navigationBarColor = if (isDarkTheme) {
            Color(0xFF1C1B1F).toArgb()
        } else {
            Color(0xFFFFFBFF).toArgb()
        }

        // Установка цвета значков в навигационной панели
        val decorView = window.decorView
        if (isDarkTheme) {
            decorView.systemUiVisibility = decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        } else {
            decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CalculatorApp(isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
        var expression by remember { mutableStateOf("") }
        var result by remember { mutableStateOf("0") }

        val colorScheme = if (isDarkTheme) {
            darkColorScheme()
        } else {
            lightColorScheme()
        }

        MaterialTheme(colorScheme = colorScheme) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Калькулятор") },
                        actions = {
                            IconButton(onClick = { goToAnotherActivity() }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.help_circle_outline),
                                    contentDescription = "О приложении"
                                )
                            }
                            IconButton(onClick = { goThemeActivity() }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_vector_outline_settings),
                                    contentDescription = "Настройки"
                                )
                            }
                            IconButton(onClick = { onThemeChange(!isDarkTheme) }) {
                                Icon(
                                    imageVector = if (isDarkTheme)
                                        ImageVector.vectorResource(id = R.drawable.ic_sun)
                                    else
                                        ImageVector.vectorResource(id = R.drawable.ic_moon),
                                    contentDescription = "Переключить тему"
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = expression, style = MaterialTheme.typography.headlineMedium)
                    Text(text = result, style = MaterialTheme.typography.headlineLarge)

                    Column {
                        val buttons = listOf(
                            "1", "2", "3", "/",
                            "4", "5", "6", "*",
                            "7", "8", "9", "-",
                            "C", "0", "=", "+"
                        )
                        buttons.chunked(4).forEach { row ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                row.forEach { buttonText ->
                                    Button(
                                        onClick = {
                                            handleButtonClick(buttonText, { vibrate() }, expression, result) { newExpression, newResult ->
                                                expression = newExpression
                                                result = newResult
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.DarkGray
                                        ),
                                        modifier = Modifier.size(80.dp).padding(4.dp)
                                    ) {
                                        Text(
                                            text = buttonText,
                                            color = Color.White,
                                            fontSize = 24.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleButtonClick(
        buttonText: String,
        vibrate: () -> Unit,
        currentExpression: String,
        currentResult: String,
        updateExpressionAndResult: (String, String) -> Unit
    ) {
        vibrate()

        when (buttonText) {
            in "0".."9" -> updateExpressionAndResult(currentExpression + buttonText, currentResult)
            "+", "-", "*", "/" -> updateExpressionAndResult("$currentExpression $buttonText ", currentResult)
            "=" -> {
                val evaluatedResult = evaluateExpression(currentExpression.trim())
                updateExpressionAndResult("", evaluatedResult)
            }
            "C" -> updateExpressionAndResult("", "0")
        }
    }

    private fun evaluateExpression(expr: String): String {
        return try {
            val evaluatedResult= eval(expr)
            Math.round(evaluatedResult).toString()
        } catch (e: Exception) {
            "Ошибка"
        }
    }

    private fun eval(expr: String): Double {
        val parts= expr.split(" ")
        if(parts.size != 3 ) throw IllegalArgumentException("Неверное выражение")

        val left= parts[0].toDouble()
        val right= parts[2].toDouble()
        return when(parts[1]){
            "+"-> left + right
            "-"-> left - right
            "*"-> left * right
            "/"-> left / right
            else-> throw IllegalArgumentException("Неверный оператор")
        }
    }

    private fun vibrate() {
        vibrator.vibrate(50)
    }

    private fun goToAnotherActivity() {
        startActivity(Intent(this, AboutActivity::class.java))
    }

    private fun goThemeActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }
}

@Preview(showBackground=true)
@Composable
fun PreviewCalculatorApp() {
    MainActivity().CalculatorApp(isDarkTheme=false){}
}