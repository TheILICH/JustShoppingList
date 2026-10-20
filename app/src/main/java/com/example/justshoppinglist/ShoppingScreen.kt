package com.example.justshoppinglist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ShoppingScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingViewModel = viewModel()
) {
    // draft text, survives rotation
    var newItemText by rememberSaveable { mutableStateOf("") }
    // ui only, resets on rotation (for demo)
    var showInfo by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderCard(
            bought = viewModel.boughtCount,
            total = viewModel.totalCount,
            showInfo = showInfo,
            onInfoClick = { showInfo = !showInfo }
        )

        AddItemRow(
            text = newItemText,
            onTextChange = { newItemText = it },
            onAddClick = {
                viewModel.addItem(newItemText)
                newItemText = ""
            }
        )

        OutlinedTextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.onSearchChange(it) },
            label = { Text("Search") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        val visible = viewModel.visibleItems
        if (visible.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(visible, key = { it.id }) { item ->
                    ShoppingItemCard(
                        item = item,
                        onCheckedChange = { viewModel.toggleBought(item) },
                        onDeleteClick = { viewModel.deleteItem(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderCard(
    bought: Int,
    total: Int,
    showInfo: Boolean,
    onInfoClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // logo inside a colored circle
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "App logo",
                        modifier = Modifier.size(56.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Shopping List", style = MaterialTheme.typography.titleLarge)
                    Text("Bought $bought of $total", style = MaterialTheme.typography.bodyMedium)
                }
                TextButton(onClick = onInfoClick) {
                    Text(if (showInfo) "Hide" else "Info")
                }
            }
            if (showInfo) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Add things you need, tick them when bought. Rotate the phone and see what stays!",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun AddItemRow(
    text: String,
    onTextChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text("New item") },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        Button(onClick = onAddClick, enabled = text.isNotBlank()) {
            Text("Add")
        }
    }
}

@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    onCheckedChange: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = item.isBought, onCheckedChange = { onCheckedChange() })
            Text(
                text = item.name,
                modifier = Modifier.weight(1f),
                textDecoration = if (item.isBought) TextDecoration.LineThrough else TextDecoration.None
            )
            TextButton(onClick = onDeleteClick) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Nothing here", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}