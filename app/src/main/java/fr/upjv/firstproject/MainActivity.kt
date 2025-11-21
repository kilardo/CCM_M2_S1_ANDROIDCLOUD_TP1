package fr.upjv.firstproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import fr.upjv.firstproject.ui.navigation.ApplicationNavHost
import fr.upjv.firstproject.ui.theme.FirstProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirstProjectTheme {
                Box {
                    val navController = rememberNavController()
                    ApplicationNavHost(
                        navController = navController,
                    )
                }
            }
        }
    }

    @Composable
    fun AnimatedContent() {
        val isVisible = remember { mutableStateOf(true) }


        AnimatedVisibility(
            visible = isVisible.value,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Text("Spotted")
        }


        Button(
            onClick = {
                isVisible.value = isVisible.value.not()
            }
        ) {
            Text(text = "Show/Hide Text")
        }
    }

}
