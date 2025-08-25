package com.example.flightsearch.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.flightsearch.data.AppDatabase
import com.example.flightsearch.data.Favorite
import kotlinx.coroutines.launch

@Composable
fun FlightListScreen(navController: NavHostController, departureCode: String) {

    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val viewModel: FlightViewModel = viewModel(factory = FlightViewModelFactory(db))
    val favorites by viewModel.favorites.collectAsState()
    val flights = remember { mutableStateListOf<Favorite>() }
    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(departureCode) {
        coroutineScope.launch {
            val results = viewModel.getFlightsFromDeparture(departureCode)
            flights.clear()
            flights.addAll(results)
            isLoading = false
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(text = "Flights from $departureCode", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            flights.isEmpty() -> {
                Text(
                    text = "No flights found from this airport.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            else -> {
                LazyColumn {
                    items(flights) { flight ->
                        FlightItem(flight, viewModel, favorites)
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteItem(favorite: Favorite, viewModel: FlightViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Depart: ${favorite.departure_code}")
                Text("Arrive: ${favorite.destination_code}")
            }

            IconButton(onClick = {
                viewModel.removeFavorite(favorite.departure_code, favorite.destination_code)
            }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Unfavorite",
                    tint = Color.Yellow
                )
            }
        }
    }
}
