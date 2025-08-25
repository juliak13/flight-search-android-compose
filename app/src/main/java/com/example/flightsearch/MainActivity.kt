package com.example.flightsearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flightsearch.ui.FlightListScreen
import com.example.flightsearch.ui.FlightSearchScreen
import com.example.flightsearch.ui.theme.FlightSearchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlightSearchTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "search") {
        composable("search") {
            FlightSearchScreen(navController)
        }
        composable("flights/{departureCode}") { backStackEntry ->
            val departureCode = backStackEntry.arguments?.getString("departureCode") ?: ""
            FlightListScreen(navController, departureCode)
        }
    }
}
