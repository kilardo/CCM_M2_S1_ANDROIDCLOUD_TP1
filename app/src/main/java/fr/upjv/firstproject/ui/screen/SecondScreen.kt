package fr.upjv.firstproject.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import fr.upjv.firstproject.ui.model.ItemUi
import fr.upjv.firstproject.ui.viewmodel.AndroidVersionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(
    navigateBack: () -> Unit,
    viewModel: AndroidVersionViewModel = viewModel(),
) {
    val listOfResult by viewModel.androidVersionList.collectAsState()

    // item sélectionné pour la suppression
    var selectedItem by remember { mutableStateOf<ItemUi.Item?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Android versions")
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                tonalElevation = 8.dp
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    onClick = { viewModel.insertAndroidVersion() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add version"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add")
                }

                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    onClick = { viewModel.deleteAllAndroidVersion() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete all versions"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Delete")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            MyScreen(
                modifier = Modifier.fillMaxSize(),
                listOfResult = listOfResult,
                onItemClick = { item ->
                    selectedItem = item
                }
            )

            // Dialog de confirmation de suppression
            selectedItem?.let { item ->
                ConfirmDeleteDialog(
                    item = item,
                    onConfirm = {
                        viewModel.deleteAndroidVersion(item)
                        selectedItem = null
                    },
                    onDismiss = {
                        selectedItem = null
                    }
                )
            }
        }
    }
}

@Composable
private fun MyScreen(
    modifier: Modifier = Modifier,
    listOfResult: List<ItemUi>,
    onItemClick: (ItemUi.Item) -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(listOfResult) { item ->
                when (item) {
                    is ItemUi.Header -> HeaderRow(header = item)
                    is ItemUi.Item -> VersionItemRow(item = item, onClick = { onItemClick(item) })
                    is ItemUi.Footer -> FooterRow(footer = item)
                }
            }
        }
    }
}

/**
 * Header de groupe (HoneyComb / Ice Cream Sandwich / Jelly Bean, etc.)
 */
@Composable
private fun HeaderRow(header: ItemUi.Header) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = header.image,
                contentDescription = header.title,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = header.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Famille de versions Android",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider()
    }
}

/**
 * Card pour l’affichage d’un Item : nom + version + année
 */
@Composable
private fun VersionItemRow(
    item: ItemUi.Item,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },   // 👈 clic sur la card
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = item.versionName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Version ${item.versionNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "LTS : ${if (item.lts) "Oui" else "Non"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Nombre d'utilisateur : ${item.numberOfUser}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = "Year ${item.year}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FooterRow(footer: ItemUi.Footer) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 16.dp),
        text = footer.title,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

/**
 * Dialog de confirmation de suppression
 */
@Composable
private fun ConfirmDeleteDialog(
    item: ItemUi.Item,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Supprimer cette version ?")
        },
        text = {
            Text(
                text = "Voulez-vous supprimer la version ${item.versionName} (${item.versionNumber}) ?",
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Supprimer",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Annuler")
            }
        }
    )
}
