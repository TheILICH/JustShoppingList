package com.example.justshoppinglist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class ShoppingItem(
    val id: Int,
    val name: String,
    val isBought: Boolean = false
)

class ShoppingViewModel : ViewModel() {

    private var nextId = 4

    // list lives here so rotation does not kill it
    private val items = mutableStateListOf(
        ShoppingItem(1, "Milk"),
        ShoppingItem(2, "Bread"),
        ShoppingItem(3, "Eggs", isBought = true)
    )

    var searchQuery by mutableStateOf("")
        private set

    // filtered list for the screen
    val visibleItems: List<ShoppingItem>
        get() = items.filter { it.name.contains(searchQuery, ignoreCase = true) }

    val boughtCount: Int
        get() = items.count { it.isBought }

    val totalCount: Int
        get() = items.size

    fun addItem(name: String) {
        if (name.isBlank()) return
        items.add(ShoppingItem(nextId++, name.trim()))
    }

    fun toggleBought(item: ShoppingItem) {
        val index = items.indexOfFirst { it.id == item.id }
        // replace with copy so compose sees the change
        if (index != -1) items[index] = item.copy(isBought = !item.isBought)
    }

    fun deleteItem(item: ShoppingItem) {
        items.removeAll { it.id == item.id }
    }

    fun onSearchChange(text: String) {
        searchQuery = text
    }
}