package com.example.tictactoe

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Screen {
    const val START = "start_screen"
    const val GAME = "game_screen"
}

@Composable
fun GameNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.START) {
        composable(Screen.START) {
            StartScreen(onStartGameClicked = { navController.navigate(Screen.GAME) })
        }
        composable(Screen.GAME) {
            GameScreen()
        }
    }
}