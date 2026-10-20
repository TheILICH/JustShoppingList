package com.example.justshoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.justshoppinglist.ui.theme.JustShoppingListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JustShoppingListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShoppingScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}