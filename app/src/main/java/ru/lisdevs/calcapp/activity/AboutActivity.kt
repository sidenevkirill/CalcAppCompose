package ru.lisdevs.calcapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

class AboutActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AboutScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AboutScreen() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("О приложении") },
                    navigationIcon = {
                        IconButton(onClick = { onBackPressed() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                        }
                    },
                    actions = {
                        // Здесь можно добавить дополнительные действия для Toolbar, если нужно
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = { share() }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Public, contentDescription = "Rustore", modifier = Modifier.padding(end = 8.dp))
                        Text("Поделиться")
                    }
                }

                Button(onClick = { openUrl("https://vk.com/railcinec") }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Public, contentDescription = "VK", modifier = Modifier.padding(end = 8.dp))
                        Text("VK")
                    }
                }

                Button(onClick = { openUrl("https://t.me/railcinec") }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Public, contentDescription = "Telegram", modifier = Modifier.padding(end = 8.dp))
                        Text("Telegram")
                    }
                }

                Button(onClick = { openUrl("https://sidenevkirill.github.io") }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Public, contentDescription = "Сайт", modifier = Modifier.padding(end = 8.dp))
                        Text("Сайт")
                    }
                }
            }
        }
    }

    private fun share() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.rustore.ru/catalog/app/ru.lisdevs.calculator")
            )
        )
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAboutScreen() {
    //AboutScreen()
}