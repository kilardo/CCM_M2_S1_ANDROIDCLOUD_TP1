package fr.upjv.firstproject.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onButtonClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Android versions")
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                // Texte en haut
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Explorer l'évolution des versions de Android",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Découvrez les différentes versions, leurs numéros " +
                                "et leurs années de sortie, regroupées par nom.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 🔽 espace flexible occupé par l'image
                Box(
                    modifier = Modifier
                        .weight(1f)          // prend tout l’espace entre le texte et le bouton
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = "https://giffiles.alphacoders.com/216/216414.gif",
                        contentDescription = "Android dance",
                        modifier = Modifier
                            .fillMaxWidth(0.9f)          // 90% de la largeur
                            .height(260.dp),             // hauteur max raisonnable
                        contentScale = ContentScale.Crop
                    )
                }

                // Bouton en bas
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ElevatedButton(
                        onClick = onButtonClick,
                    ) {
                        Text(
                            text = "Voir les versions Android",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
